package jp.co.dmm.customize.endpoint.ri.ri0030;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.component.DmmCodeBook.RcvinspSts;
import jp.co.nci.integrated_workflow.api.param.input.GetProcessHistoryListInParam;
import jp.co.nci.integrated_workflow.api.reference.query.ActionHistoryCommand;
import jp.co.nci.integrated_workflow.api.reference.query.CommandQuery;
import jp.co.nci.integrated_workflow.api.reference.query.history.UserAndSharerActionHistoryCommand;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 検収一覧のリポジトリ
 */
@ApplicationScoped
public class Ri0030Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");
	@Inject private SessionHolder sessionHolder;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Ri0030SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("RI0030_01").replaceFirst(REPLACE, "count(*)"));
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
	public List<?> select(Ri0030SearchRequest req, Ri0030SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("RI0030_01").replaceFirst(REPLACE, getSql("RI0030_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Ri0030Entity.class, sql, params.toArray());
	}

	private void fillCondition(Ri0030SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
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

		// 検収No
		if (isNotEmpty(req.rcvinspNo)) {
			sql.append(" and R.RCVINSP_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.rcvinspNo));
		}
		// 取引先CD
		if (isNotEmpty(req.splrCd)) {
			sql.append(" and R.SPLR_CD = ?");
			params.add(req.splrCd);
		}
		// 取引先名称
		if (isNotEmpty(req.splrNmKj)) {
			sql.append(" and R.SPLR_NM_KJ like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmKj));
		}
		// 納品日
		if (isNotEmpty(req.dlvDtFrom)) {
			sql.append(" and ? <= R.DLV_DT");
			params.add(req.dlvDtFrom);
		}
		if (isNotEmpty(req.dlvDtTo)) {
			sql.append(" and R.DLV_DT <= ?");
			params.add(req.dlvDtTo);
		}
		// 検収日
		if (isNotEmpty(req.rcvinspDtFrom)) {
			sql.append(" and ? <= R.RCVINSP_DT");
			params.add(req.rcvinspDtFrom);
		}
		if (isNotEmpty(req.rcvinspDtTo)) {
			sql.append(" and R.RCVINSP_DT <= ?");
			params.add(req.rcvinspDtTo);
		}
		// ステータス
		List<String> rcvinspStsList = new ArrayList<>();
		if (req.rcvinspStsRcvinspFixed) rcvinspStsList.add(RcvinspSts.RCVINSP_FIXED);
		if (req.rcvinspStsPayFixed) rcvinspStsList.add(RcvinspSts.PAY_FIXED);
		if (req.rcvinspStsCancel) rcvinspStsList.add(RcvinspSts.CANCEL);
		if (!rcvinspStsList.isEmpty()) {
			sql.append(" and " + toInListSql("R.RCVINSP_STS", rcvinspStsList.size()));
			params.addAll(rcvinspStsList);
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

	/** 支払申請のカウント */
	public int countPay(Ri0030ValidateRequest req) {
		final Object[] params = { req.companyCd, req.rcvinspNo };
		final String sql = getSql("RI0030_03");
		return count(sql, params);
	}

	/** 申請中の変更申請のカウント */
	public int countChangeRcvinsp(Ri0030ValidateRequest req) {
		final Object[] params = { req.companyCd, req.rcvinspNo };
		final String sql = getSql("RI0030_04");
		return count(sql, params);
	}

	/** 前払ありの検収か */
	public int countPrePay(Ri0030ValidateRequest req) {
		final Object[] params = { req.companyCd, req.rcvinspNo };
		final String sql = getSql("RI0030_05");
		return count(sql, params);
	}
}
