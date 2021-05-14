package jp.co.nci.iwf.endpoint.suggestion;

import java.sql.Date;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.DeleteFlag;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.custom.WfmUser;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmUserOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.endpoint.sandbox.SandboxSuggestionResponse;
import jp.co.nci.iwf.jersey.base.BasePagingService;

/**
 * Suggestionサービス
 */
@BizLogic
public class SuggestionService extends BasePagingService {
	@Inject private WfInstanceWrapper wf;

	/**
	 * ユーザ検索
	 * @param req
	 * @return
	 */
	public SandboxSuggestionResponse suggestUser(SuggestionUserRequest req) {
		final Date baseDate = defaults(req.baseDate, today());
		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(req.corporationCode);
		in.setUserAddedInfo(req.userAddedInfo);
		in.setUserName(req.userName);
		in.setValidStartDate(baseDate);
		in.setValidEndDate(baseDate);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setMailAddress(req.mailAddress);
		in.setPageSize(req.pageSize);
		in.setPageNo(req.pageNo);
		in.setSearchMode(SearchMode.SEARCH_MODE_LIST);
		in.setOrderBy(new OrderBy[]{ new OrderBy(true, WfmUser.USER_ADDED_INFO) });
		final SearchWfmUserOutParam out = wf.searchWfmUser(in);

		final SandboxSuggestionResponse res = createResponse(
				SandboxSuggestionResponse.class, req, out.getCount().intValue());
		res.results = out.getUserList();
		res.success = true;
		return res;
	}
}
