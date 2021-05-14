package jp.co.nci.iwf.endpoint.vd.vd0310.bean;

import java.io.Serializable;

import jp.co.nci.integrated_workflow.model.view.WfvActionDef;

public class SendbackInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	public SendbackInfo() {}
	public SendbackInfo(WfvActionDef actionDef) {
		this.seqNoConditionDef = actionDef.getSeqNoConditionDef();
		this.activityDefNameTransit = actionDef.getActivityDefNameTransit();
	}

	public Long seqNoConditionDef;
	public String activityDefNameTransit;

}
