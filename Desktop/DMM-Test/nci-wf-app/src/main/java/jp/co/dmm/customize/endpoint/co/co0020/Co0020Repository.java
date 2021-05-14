package jp.co.dmm.customize.endpoint.co.co0020;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import jp.co.dmm.customize.endpoint.co.RtnPayMstEntity;
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
 * 経常支払マスタ一覧のリポジトリ
 */
@ApplicationScoped
public class Co0020Repository extends BaseRepository {
	@Inject private SessionHolder sessionHolder;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 選択肢取得
	 * @param corporationCode 会社コード
	 * @param optionCode オプションコード
	 * @return 選択肢リスト
	 */
	public List<OptionItem> getSelectItems(String corporationCode, String optionCode) {
		String query = "select B.* from MWM_OPTION A, MWM_OPTION_ITEM B where A.OPTION_ID = B.OPTION_ID and A.CORPORATION_CODE = ? and A.OPTION_CODE = ? and A.DELETE_FLAG = '0'";
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
	 * 経常支払マスタ一覧件数の抽出
	 * @param req
	 * @return
	 */
	public int count(Co0020SearchRequest req) {
		StringBuilder sql = new StringBuilder("select count(*) from (" + getSql("CO0020_02"));
		List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, false);
		sql.append(")");

		return count(sql.toString(), params.toArray());
	}

	/**
	 * 経常支払マスタ一覧ページングありで抽出
	 * @param req
	 * @param res
	 * @return
	 */
	public List<?> select(Co0020SearchRequest req, Co0020SearchResponse res) {
		if (res.allCount == 0) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder("select * from (" + getSql("CO0020_02"));
		final List<Object> params = new ArrayList<>();

		fillCondition(req, sql, params, true);

		// ページング
		sql.append(") offset ? rows fetch first ? rows only");
		params.add(toStartPosition(req.pageNo, req.pageSize));
		params.add(req.pageSize);

		return select(RtnPayMstEntity.class, sql, params.toArray());
	}

	/**
	 * 経常支払マスタ一覧検索条件設定
	 * @param req リクエスト
	 * @param sql SQL
	 * @param params SQLパラメータ
	 * @param paging ページング有無
	 */
	private void fillCondition(Co0020SearchRequest req, StringBuilder sql, List<Object> params, boolean paging) {

		params.add(LoginInfo.get().getLocaleCode());
		params.add(LoginInfo.get().getLocaleCode());
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

//		// 会社コード
//		params.add(req.companyCd);

		// 支払予約No
		if (isNotEmpty(req.rtnPayNo)) {
			sql.append(" and pm.RTN_PAY_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.rtnPayNo));
		}

		// 部門コード
		if (isNotEmpty(req.bumonCd)) {
			sql.append(" and rpm.BUMON_CD = ? ");
			params.add(req.bumonCd);
		}


		// 契約管理No
		if (isNotEmpty(req.cntrctNo)) {
			sql.append(" and pm.CNTRCT_NO like ? escape '~'");
			params.add(escapeLikeBoth(req.cntrctNo));
		}

		// 契約件名
		if (isNotEmpty(req.cntrctNm)) {
			sql.append(" and c.CNTRCT_NM like ? escape '~'");
			params.add(escapeLikeBoth(req.cntrctNm));
		}

		// 取引先名称
		if (isNotEmpty(req.splrNmKj)) {
			sql.append(" and pm.SPLR_NM_KJ like ? escape '~'");
			params.add(escapeLikeBoth(req.splrNmKj));
		}

		// 支払期間（開始）
		if (isNotEmpty(req.payStartTime)) {
			sql.append(" and to_number(pm.PAY_END_TIME) >= ? ");
			params.add(new BigDecimal(req.payStartTime.replace("/", "")));

		}

		// 支払期間（終了）
		if (isNotEmpty(req.payEndTime)) {
			sql.append(" and to_number(pm.PAY_START_TIME) <= ? ");
			params.add(new BigDecimal(req.payEndTime.replace("/", "")));
		}

		// 支払サイトコード
		if (isNotEmpty(req.paySiteCd)) {
			sql.append(" and pm.PAY_SITE_CD = ? ");
			params.add(req.paySiteCd);
		}

		// 契約主体部署
		if (isNotEmpty(req.cntrctrDptCd)) {
			sql.append(" and c.CNTRCTR_DPT_CD = ? ");
			params.add(req.cntrctrDptCd);
		}

		// 申請日
		if (isNotEmpty(req.sbmDptSDt)) {
			sql.append(" and c.SBMT_DPT_DT >= ? ");
			params.add(req.sbmDptSDt);
		}

		if (isNotEmpty(req.sbmDptEDt)) {
			sql.append(" and c.SBMT_DPT_DT <= ? ");
			params.add(req.sbmDptEDt);
		}


		// 金額
		if (req.payAmtMin != null) {
			sql.append(" and ((pm.MNY_TP = 1 ");
			sql.append(" and pm.RTN_PAY_AMT_JPY >= ? ) ");
			params.add(req.payAmtMin);
			sql.append(" or (pm.MNY_TP = 2 ");
			sql.append(" and pm.RTN_PAY_AMT_FC >= ? )) ");
			params.add(req.payAmtMin);
		}

		if (req.payAmtMax != null) {
			sql.append(" and ((pm.MNY_TP = 1 ");
			sql.append(" and pm.RTN_PAY_AMT_JPY <= ? ) ");
			params.add(req.payAmtMax);
			sql.append(" or (pm.MNY_TP = 2 ");
			sql.append(" and pm.RTN_PAY_AMT_FC <= ? )) ");
			params.add(req.payAmtMax);
		}

		// 申請者
		if (isNotEmpty(req.sbmtrNm)) {
			sql.append(" and wu.USER_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.sbmtrNm));
		}

		// 組織
		if (isNotEmpty(req.orgnzNm)) {
			sql.append(" and wo.ORGANIZATION_NAME like ? escape '~'");
			params.add(escapeLikeBoth(req.orgnzNm));
		}


		// ソート
		if (paging && isNotEmpty(req.sortColumn)) {
			sql.append(toSortSql(req.sortColumn, req.sortAsc));
		}
	}
}
