package jp.co.nci.iwf.endpoint.rm.rm0902;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ASP管理者登録の更新リクエスト
 */
public class Rm0902UpdateRequest extends BaseRequest {
	public WfmMenuRole menuRole;
}
