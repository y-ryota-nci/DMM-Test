package jp.co.nci.iwf.endpoint.wm.wm0230;

import jp.co.nci.integrated_workflow.model.custom.WfmAssignRoleDetail;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 参加者ロール構成登録リクエスト
 */
public class Wm0230InsertRequest extends BaseRequest {
	public WfmAssignRoleDetail assignRoleDetail;
}
