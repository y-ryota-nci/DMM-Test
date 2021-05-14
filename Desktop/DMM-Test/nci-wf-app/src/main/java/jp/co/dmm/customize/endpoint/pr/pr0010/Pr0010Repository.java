package jp.co.dmm.customize.endpoint.pr.pr0010;

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
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * 管理_購入依頼のリポジトリ
 */
@ApplicationScoped
public class Pr0010Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");
	@Inject private SessionHolder sessionHolder;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getOptionItems(String corporationCode, String localeCode, boolean isEmpty) {
		String query = getSql("PR0010_01");
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(localeCode);
		List<MwmOptionItem> results = select(MwmOptionItem.class, query, params.toArray());
		List<OptionItem> newItems = new ArrayList<OptionItem>();

		if (isEmpty){
			newItems.add(new OptionItem("", "--"));
		}
		results.stream().forEach(i -> newItems.add(new OptionItem(i.getCode(), i.getLabel())));

		return newItems;
	}

	/**
	 * 件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Pr0010SearchRequest req) {
		StringBuilder sql = new StringBuilder(
				getSql("PR0010_02").replaceFirst(REPLACE, "count(*)"));
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
	public List<?> select(Pr0010SearchRequest req, Pr0010SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(
				getSql("PR0010_02").replaceFirst(REPLACE, getSql("PR0010_03")));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		return select(Pr0010Entity.class, sql, params.toArray());
	}

	private void fillCondition(Pr0010SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
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

		// 購入依頼No
		if (isNotEmpty(req.purrqstNo)) {
			sql.append(" and P.PURRQST_NO like ? escape '~'");
			params.add(escapeLikeFront(req.purrqstNo));
		}
		// 調達部門区分
		if (isNotEmpty(req.prcFldTp)) {
			sql.append(" and P.PRC_FLD_TP = ?");
			params.add(req.prcFldTp);
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

}
