package jp.co.nci.iwf.endpoint.vd.vd0310.bean;

import java.io.Serializable;

import jp.co.nci.integrated_workflow.model.base.WfmAction;
import jp.co.nci.integrated_workflow.model.view.WfvActionDef;
import jp.co.nci.iwf.util.MiscUtils;

public class ActionInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	public ActionInfo() {}

	public ActionInfo(WfvActionDef actionDef) {
		this.seqNoActionDef = actionDef.getSeqNoActionDef();
		this.actionCode = actionDef.getActionCode();
		this.actionName = MiscUtils.isEmpty(actionDef.getActionDefName()) ? actionDef.getActionName() : actionDef.getActionDefName();
		this.actionType = actionDef.getActionType();
		this.defaultFlag = actionDef.getDefaultFlag();
		this.localeCode = actionDef.getLocaleCode();
	}

	public ActionInfo(WfmAction action, String localeCode) {
		this.seqNoActionDef = null;
		this.actionCode = action.getActionCode();
		this.actionName = action.getActionName();
		this.actionType = action.getActionType();
		this.localeCode = localeCode;
	}

	public Long seqNoActionDef;
	public String actionCode;
	public String actionName;
	public String actionType;
	public String defaultFlag;
	public String localeCode;
	public boolean isDummy = false;
}
