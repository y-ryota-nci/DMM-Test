package jp.co.dmm.customize.endpoint.py.py0060;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.nci.integrated_workflow.api.param.input.GetProcessHistoryListInParam;
import jp.co.nci.integrated_workflow.api.reference.query.ActionHistoryCommand;
import jp.co.nci.integrated_workflow.api.reference.query.CommandQuery;
import jp.co.nci.integrated_workflow.api.reference.query.history.UserAndSharerActionHistoryCommand;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 前払一覧のリポジトリ
 */
@ApplicationScoped
public class Py0060Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");
	@Inject private SessionHolder sessionHolder;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Py0060SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("PY0060_01").replaceFirst(REPLACE, "count(*)"));
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
	public List<?> select(Py0060SearchRequest req, Py0060SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("PY0060_01").replaceFirst(REPLACE, getSql("PY0060_02")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Py0060Entity.class, sql, params.toArray());
	}

	private void fillCondition(Py0060SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
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

		// 支払No
		if (isNotEmpty(req.payNo)) {
			sql.append(" and P.PAY_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.payNo));
		}
		// 支払件名
		if (isNotEmpty(req.payNm)) {
			sql.append(" and P.PAY_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.payNm));
		}
		// 取引先
		if (isNotEmpty(req.splrNmKj)) {
			sql.append(" and P.SPLR_NM_KJ like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmKj));
		}
		// 支払予定日
		if (isNotEmpty(req.payPlnDtFrom)) {
			sql.append(" and ? <= P.PAY_PLN_DT");
			params.add(req.payPlnDtFrom);
		}
		if (isNotEmpty(req.payPlnDtTo)) {
			sql.append(" and P.PAY_PLN_DT <= ?");
			params.add(req.payPlnDtTo);
		}
//		// ステータス
//		List<String> payStsList = new ArrayList<>();
//		if (req.payStsPayReq) payStsList.add(PaySts.PAY_REQ);
//		if (req.payStsPartialPay) payStsList.add(PaySts.PARTIAL_PAY);
//		if (req.payStsPayFixed) payStsList.add(PaySts.PAY_FIXED);
//		if (!payStsList.isEmpty()) {
//			sql.append(" and " + toInListSql("P.PAY_STS", payStsList.size()));
//			params.addAll(payStsList);
//		}
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
	 * 変更申請_発注申請中件数
	 * @param req
	 * @return
	 */
	public int countPay(Py0060ValidRequest req) {
		final String sql = getSql("PY0060_03");
		final Object[] args = {req.companyCd, req.payNo};
		return count(sql, args);
	}

	/**
	 * 画面プロセスID取得
	 * @param req
	 * @return
	 */
	public int getScreenProcessId(String companyCd) {
		final String screenCode = "SCR0076";
		StringBuilder sql = new StringBuilder(getSql("PY0060_04"));
		final List<Object> params = new ArrayList<>();

		// 会社コード
		params.add(companyCd);
		params.add(screenCode);

		return count(sql.toString(), params.toArray());
	}

}
