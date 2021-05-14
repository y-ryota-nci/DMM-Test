package jp.co.nci.iwf.endpoint.api.request;

import jp.co.nci.integrated_workflow.model.custom.impl.WfUserRoleImpl;
import jp.co.nci.iwf.jersey.base.BaseRequest;

public class ApiBaseRequest extends BaseRequest {

	/** ユーザロール. */
	private WfUserRoleImpl wfUserRole;

	public WfUserRoleImpl getWfUserRole() {
		return wfUserRole;
	}

	public void setWfUserRole(WfUserRoleImpl wfUserRole) {
		this.wfUserRole = wfUserRole;
	}
}
