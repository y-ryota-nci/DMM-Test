package jp.co.nci.iwf.endpoint.rm.rm0902;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ASP管理者登録リクエスト
 */
public class Rm0902Request extends BasePagingRequest {
	public String corporationCode;
	public String menuRoleCode;

	public List<WfmMenuRoleDetail> deleteMenuRoleDetails;
}
