package jp.co.nci.iwf.endpoint.mm.mm0004;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.common.CodeMaster.LookupTypeCode;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 【プロファイル管理】ユーザ所属編集画面サービス
 */
@BizLogic
public class Mm0004Service extends BaseService {
	@Inject
	private WfmLookupService lookup;

	public BaseResponse init(BaseRequest req) {
		Mm0004Response res = createResponse(Mm0004Response.class, req);
		res.success = true;

		// 主務兼務の選択肢
		res.jobTypeList = lookup.getOptionItems(false, LookupTypeCode.JOB_TYPE);

		return res;
	}

}
