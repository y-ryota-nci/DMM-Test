package jp.co.dmm.customize.endpoint.co.co0010;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.co.CntrctInfEntity;
import jp.co.nci.integrated_workflow.api.param.input.GetProcessHistoryListInParam;
import jp.co.nci.integrated_workflow.api.reference.query.ActionHistoryCommand;
import jp.co.nci.integrated_workflow.api.reference.query.CommandQuery;
import jp.co.nci.integrated_workflow.api.reference.query.history.UserAndSharerActionHistoryCommand;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.jersey.base.BaseRepository;
import jp.co.nci.iwf.jpa.entity.mw.MwmOptionItem;

/**
 * 契約一覧のリポジトリ
 */
@ApplicationScoped
public class Co0010Repository extends BaseRepository {

	@Inject private SessionHolder sessionHolder;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getSelectItems(String corporationCode, String optionCode) {
		String query = "select B.* from MWM_OPTION A, MWM_OPTION_ITEM B where A.OPTION_ID = B.OPTION_ID and A.CORPORATION_CODE = ? and A.OPTION_CODE = ? and A.DELETE_FLAG = '0' order by B.SORT_ORDER";
		List<Object> params = new ArrayList<>();
		params.add(corporationCode);
		params.add(optionCode);
		List<MwmOptionItem> results = select(MwmOptionItem.class, query, params.toArray());
		List<OptionItem> newItems = new ArrayList<OptionItem>();
		newItems.add(new OptionItem("", "--"));

		for (MwmOptionItem item : results) {
			newItems.add(new OptionItem(item.getCode(), item.getLabel()));
		}

		return newItems;
	}

	/**
	 * 契約一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Co0010SearchRequest req) {
		StringBuilder sql = new StringBuilder("select count(*) from (" + getSql("CO0010_01"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params);
		sql.append(")");

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 契約一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<CntrctInfEntity> select(Co0010SearchRequest req, Co0010SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder("select * from (" + getSql("CO0010_01"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params);

		// ページング
		sql.append(") offset ? rows fetch first ? rows only");
		params.add(toStartPosition(req.pageNo, req.pageSize));
		params.add(req.pageSize);

		return select(CntrctInfEntity.class, sql, params.toArray());
	}


	/**
	 * 契約一覧検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Co0010SearchRequest req, StringBuilder sql, List<Object> params) {

		sql.append(" and exists (select 'x' from (%SUBQUERY%) PS where C.COMPANY_CD = PS.CORPORATION_CODE and C.CNTRCT_NO = PS.APPLICATION_NO) ");

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
			sql.append(" and c.CNTRCT_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.cntrctNo));
		}

		// 契約件名
		if (isNotEmpty(req.cntrctNm)) {
			sql.append(" and c.CNTRCT_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.cntrctNm));
		}

		// 取引先名称
		if (isNotEmpty(req.splrNmKj)) {
			sql.append(" and cs.SPLR_NM_KJ like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmKj));
		}

		// 契約期間
		if (isNotEmpty(req.cntrctPrdSDt)) {
			sql.append(" and c.CNTRCT_PRD_E_DT >= ? ");
			params.add(req.cntrctPrdSDt);
		}

		if (isNotEmpty(req.cntrctPrdEDt)) {
			sql.append(" and c.CNTRCT_PRD_S_DT <= ? ");
			params.add(req.cntrctPrdEDt);
		}

		// 依頼種別
		if (isNotEmpty(req.cntrctshtFrmt)) {
			sql.append(" and c.CNTRCTSHT_FRMT = ? ");
			params.add(req.cntrctshtFrmt);
		}

		// ソート
		if (isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));
		}
	}

	/**
	 * 画面プロセスID取得
	 * @param req
	 * @return
	 */
	public int getScreenProcessId(String corporationCode) {

		//振込先口座マスタ
		StringBuilder sql = new StringBuilder(getSql("CO0020_06"));
		final List<Object> params = new ArrayList<>();

		// 会社コード
		params.add(corporationCode);
		params.add("201807131906");
		params.add("00001");

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 変更申請_取引先申請中件数
	 * @param req
	 * @return
	 */
	public int countCntrct(Co0010GetScreenProcessIdRequest req) {
		final String sql = getSql("CO0010_02");
		final Object[] args = {
				req.companyCd, req.cntrctNo
		};

		return count(sql, args);
	}

}
