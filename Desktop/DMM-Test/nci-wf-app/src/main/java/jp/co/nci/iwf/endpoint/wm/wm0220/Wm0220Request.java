package jp.co.nci.iwf.endpoint.wm.wm0220;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmAssignRoleDetail;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 参加者ロール設定リクエスト
 */
public class Wm0220Request extends BasePagingRequest {
	public String corporationCode;
	public String assignRoleCode;

	public List<WfmAssignRoleDetail> deleteAssignRoleDetails;
}
