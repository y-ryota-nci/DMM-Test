package jp.co.nci.iwf.endpoint.wm.wm0320;

import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 参加者変更ロール設定の更新リクエスト
 */
public class Wm0320UpdateRequest extends BaseRequest {
	public WfmChangeRole changeRole;
}
