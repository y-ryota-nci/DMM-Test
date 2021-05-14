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
public class CallbackFunctionCreate_SPLR_MST extends BaseCallbackFunction {

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
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		final MoveActivityInstanceInParam in = (MoveActivityInstanceInParam)param;
		final PartsRootContainer root = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);

		// 会社コード
		String companyCd = ctx.runtimeMap.get("TXT0301").getValue();

		// 連携先会社コード情報(TXT0401)が存在する場合は、DGHD管理フロー
		boolean dghdFlg = false;
		if(ctx.runtimeMap.get("TXT0401") != null
				&& ctx.runtimeMap.get("TXT0401").getValue().trim().length() > 0) {
			dghdFlg = true;
		}

		// 取引先情報INSERT
		// INSERT行数
		int count = 0;

		// 取引先コード（同一の取引先の場合は設定あり）
		String splrCd = ctx.runtimeMap.get("TXT0003").getValue();
//		boolean isExist = true;
		List<String> crpIds = new ArrayList<>();

		if (StringUtils.isEmpty(splrCd)) {
			splrCd = getMaxSplrCd();
//			isExist = false;
		}

		if(dghdFlg) {
			// DGHD管理フローの場合は連携対象の会社に対してインサート
			String str = ctx.runtimeMap.get("TXT0401").getValue();
			String[] corpCds = str.split(",", 0);
			for(String corpCd : corpCds) {
				count =+ insertSPLR_MST(in, root.rows.get(0).runtimeId, splrCd, corpCd);
				crpIds.add(corpCd);
			}
		} else {
			// 申請者の所属会社に対してインサート
			count = insertSPLR_MST(in, root.rows.get(0).runtimeId, splrCd, companyCd);
			crpIds.add(companyCd);
		}

		if (count == 0) {
			throw new InternalServerErrorException("取引先情報の実インサート行数が0でした。");
		}

		// すでに存在する取引先の場合は上記以外の会社の取引先を更新
