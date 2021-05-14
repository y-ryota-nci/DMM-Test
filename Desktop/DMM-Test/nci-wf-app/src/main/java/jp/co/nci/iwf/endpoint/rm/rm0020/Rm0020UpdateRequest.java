package jp.co.nci.iwf.endpoint.rm.rm0020;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 利用者ロール設定の更新リクエスト
 */
public class Rm0020UpdateRequest extends BaseRequest {
	public WfmMenuRole menuRole;
}
