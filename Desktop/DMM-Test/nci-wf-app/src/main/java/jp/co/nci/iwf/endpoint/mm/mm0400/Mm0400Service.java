package jp.co.nci.iwf.endpoint.mm.mm0400;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationGroupInParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmCorporationGroupOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.endpoint.mm.mm0410.Mm0410Response;
import jp.co.nci.iwf.jersey.base.BasePagingService;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 企業グループマスタ一覧画面のサービス
 */
@BizLogic
public class Mm0400Service extends BasePagingService {
	@Inject private WfmLookupService lookup;
	@Inject private WfInstanceWrapper wf;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0410Response init(BaseRequest req) {
		Mm0410Response res = createResponse(Mm0410Response.class, req);
		res.success = true;
		// 削除区分
		res.deleteFlags = lookup.getOptionItems(true, LookupTypeCode.DELETE_FLAG);
		return res;
	}

	/**
	 * 検索
	 * @param req
	 * @return
	 */
	public Mm0400Response search(Mm0400Request req) {
		final SearchWfmCorporationGroupInParam in = new SearchWfmCorporationGroupInParam();
		in.setLoginCorporationCode(sessionHolder.getLoginInfo().getCorporationCode());
		in.setCorporationGroupCode(req.corporationGroupCode);
		in.setCorporationGroupName(req.corporationGroupName);
		in.setDeleteFlag(req.deleteFlag);
		in.setSearchMode(SearchMode.SEARCH_MODE_LIST);
		in.setOrderBy(toOrderBy(req, ""));
		in.setPageSize(req.pageSize);
		in.setPageNo(req.pageNo);

		final SearchWfmCorporationGroupOutParam out = wf.searchWfmCorporationGroup(in);

		final Mm0400Response res = createResponse(Mm0400Response.class, req, out.getCount());
		res.results = out.getCorporationGroupList();
		res.success = true;
		return res;
	}

}
