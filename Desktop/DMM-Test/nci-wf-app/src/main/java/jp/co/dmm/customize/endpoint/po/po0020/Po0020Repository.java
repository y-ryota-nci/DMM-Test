package jp.co.dmm.customize.endpoint.po.po0020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.component.DmmCodeBook.MlAddTp;
import jp.co.nci.integrated_workflow.api.param.input.GetProcessHistoryListInParam;
import jp.co.nci.integrated_workflow.api.reference.query.ActionHistoryCommand;
import jp.co.nci.integrated_workflow.api.reference.query.CommandQuery;
import jp.co.nci.integrated_workflow.api.reference.query.history.UserAndSharerActionHistoryCommand;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jersey.base.BaseRepository;

/**
 * 管理_定期発注マスタ一覧 のリポジトリ
 */
@ApplicationScoped
public class Po0020Repository extends BaseRepository {
	private static final String REPLACE = quotePattern("${REPLACE}");
	@Inject private SessionHolder sessionHolder;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 件数のカウント
	 * @param req
	 * @return
	 */
	public int count(Po0020SearchRequest req) {
		final List<Object> params = new ArrayList<>();
		final StringBuilder sql = new StringBuilder();
		sql.append(getSql("PO0020_01").replaceFirst(REPLACE, "count(*)"));

		fillCondition(req, sql, params, false);


		return count(sql.toString(), params.toArray());
	}

	/**
	 * ページングありで検索
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Po0020SearchRequest req, BasePagingResponse res) {
		final List<Object> params = new ArrayList<>();
		final StringBuilder sql = new StringBuilder();
		sql.append(getSql("PO0020_01").replaceFirst(REPLACE, getSql("PO0020_02")));

		fillCondition(req, sql, params, true);

		List<Po0020Entity> list = select(Po0020Entity.class, sql.toString(), params.toArray());
		list.forEach(e -> {
			em.detach(e);
			e.payStartTime = addSlash(e.payStartTime);
			e.payEndTime = addSlash(e.payEndTime);
		});
		return list;
	}

	private String addSlash(String s) {
		if (isEmpty(s))
			return s;
		return s.substring(0, 4) + "/" + s.substring(4);
	}

	/** 共通検索条件設定 */
	private void fillCondition(Po0020SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {
		// 会社CD
//		params.add(req.companyCd);

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

		// 契約書No
		if (isNotEmpty(req.cntrctNo)) {
			sql.append(" and O.CNTRCT_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.cntrctNo));
		}
		// 発注No
		if (isNotEmpty(req.purordNo)) {
			sql.append(" and O.PURORD_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.purordNo));
		}
		// 定期発注No
		if (isNotEmpty(req.prdPurordNo)) {
			sql.append(" and O.PRD_PURORD_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.prdPurordNo));
		}
		// 取引先CD
		if (isNotEmpty(req.splrNmKj)) {
			sql.append(" and O.SPLR_NM_KJ like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmKj));
		}
		// 支払開始年月
		if (isNotEmpty(req.payStartTime)) {
			sql.append(" and P.PAY_END_TIME >= ?");
			params.add(req.payStartTime.replaceAll("/", ""));
		}
		// 支払終了年月
		if (isNotEmpty(req.payEndTime)) {
			sql.append(" and P.PAY_START_TIME <= ?");
			params.add(req.payEndTime.replaceAll("/", ""));
		}
		// 月次計上区分
		final List<String> mlAddTpList = new ArrayList<>();
		if (req.mlAddTpNot)
			mlAddTpList.add(MlAddTp.ACC_PAYABLE);
		if (req.mlAddTpPre)
			mlAddTpList.add(MlAddTp.PREPAYMENT);
		if (!mlAddTpList.isEmpty()) {
			sql.append(" and " + toInListSql("P.ML_ADD_TP", mlAddTpList.size()));
			params.addAll(mlAddTpList);
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
