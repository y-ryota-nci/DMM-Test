package jp.co.nci.iwf.endpoint.rm.rm0010;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 利用者ロールの登録リクエスト
 */
public class Rm0010InsertRequest extends BaseRequest {
	public WfmMenuRole menuRole;
}
