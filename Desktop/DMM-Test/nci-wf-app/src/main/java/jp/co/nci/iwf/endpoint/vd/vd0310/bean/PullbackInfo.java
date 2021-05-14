package jp.co.nci.iwf.endpoint.vd.vd0310.bean;

import java.io.Serializable;

import jp.co.nci.integrated_workflow.model.base.WfmActivityDef;

public class PullbackInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	public PullbackInfo() {}

	public PullbackInfo(WfmActivityDef activityDef) {
		this.corporationCode = activityDef.getCorporationCode();
		this.processDefCode = activityDef.getProcessDefCode();
		this.processDefDetailCode = activityDef.getProcessDefDetailCode();
		this.activityDefCode = activityDef.getActivityDefCode();
		this.activityDefName = activityDef.getActivityDefName();
	}

	/** 会社コード */
	public String corporationCode;
	/** プロセス定義コード */
	public String processDefCode;
	/** プロセス定義コード枝番 */
	public String processDefDetailCode;
	/** アクティビティ定義コード */
	public String activityDefCode;
	/** アクティビティ定義名称 */
	public String activityDefName;

}
