package jp.co.nci.iwf.endpoint.wm.wm0330;

import jp.co.nci.integrated_workflow.model.custom.WfmChangeRoleDetail;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 参加者変更ロール構成登録リクエスト
 */
public class Wm0330InsertRequest extends BaseRequest {
	public WfmChangeRoleDetail changeRoleDetail;
}
