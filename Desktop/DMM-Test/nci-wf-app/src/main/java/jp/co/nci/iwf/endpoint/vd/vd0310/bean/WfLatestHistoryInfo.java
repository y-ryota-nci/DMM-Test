package jp.co.nci.iwf.endpoint.vd.vd0310.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import jp.co.nci.integrated_workflow.model.custom.WfLatestHistory;

public class WfLatestHistoryInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfLatestHistoryInfo() {}
	public WfLatestHistoryInfo(WfLatestHistory wlh) {
		this.activityId = wlh.getActivityId();
		this.activityDefCode = wlh.getActivityDefCode();
		this.activityDefName = wlh.getActivityDefName();
		this.activityStatus = wlh.getActivityStatus();
		this.assignedStatus = wlh.getAssignedStatus();
		this.executionDate = wlh.getExecutionDate();
		this.sealNameUser = wlh.getSealNameUserProxyAsd();
		this.sealNameAction = wlh.getSealNameAction();
		this.stampCode = wlh.getStampCodeAssigned();
	}

	public Long activityId;
	public String activityDefCode;
	public String activityDefName;
	public String activityStatus;
	public String assignedStatus;
	public Timestamp executionDate;
	public String sealNameUser;
	public String sealNameAction;
	public String stampCode;

}
