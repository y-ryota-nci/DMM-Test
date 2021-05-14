package jp.co.nci.iwf.endpoint.wm.wm0220;

import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 参加者ロール設定の更新リクエスト
 */
public class Wm0220UpdateRequest extends BaseRequest {
	public WfmAssignRole assignRole;
}
