package jp.co.nci.iwf.endpoint.wm.wm0310;

import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 参加者変更ロールの登録リクエスト
 */
public class Wm0310InsertRequest extends BaseRequest {
	public WfmChangeRole changeRole;
}
