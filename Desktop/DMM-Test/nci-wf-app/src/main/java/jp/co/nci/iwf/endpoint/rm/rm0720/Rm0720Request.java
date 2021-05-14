package jp.co.nci.iwf.endpoint.rm.rm0720;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 利用者ロール設定リクエスト
 */
public class Rm0720Request extends BasePagingRequest {
	public String corporationCode;
	public String menuRoleCode;

	public List<WfmMenuRoleDetail> deleteMenuRoleDetails;
}
