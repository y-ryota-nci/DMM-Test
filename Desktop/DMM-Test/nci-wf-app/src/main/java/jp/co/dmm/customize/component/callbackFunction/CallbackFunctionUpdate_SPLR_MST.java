package jp.co.dmm.customize.component.callbackFunction;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang.StringUtils;

import jp.co.dmm.customize.jpa.entity.mw.SplrMst;
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
 * 変更取引先申請の承認後に呼び出されるコールバックファンクション。
 * WF新規取引先申請テーブル(CNTR010002、CNTR011001)からDMMの取引先マスタテーブル(SPLR_MST)、振込先銀行口座マスタ(PAYEE_BNKACC_MST)へデータの変更を反映する。
 * 【変更取引先申請のみ対象】
 */
public class CallbackFunctionUpdate_SPLR_MST extends BaseCallbackFunction {

	/** 振込先銀行口座コード（SuperStream）*/
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
		// 取引先コード
		String splrCd = ctx.runtimeMap.get("TXT0003").getValue();

		// INSERT行数
		int count = 0;

		// 連携先会社コード情報(TXT0401)が存在する場合は、DGHD管理フロー
		boolean dghdFlg = false;
		if(ctx.runtimeMap.get("TXT0401") != null
				&& ctx.runtimeMap.get("TXT0401").getValue().trim().length() > 0) {
			dghdFlg = true;
		}

		String[] corpCds = null;
		if(dghdFlg) {
			// DGHD管理フローの場合は連携対象の会社に対して更新
			String str = ctx.runtimeMap.get("TXT0401").getValue();
			corpCds = str.split(",", 0);

			List<String> corpCdList = new ArrayList<>();
			for(String corpCd : corpCds) {
				if (isNotEmpty(corpCd)) {
					corpCdList.add(corpCd);
				}
			}

			if (corpCdList.size() != 0) {
				// INSERT or UPDATE
				for(String corpCd : corpCdList) {
					count += mergeSPLR_MST(in, root.rows.get(0).runtimeId, corpCd, splrCd);
				}

				// 未選択となった会社分を論理削除
				deleteSPLR_MST(splrCd, corpCdList);
			}

		} else {
			// 事業部申請の場合も登録済み会社すべてに更新申請者の所属会社に対して更新（有効なものだけ）
			List<String> crpIds = getCrpIds(splrCd);
			if (!crpIds.contains(companyCd)) {
				crpIds.add(companyCd);
			}

			for(String corpCd : crpIds) {
				count += mergeSPLR_MST(in, root.rows.get(0).runtimeId, corpCd, splrCd);
			}
		}

		// 取引先情報更新フラグ
		if (count == 0) {
			throw new InternalServerErrorException("取引先情報の更新件数が0件でした。");
		}

		// 振込先銀行口座マスタ更新
		count = 0;

		// 取引先口座明細情報を整理
		getInputedPayeeBnkaccCdSsMap(ctx, dghdFlg, companyCd);

		// MWT_SPLR_REQUEST_DETAILからPAYEE_BNKACC_MSTへ転写
		PartsGrid grid = (PartsGrid)ctx.runtimeMap.get("GRD1000");	// 口座明細のリピーター

