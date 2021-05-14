package jp.co.nci.iwf.endpoint.wm.wm0210;

import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 参加者ロールの登録リクエスト
 */
public class Wm0210InsertRequest extends BaseRequest {
	public WfmAssignRole assignRole;
}
