package jp.co.dmm.customize.component.callbackFunction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang.StringUtils;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.input.MoveActivityInstanceInParam;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsGrid;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 新規取引先申請の承認後に呼び出されるコールバックファンクション。
 * WF新規取引先申請テーブル(CNTR010001、CNTR010003、CNTR011001)からDMMの取引先マスタテーブル(SPLR_MST)、振込先銀行口座マスタ(PAYEE_BNKACC_MST)へデータを吸い出す。
 * 【取引先変更は対象ではない】
 */
public class CallbackFunctionCreate_SPLR_MST_RLT_PRT extends BaseCallbackFunction {

	/**
	 * 取引先コードの基礎数字：2から始まり、9桁数
	 */
	public static final long SPLR_CD_MIN_NUMERING = 200000000;
	public static final long SPLR_CD_MAX_NUMERING = 209999999;
	public static final int SPLR_CD_MAX_LENGTH = 9;
	public static final String PAYEE_BNKACC_CD_SS_MIN = "0001";
	public static final int PAYEE_BNKACC_CD_SS_MAX = 4999;

	private Map<String, Set<String>> inputedPayeeBnkaccCdSsMap;

	@Transactional
	@Override
	public void execute(
			InParamCallbackBase param
			, OutParamCallbackBase result
			, String actionType
			, Vd0310Contents contents
			, RuntimeContext ctx
			, WfvFunctionDef functionDef) {

		final MoveActivityInstanceInParam in = (MoveActivityInstanceInParam)param;
		final PartsRootContainer root = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

		// 会社コード
		String companyCd = ctx.runtimeMap.get("TXT0301").getValue();

		// 取引先情報INSERT
		// INSERT行数
		int count = 0;

		// 取引先コード（同一の取引先の場合は設定あり）
		String splrCd = ctx.runtimeMap.get("TXT0003").getValue();
		List<String> crpIds = new ArrayList<>();

		if (StringUtils.isEmpty(splrCd)) {
			splrCd = getMaxSplrCd();
//			isExist = false;
		}

		// 申請者の所属会社に対してインサート
		count = insertSPLR_MST(in, root.rows.get(0).runtimeId, splrCd, companyCd);
		crpIds.add(companyCd);

		if (count == 0) {
			throw new InternalServerErrorException("取引先情報の実インサート行数が0でした。");
		}

		// 振込先銀行口座マスタINSERT
		// INSERT行数
		count = 0;

		// 取引先口座明細情報を整理
		getInputedPayeeBnkaccCdSsMap(ctx, companyCd);

		// CNTR011001からPAYEE_BNKACC_MSTへ転写
		PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD1000");	// 口座明細のリピーター

		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";
			String bnkCd = ctx.runtimeMap.get(prefix + "TXT1001").getValue();
			String bnkbrcCd = ctx.runtimeMap.get(prefix + "TXT1003").getValue();
			String payeeBnkaccCdSs = ctx.runtimeMap.get(prefix + "TXT1013").getValue().trim();

			if (isNotEmpty(bnkCd) && isNotEmpty(bnkbrcCd)) {

				if (isEmpty(payeeBnkaccCdSs)) {
					payeeBnkaccCdSs = getMaxPayeeBnkaccCdSs(companyCd, splrCd);
					while (inputedPayeeBnkaccCdSsMap.containsKey(companyCd) && inputedPayeeBnkaccCdSsMap.get(companyCd).contains(payeeBnkaccCdSs)) {
						payeeBnkaccCdSs = StringUtils.leftPad(String.valueOf(Integer.parseInt(payeeBnkaccCdSs) + 1), 4, "0");
					}

					if ((Integer.parseInt(payeeBnkaccCdSs)) > PAYEE_BNKACC_CD_SS_MAX) {
						throw new InternalServerErrorException("振込先銀行口座コード（SuperStream）は最大値の上限値を超えました。");
					}
				}

				//DGHD管理フローの場合は連携対象の会社に対してインサート
				//申請者の所属会社に対してインサート
				count = insertPAYEE_BNKACC_MST(in, row.runtimeId, splrCd, payeeBnkaccCdSs, companyCd);

				if (count == 0) {
					throw new InternalServerErrorException("取引先口座管理情報の実インサート行数が0でした。");
				}
			}
		}

