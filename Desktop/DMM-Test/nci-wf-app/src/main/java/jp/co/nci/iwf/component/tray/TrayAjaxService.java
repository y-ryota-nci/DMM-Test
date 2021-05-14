package jp.co.nci.iwf.component.tray;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.param.input.SearchWfmCorporationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmOrganizationInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmPostInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmUserInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * トレイ系画面のAjax用サービス
 */
@BizLogic
public class TrayAjaxService extends BaseService {
	@Inject private WfInstanceWrapper wf;

	/**
	 * ユーザマスタ取得
	 * @param corporationCode
	 * @param userCode
	 * @return
	 */
	public BasePagingResponse getWfmUser(TrayNameRequest req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.userCode))
			return null;

		final SearchWfmUserInParam in = new SearchWfmUserInParam();
		in.setCorporationCode(req.corporationCode);
		in.setUserCode(req.userCode);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);

		final BasePagingResponse res = createResponse(BasePagingResponse.class, req);
		res.results = wf.searchWfmUser(in).getUserList();
		res.success = true;
		return res;
	}

	/**
	 * 組織マスタ取得
	 * @param corporationCode
	 * @param organizationCode
	 * @return
	 */
	public BasePagingResponse getWfmOrganization(TrayNameRequest req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.organizationCode))
			return null;

		final SearchWfmOrganizationInParam in = new SearchWfmOrganizationInParam();
		in.setCorporationCode(req.corporationCode);
		in.setOrganizationCode(req.organizationCode);

		final BasePagingResponse res = createResponse(BasePagingResponse.class, req);
		res.results = wf.searchWfmOrganization(in).getOrganizationList();
		res.success = true;
		return res;
	}

	/**
	 * 役職マスタ取得
	 * @param corporationCode
	 * @param postCode
	 * @return
	 */
	public BasePagingResponse getWfmPost(TrayNameRequest req) {
		if (isEmpty(req.corporationCode) || isEmpty(req.postCode))
			return null;

		final SearchWfmPostInParam in = new SearchWfmPostInParam();
		in.setCorporationCode(req.corporationCode);
		in.setPostCode(req.postCode);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);

		final BasePagingResponse res = createResponse(BasePagingResponse.class, req);
		res.results = wf.searchWfmPost(in).getPostList();
		res.success = true;
		return res;
	}

	/**
	 * 企業マスタ取得
	 * @param corporationCode
	 * @return
	 */
	public BasePagingResponse getWfmCorporation(TrayNameRequest req) {
		if (isEmpty(req.corporationCode))
			return null;

		final SearchWfmCorporationInParam in = new SearchWfmCorporationInParam();
		in.setCorporationCode(req.corporationCode);
		in.setSearchMode(SearchMode.SEARCH_MODE_OBJECT);

		final BasePagingResponse res = createResponse(BasePagingResponse.class, req);
		res.results = wf.searchWfmCorporation(in).getCorporations();
		res.success = true;
		return res;
	}
}