		for (PartsContainerRow row : grid.rows) {
			String prefix = grid.htmlId + "-" + row.rowId + "_";

			// 銀行コード、支店コード、口座番号が入力されていれば、有効な明細行とみなす
			String targetCorpCd = ctx.runtimeMap.get(prefix + "MST1013").getValue();
			String bnkCd = ctx.runtimeMap.get(prefix + "TXT1001").getValue();
			String bnkBrhCd = ctx.runtimeMap.get(prefix + "TXT1003").getValue();
			String bnkaccNo = ctx.runtimeMap.get(prefix + "TXT1006").getValue();
			String payeeBnkaccCd = ctx.runtimeMap.get(prefix + "TXT1011").getValue();
			String payeeBnkaccCdSs = ctx.runtimeMap.get(prefix + "TXT1013").getValue();

			targetCorpCd = dghdFlg ? targetCorpCd : companyCd;

			if (isNotEmpty(payeeBnkaccCd) ||  (isNotEmpty(bnkCd) && isNotEmpty(bnkBrhCd) && isNotEmpty(bnkaccNo))) {

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

				// 振込先銀行口座コードがない場合はINSERT
				if (isEmpty(payeeBnkaccCd)) {
					count = insertPAYEE_BNKACC_MST(in, row.runtimeId, targetCorpCd, splrCd, payeeBnkaccCdSs);
				// UPDATE
				} else {
					count = updatePAYEE_BNKACC_MST(in, row.runtimeId, targetCorpCd, payeeBnkaccCd, payeeBnkaccCdSs);
				}

				if (count == 0) {
					throw new InternalServerErrorException("取引先口座管理情報の更新件数が0件でした。");
				}
			}
		}
	}

	/** 取引先情報テーブルを論理削除(選択された会社に含まれないもの) */
	private void deleteSPLR_MST(String splrCd, List<String> corpCds) {

		// 削除フラグ更新
		String updateSql = getSql("SP0001_06");
		updateSql += " and not " + toInListSql("COMPANY_CD", corpCds.size());

		List<Object> params = new ArrayList<>();
		params.add(splrCd);
		params.addAll(corpCds);

		execSql(updateSql, params.toArray());
	}

	@SuppressWarnings("unchecked")
	private List<String> getCrpIds(String splrCd) {
		String sql = getSql("SP0000_09");

		EntityManager em = get(EntityManager.class);
		final Query q = em.createNativeQuery(sql, SplrMst.class);
		q.setParameter(1, splrCd);
		List<SplrMst> splrList =  (List<SplrMst>)q.getResultList();

		return splrList.stream().map(s -> s.getId().getCompanyCd()).collect( Collectors.toList());
	}

	/** 取引先情報テーブルをMERGE */
	private int mergeSPLR_MST(
			MoveActivityInstanceInParam in, Long runtimeId, String companyCd, String splrCd) {

		// 更新分をMERGE
		final String mergeSql = getSql("SP0000_07");

		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final Object[] args = {
				companyCd, splrCd, runtimeId, companyCd, splrCd,
				corporationCode, userCode, ipAddr, now,
				companyCd, splrCd, companyCd, splrCd,
				corporationCode, userCode, ipAddr, now,
				corporationCode, userCode, ipAddr, now
		};
		return execSql(mergeSql, args);
	}

	/** 振込先銀行口座マスタテーブルをINSERT */
	private int insertPAYEE_BNKACC_MST(MoveActivityInstanceInParam in, Long runtimeId, String companyCd, String splrCd, String payeeBnkaccCdSS) {

		// INSERT
		final String insertSql = getSql("SP0000_02");

		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		// 作成日取得
		Timestamp targetCreateDate = null;

		if (isNotEmpty(payeeBnkaccCdSS)) {
			final String getCreateDateSql = getSql("SP0020_10");
			EntityManager em = get(EntityManager.class);
			Query q = em.createNativeQuery(getCreateDateSql);
			q.setParameter(1, companyCd);
			q.setParameter(2, splrCd);
			q.setParameter(3, payeeBnkaccCdSS);
			targetCreateDate = (Timestamp)q.getSingleResult();
		}

		if (targetCreateDate == null) {
			targetCreateDate = now;
		}

		final Object[] args = {
				companyCd, companyCd, splrCd, payeeBnkaccCdSS, splrCd, payeeBnkaccCdSS,
				corporationCode, userCode, ipAddr, targetCreateDate,
				corporationCode, userCode, ipAddr, now,
				runtimeId
		};
		return execSql(insertSql, args);
	}

	/** 振込先銀行口座マスタテーブルをUPDATE */
	private int updatePAYEE_BNKACC_MST(MoveActivityInstanceInParam in, Long runtimeId, String companyCd, String payeeBnkaccCd, String payeeBnkaccCdSs) {

		final String updateSql = getSql("SP0000_08");

		final String corporationCode = in.getWfUserRole().getCorporationCode();
		final String userCode = in.getWfUserRole().getUserCode();
		final String ipAddr = in.getWfUserRole().getIpAddress();
		final Timestamp now = MiscUtils.timestamp();

		final Object[] args = {
				payeeBnkaccCdSs,
				corporationCode, userCode, ipAddr, now,
				runtimeId,
				companyCd, payeeBnkaccCd, companyCd, payeeBnkaccCd

		};
		return execSql(updateSql, args);
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

	/**
	 * 振込先銀行口座コード（SuperStream）の自動採番処理<br/>
	 * @return 会社コードごとにDBのMAX値+1（※4999以下のMAX値、4999を超えたらエラー）
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
	 * IN句に相当するSQLを生成。「column in (?, ?, .., ?)」
	 * @param column カラム名
	 * @param count 出力する「?」の数
	 * @return
	 */
	private String toInListSql(String column, int count) {
		StringBuilder sb = new StringBuilder(32);
		sb.append(" ").append(column).append(" in (");
		for (int i = 0; i < count; i++) {
			sb.append(i == 0 ? "?" : ", ?");
		}
		sb.append(") ");
		return sb.toString();
	}
}