		// 反社情報（親）
		{
			// 関係先マスタ（親）
			count = insertRLT_PRT_MST_PARENT(in, root.rows.get(0).runtimeId, splrCd, companyCd);
			if (count == 0) {
				throw new InternalServerErrorException("関係先マスタの実インサート行数が0でした。");
			}
			// 反社情報（親）
			{
				final PartsGrid orgCrmGrid = (PartsGrid)ctx.runtimeMap.get("GRD2041");	// 反社情報（親）のリピーター
				for (PartsContainerRow row : orgCrmGrid.rows) {
					insertORG_CRM_INF_PARENT(in, row.runtimeId, splrCd, companyCd, row.rowId);
				}
			}
		}

		// 反社情報（子）
		{
			final PartsGrid rltPrtGrid = (PartsGrid)ctx.runtimeMap.get("GRD2012");	// 反社情報（親）のリピーター
			for (PartsContainerRow row : rltPrtGrid.rows) {
				insertRLT_PRT_MST_CHILD(in, row.runtimeId, splrCd, companyCd, row.rowId);
				// 反社情報（親）
				{
					final String prefix = rltPrtGrid.htmlId + "-" + row.rowId + "_";
					final PartsGrid orgCrmGrid = (PartsGrid)ctx.runtimeMap.get(prefix + "GRD0015");	// 反社情報（親）のリピーター
					for (PartsContainerRow row2 : orgCrmGrid.rows) {
						insertORG_CRM_INF_CHILD(in, row2.runtimeId, splrCd, companyCd, row.rowId, row2.rowId);
					}
				}
			}
		}
	}

	/** 取引先情報テーブルをINSERT */
	private int insertSPLR_MST(
			MoveActivityInstanceInParam in, Long runtimeId, String spLrCd, String corpCd) {
		final String sql = getSql("SP0000_10");
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final Object[] args = {
				corpCd, spLrCd, corpCd,
				corporationCode, userCode, ipAddr, now,
				corporationCode, userCode, ipAddr, now,
				runtimeId
		};
		return execSql(sql, args);
	}

	/** 振込先銀行口座マスタテーブルをINSERT */
	private int insertPAYEE_BNKACC_MST(MoveActivityInstanceInParam in, Long runtimeId, String splrCd, String payeeBnkaccCdSs, String corpCd) {
		final String sql = getSql("SP0000_11");
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();
		final Object[] args = {
				corpCd, corpCd, splrCd, payeeBnkaccCdSs, splrCd, payeeBnkaccCdSs,
				corporationCode, userCode, ipAddr, now,
				corporationCode, userCode, ipAddr, now,
				runtimeId
		};
		return execSql(sql, args);
	}

	/** 関係先マスタ（親）テーブルをINSERT */
	private int insertRLT_PRT_MST_PARENT (
			MoveActivityInstanceInParam in, Long runtimeId, String splrCd, String corpCd) {
		final String sql = getSql("SP0000_12");
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final Object[] args = {
				corpCd, splrCd,
				corporationCode, userCode, ipAddr, now,
				corporationCode, userCode, ipAddr, now,
				runtimeId
		};
		return execSql(sql, args);
	}

	/** 反社情報（親）テーブルをINSERT */
	private int insertORG_CRM_INF_PARENT (
			MoveActivityInstanceInParam in, Long runtimeId, String splrCd, String corpCd, int brno) {
		final String sql = getSql("SP0000_13");
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final Object[] args = {
				corpCd, splrCd, brno,
				corporationCode, userCode, ipAddr, now,
				corporationCode, userCode, ipAddr, now,
				runtimeId
		};
		return execSql(sql, args);
	}

	/** 関係先マスタ（子）テーブルをINSERT */
	private int insertRLT_PRT_MST_CHILD (
			MoveActivityInstanceInParam in, Long runtimeId, String spLrCd, String corpCd, int sqno) {
		final String sql = getSql("SP0000_14");
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();
		final Object[] args = {
				corpCd, spLrCd, sqno,
				corporationCode, userCode, ipAddr, now,
				corporationCode, userCode, ipAddr, now,
				runtimeId
		};
		return execSql(sql, args);
	}

	/** 反社情報（子）テーブルをINSERT */
	private int insertORG_CRM_INF_CHILD (
			MoveActivityInstanceInParam in, Long runtimeId, String spLrCd, String corpCd, int sqno, int brno) {
		final String sql = getSql("SP0000_15");
		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final Object[] args = {
				corpCd, spLrCd, sqno, brno,
				corporationCode, userCode, ipAddr, now,
				corporationCode, userCode, ipAddr, now,
				runtimeId
		};
		return execSql(sql, args);
	}

	/**
	 * 振込先銀行口座コード（SuperStream）の自動採番処理<br/>
	 * @return 会社コード・取引先コード単位にDBのMAX値+1（※4999以下のMAX値、4999を超えたらエラー）
	 */
	@SuppressWarnings("unchecked")
	private String getMaxPayeeBnkaccCdSs(String companyCd, String splrCd) {
		String maxPayeeBnkaccCdSs = PAYEE_BNKACC_CD_SS_MIN;

		String sql = "select nvl(to_char(max(PAYEE_BNKACC_CD_SS) + 1), '0001') as PAYEE_BNKACC_MST "
				+ "from PAYEE_BNKACC_MST where not(regexp_like(PAYEE_BNKACC_CD_SS, '[^0-9]')) "
				+ "and to_number(PAYEE_BNKACC_CD_SS) <= 4999 and COMPANY_CD = ? and SPLR_CD = ? ";

		// JPAの管理するトランザクションに乗せるため、エンティティマネージャからConnectionを取得する
		EntityManager em = get(EntityManager.class);

		Query query = em.createNativeQuery(sql);
		query.setParameter(1, companyCd);
		query.setParameter(2, splrCd);

		List<Object> results = query.getResultList();

		if (results != null && results.size() > 0) {
			maxPayeeBnkaccCdSs = results.get(0).toString();
		}

		if ((Integer.parseInt(maxPayeeBnkaccCdSs)) > PAYEE_BNKACC_CD_SS_MAX) {
			throw new InternalServerErrorException("振込先銀行口座コード（SuperStream）は最大値の上限値を超えました。");
		}

		return StringUtils.leftPad(maxPayeeBnkaccCdSs, 4, "0");

	}

	/**
	 * 取引先コードの自動採番処理<br/>
	 * 自動採番の場合、会社コード単位ではなく、全社の取引先コードでMAX+1で採番する。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getMaxSplrCd() {
		long maxSplrCd = SPLR_CD_MIN_NUMERING;

		String sql = "select nvl(max(to_number(SPLR_CD)) + 1, 200000000) from SPLR_MST where SPLR_CD LIKE '20%' and length(SPLR_CD) = ?";

		// JPAの管理するトランザクションに乗せるため、エンティティマネージャからConnectionを取得する
		EntityManager em = get(EntityManager.class);

		Query query = em.createNativeQuery(sql);
		query.setParameter(1, SPLR_CD_MAX_LENGTH);

		List<Object> results = query.getResultList();
		if (results != null && results.size() > 0) {
			maxSplrCd = ((BigDecimal) results.get(0)).longValue();
		}

		if ((maxSplrCd) > SPLR_CD_MAX_NUMERING) {
			throw new InternalServerErrorException("取引先コードの最大値が上限値を超えました。取引先コード:" + maxSplrCd);
		}

		return String.valueOf(maxSplrCd);

	}

	/**
	 * 入力データのうち、振込先銀行口座コードSSの最大を取得）
	 * @param req
	 * @return 会社ごとのMap形式データ
	 */
	private void getInputedPayeeBnkaccCdSsMap(RuntimeContext ctx, String companyCd) {
		inputedPayeeBnkaccCdSsMap = new TreeMap<String, Set<String>>();

		PartsGrid grid = (PartsGrid) ctx.runtimeMap.get("GRD1000");
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";

			String payeeBnkaccCdSs = ctx.runtimeMap.get(prefix + "TXT1013").getValue().trim();

			// 会社毎にマップ形式をデータを構築
			if (!inputedPayeeBnkaccCdSsMap.containsKey(companyCd)) {
				inputedPayeeBnkaccCdSsMap.put(companyCd, new HashSet<String>());
			}

			if (payeeBnkaccCdSs.length() > 0
					&& StringUtils.isNumeric(payeeBnkaccCdSs)
					&& Integer.parseInt(payeeBnkaccCdSs) <= PAYEE_BNKACC_CD_SS_MAX) {
				inputedPayeeBnkaccCdSsMap.get(companyCd).add(StringUtils.leftPad(payeeBnkaccCdSs, 4, "0"));
			}
		}
	}

}