//		if (isExist) {
//			List<String> otherCrpIds = getOtherCrpIds(splrCd, crpIds);
//			for(String corpCd : otherCrpIds) {
//				mergeSPLR_MST(in, root.rows.get(0).runtimeId, corpCd, splrCd);
//			}
//		}

		// 振込先銀行口座マスタINSERT
		// INSERT行数
		count = 0;

		// 取引先口座明細情報を整理
		getInputedPayeeBnkaccCdSsMap(ctx, dghdFlg, companyCd);

		// CNTR011001からPAYEE_BNKACC_MSTへ転写
		PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD1000");	// 口座明細のリピーター

		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";

			String targetCorpCd = ctx.runtimeMap.get(prefix + "MST1013").getValue();
			String bnkCd = ctx.runtimeMap.get(prefix + "TXT1001").getValue();
			String bnkbrcCd = ctx.runtimeMap.get(prefix + "TXT1003").getValue();
			String payeeBnkaccCdSs = ctx.runtimeMap.get(prefix + "TXT1013").getValue().trim();

			targetCorpCd = dghdFlg ? targetCorpCd : companyCd;

			if (isNotEmpty(bnkCd) && isNotEmpty(bnkbrcCd)) {

				if (isEmpty(payeeBnkaccCdSs)) {
					payeeBnkaccCdSs = getMaxPayeeBnkaccCdSs(targetCorpCd, splrCd);
					while (inputedPayeeBnkaccCdSsMap.containsKey(targetCorpCd)
							&& inputedPayeeBnkaccCdSsMap.get(targetCorpCd).contains(payeeBnkaccCdSs)) {
						payeeBnkaccCdSs = StringUtils.leftPad(String.valueOf(Integer.parseInt(payeeBnkaccCdSs) + 1), 4, "0");
					}

					if ((Integer.parseInt(payeeBnkaccCdSs)) > PAYEE_BNKACC_CD_SS_MAX) {
						throw new InternalServerErrorException("振込先銀行口座コード（SuperStream）は最大値の上限値を超えました。");
					}
				}

				//DGHD管理フローの場合は連携対象の会社に対してインサート
				//申請者の所属会社に対してインサート
				count = insertPAYEE_BNKACC_MST(in, row.runtimeId, splrCd, payeeBnkaccCdSs, targetCorpCd);

				if (count == 0) {
					throw new InternalServerErrorException("取引先口座管理情報の実インサート行数が0でした。");
				}
			}
		}

	}

	/** 取引先情報テーブルをINSERT */
	private int insertSPLR_MST(
			MoveActivityInstanceInParam in, Long runtimeId, String spLrCd, String corpCd) {
		final String sql = getSql("SP0000_01");
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

//	@SUPPRESSWARNINGS("UNCHECKED")
//	PRIVATE LIST<STRING> GETOTHERCRPIDS(STRING SPLRCD, LIST<STRING> CORPCDS) {
//		STRING SQL = GETSQL("SP0000_09");
//		SQL += " AND NOT " + TOINLISTSQL("COMPANY_CD", CORPCDS.SIZE());
//
//		ENTITYMANAGER EM = GET(ENTITYMANAGER.CLASS);
//		FINAL QUERY Q = EM.CREATENATIVEQUERY(SQL, SPLRMST.CLASS);
//		INT I=1;
//		Q.SETPARAMETER(I, SPLRCD);
//		FOR (STRING CRPID : CORPCDS) {
//			Q.SETPARAMETER(++I, CRPID);
//		}
//		LIST<SPLRMST> SPLRLIST =  (LIST<SPLRMST>)Q.GETRESULTLIST();
//
//		RETURN SPLRLIST.STREAM().MAP(S -> S.GETID().GETCOMPANYCD()).COLLECT( COLLECTORS.TOLIST());
//	}
//
//	/** 取引先情報テーブルをMERGE */
//	PRIVATE INT MERGESPLR_MST(
//			MOVEACTIVITYINSTANCEINPARAM IN, LONG RUNTIMEID, STRING COMPANYCD, STRING SPLRCD) {
//
//		// 更新分をMERGE
//		FINAL STRING MERGESQL = GETSQL("SP0000_07");
//
//		FINAL STRING CORPORATIONCODE = IN.GETWFUSERROLE().GETCORPORATIONCODE();
//		FINAL STRING USERCODE = IN.GETWFUSERROLE().GETUSERCODE();
//		FINAL STRING IPADDR = IN.GETWFUSERROLE().GETIPADDRESS();
//		FINAL TIMESTAMP NOW = MISCUTILS.TIMESTAMP();
//
//		FINAL OBJECT[] ARGS = {
//				COMPANYCD, SPLRCD, RUNTIMEID, COMPANYCD, SPLRCD,
//				CORPORATIONCODE, USERCODE, IPADDR, NOW,
//				COMPANYCD, SPLRCD, COMPANYCD, SPLRCD,
//				CORPORATIONCODE, USERCODE, IPADDR, NOW,
//				CORPORATIONCODE, USERCODE, IPADDR, NOW
//		};
//		RETURN EXECSQL(MERGESQL, ARGS);
//	}

	/** 振込先銀行口座マスタテーブルをINSERT */
	private int insertPAYEE_BNKACC_MST(MoveActivityInstanceInParam in, Long runtimeId, String splrCd, String payeeBnkaccCdSs, String corpCd) {
		final String sql = getSql("SP0000_02");
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
	private void getInputedPayeeBnkaccCdSsMap(RuntimeContext ctx, boolean dghdFlg, String companyCd) {
		inputedPayeeBnkaccCdSsMap = new TreeMap<String, Set<String>>();

		PartsGrid grid = (PartsGrid) ctx.runtimeMap.get("GRD1000");
		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";

			String targetCorpCd = ctx.runtimeMap.get(prefix + "MST1013").getValue();
			targetCorpCd = dghdFlg ? targetCorpCd : companyCd;

			String payeeBnkaccCdSs = ctx.runtimeMap.get(prefix + "TXT1013").getValue().trim();

			// 会社毎にマップ形式をデータを構築
			if (!inputedPayeeBnkaccCdSsMap.containsKey(targetCorpCd)) {
				inputedPayeeBnkaccCdSsMap.put(targetCorpCd, new HashSet<String>());
			}

			if (payeeBnkaccCdSs.length() > 0 && StringUtils.isNumeric(payeeBnkaccCdSs)
					&& Integer.parseInt(payeeBnkaccCdSs) <= PAYEE_BNKACC_CD_SS_MAX) {
				inputedPayeeBnkaccCdSsMap.get(targetCorpCd).add(StringUtils.leftPad(payeeBnkaccCdSs, 4, "0"));
			}
		}
	}

//	/**
//	 * IN句に相当するSQLを生成。「column in (?, ?, .., ?)」
//	 * @param column カラム名
//	 * @param count 出力する「?」の数
//	 * @return
//	 */
//	private String toInListSql(String column, int count) {
//		StringBuilder sb = new StringBuilder(32);
//		sb.append(" ").append(column).append(" in (");
//		for (int i = 0; i < count; i++) {
//			sb.append(i == 0 ? "?" : ", ?");
//		}
//		sb.append(") ");
//		return sb.toString();
//	}
}
