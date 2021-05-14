package jp.co.nci.iwf.endpoint.wm.wm0320;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmChangeRoleDetail;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 参加者変更ロール設定リクエスト
 */
public class Wm0320Request extends BasePagingRequest {
	public String corporationCode;
	public String changeRoleCode;

	public List<WfmChangeRoleDetail> deleteChangeRoleDetails;
}
