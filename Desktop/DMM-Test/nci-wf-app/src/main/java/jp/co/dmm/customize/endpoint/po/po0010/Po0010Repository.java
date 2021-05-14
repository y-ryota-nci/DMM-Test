package jp.co.dmm.customize.endpoint.po.po0010;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import jp.co.dmm.customize.component.DmmCodeBook.PurordSts;
import jp.co.dmm.customize.component.DmmCodeBook.PurordTp;
import jp.co.dmm.customize.jpa.entity.mw.PayApplMst;
import jp.co.dmm.customize.jpa.entity.mw.PayApplMstPK;
import jp.co.nci.integrated_workflow.api.param.input.GetProcessHistoryListInParam;
import jp.co.nci.integrated_workflow.api.reference.query.ActionHistoryCommand;
import jp.co.nci.integrated_workflow.api.reference.query.CommandQuery;
import jp.co.nci.integrated_workflow.api.reference.query.history.UserAndSharerActionHistoryCommand;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmScreenProcessDef;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 発注一覧のリポジトリ
 */
@ApplicationScoped
public class Po0010Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");
	@Inject private SessionHolder sessionHolder;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Po0010SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("PO0010_01").replaceFirst(REPLACE, "count(*)"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);

		return count(sql.toString(), params.toArray());
	}

	/**
	 * ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Po0010SearchRequest req, Po0010SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("PO0010_01").replaceFirst(REPLACE, getSql("PO0010_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Po0010Entity.class, sql, params.toArray());
	}

	private void fillCondition(Po0010SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		params.add(LoginInfo.get().getLocaleCode());

		// 汎用案件と同等の閲覧権限付与する
		GetProcessHistoryListInParam criteria = new GetProcessHistoryListInParam();
		criteria.setMode(GetProcessHistoryListInParam.Mode.USER_INFO_SHARER_HISTORY);
		criteria.setExecuting(true);
		criteria.setWfUserRole(sessionHolder.getWfUserRole());

		// SQL生成
		ActionHistoryCommand command = UserAndSharerActionHistoryCommand.getQueryCommand(criteria, wf);
		command.setSearchType(ActionHistoryCommand.SearchType.PROCESS);
		command.setAll(true);
		CommandQuery query = command.generate();

		int start = sql.indexOf(ActionHistoryCommand.SUBQUERY);
		int end = start + ActionHistoryCommand.SUBQUERY.length();
		sql.replace(start, end, query.getQuery());
		params.addAll(Arrays.asList(query.getQueryParam()));

		// 発注No
		if (isNotEmpty(req.purordNo)) {
			sql.append(" and O.PURORD_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.purordNo));
		}
		// 発注件名
		if (isNotEmpty(req.purordNm)) {
			sql.append(" and O.PURORD_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.purordNm));
		}
		// 契約書No
		if (isNotEmpty(req.cntrctNo)) {
			sql.append(" and O.CNTRCT_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.cntrctNo));
		}
		// 購入依頼No
		if (isNotEmpty(req.purrqstNo)) {
			sql.append(" and O.PURRQST_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.purrqstNo));
		}
		// 取引先CD
		if (isNotEmpty(req.splrCd)) {
			sql.append(" and O.SPLR_CD = ?");
			params.add(req.splrCd);
		}
		// 取引先名称
		if (isNotEmpty(req.splrNmKj)) {
			sql.append(" and O.SPLR_NM_KJ like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmKj));
		}
		// 発注区分
		List<String> purordTpList = new ArrayList<>();
		if (req.purordTpNormal) purordTpList.add(PurordTp.NORMAL);
		if (req.purordTpRoutine) purordTpList.add(PurordTp.ROUTINE);
		if (req.purordTpFocus) purordTpList.add(PurordTp.FOCUS);
		if (req.purordTpExpense) purordTpList.add(PurordTp.EXPENSE);
		if (!purordTpList.isEmpty()) {
			sql.append(" and " + toInListSql("O.PURORD_TP", purordTpList.size()));
			params.addAll(purordTpList);
		}
		// 発注依頼日
		if (isNotEmpty(req.purordRqstDtFrom)) {
			sql.append(" and ? <= O.PURORD_RQST_DT");
			params.add(req.purordRqstDtFrom);
		}
		if (isNotEmpty(req.purordRqstDtTo)) {
			sql.append(" and O.PURORD_RQST_DT <= ?");
			params.add(req.purordRqstDtTo);
		}
		// 納期（納品予定日）
		if (isNotEmpty(req.dlvPlnDtFrom)) {
			sql.append(" and ? <= O.DLV_PLN_DT");
			params.add(req.dlvPlnDtFrom);
		}
		if (isNotEmpty(req.dlvPlnDtTo)) {
			sql.append(" and O.DLV_PLN_DT <= ?");
			params.add(req.dlvPlnDtTo);
		}
		// 検収予定日
		if (isNotEmpty(req.inspCompDtFrom)) {
			sql.append(" and ? <= O.INSP_COMP_DT");
			params.add(req.inspCompDtFrom);
		}
		if (isNotEmpty(req.inspCompDtTo)) {
			sql.append(" and O.INSP_COMP_DT <= ?");
			params.add(req.inspCompDtTo);
		}
		// 発注申請者コード
		if (isNotEmpty(req.sbmtrCd)) {
			sql.append(" and O.SBMTR_CD = ?");
			params.add(req.sbmtrCd);
		}
		// ステータス
		List<String> purordStsList = new ArrayList<>();
		if (req.purordStsPurordFixed) purordStsList.add(PurordSts.PURORD_FIXED);
		if (req.purordStsRcvinspFixed) purordStsList.add(PurordSts.RCVINSP_FIXED);
		if (req.purordStsPayFixed) purordStsList.add(PurordSts.PAY_FIXED);
		if (req.purordStsCancel) purordStsList.add(PurordSts.CANCEL);
		if (!purordStsList.isEmpty()) {
			sql.append(" and " + toInListSql("O.PURORD_STS", purordStsList.size()));
			params.addAll(purordStsList);
		}
		// ソート
		if (paging && isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));

			// ページング
			sql.append(" offset ? rows fetch first ? rows only");
			params.add(toStartPosition(req.pageNo, req.pageSize));
			params.add(req.pageSize);
		}
	}
	/**
	 * 発注ステータスの更新（10:発注済のみ→20:検収済）
	 * @param purordNo
	 * @param corporationCode
	 * @param userCode
	 * @param ipAddr
	 * @return
	 */
	public int updateSts(String purordNo, String corporationCode, String userCode, String ipAddr) {
		final String sql = getSql("PO0010_03");
		final Timestamp now = MiscUtils.timestamp();
		final Object[] args = {
				corporationCode, userCode, ipAddr, now, corporationCode, purordNo
		};
		return execSql(sql, args);
	}

	/**
	 * 画面プロセスID取得
	 * @param req
	 * @return
	 */
	public int getScreenProcessId(String corporationCode) {

		StringBuilder sql = new StringBuilder(getSql("PO0010_04"));
		final List<Object> params = new ArrayList<>();

		// 会社コード
		params.add(corporationCode);
		params.add("201808211400");
		params.add("00001");

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 変更申請_検収件数
	 * @param req
	 * @return
	 */
	public int countRcvinsp(Po0010ValidRequest req) {
		final String sql = getSql("PO0010_05");
		final Object[] args = {
				req.companyCd, req.purordNo
		};

		return count(sql, args);
	}

	/**
	 * 変更申請_発注申請中件数
	 * @param req
	 * @return
	 */
	public int countPurord(Po0010ValidRequest req) {
		final String sql = getSql("PO0010_06");
		final Object[] args = {
				req.companyCd, req.purordNo, req.companyCd, req.purordNo
		};

		return count(sql, args);
	}

	/**
	 * 削除予定処理
	 * @param req
	 * @return
	 */
	public int countTujonomi(Po0010ValidRequest req) {
		final String sql = getSql("PO0010_99");
		final Object[] args = {
				req.companyCd, req.purordNo
		};

		return count(sql, args);
	}

	/** 支払業務マスタを抽出 */
	public PayApplMst getPayApplMst(String companyCd, String payApplCd) {
		PayApplMstPK key = new PayApplMstPK();
		key.setCompanyCd(companyCd);
		key.setPayApplCd(payApplCd);
		return em.find(PayApplMst.class, key);
	}

	/** 画面プロセス定義コードから画面プロセス定義IDを抽出 */
	public long getScreenProcessId(String companyCd, String screenProcessCode) {
		final Object[] params = { companyCd, screenProcessCode };
		final String sql = getSql("PO0010_07");
		MwmScreenProcessDef spd = selectOne(MwmScreenProcessDef.class, sql, params);
		if (spd == null) {
			throw new NotFoundException("画面プロセス定義が見つかりません。画面プロセス定義コード=" + screenProcessCode);
		}
		return spd.getScreenProcessId();
	}
}
