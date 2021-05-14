package jp.co.nci.iwf.endpoint.mm.mm0302;

import java.io.Serializable;
import java.util.Map;

import jp.co.nci.integrated_workflow.model.custom.WfmActionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignedDef;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeDef;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.model.custom.WfmVariableDef;


public class Mm0302Contents implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmProcessDef processDef;
	public Map<String, WfmVariableDef> variableDefs;
	public Map<String, WfmExpressionDef> expressionDefs;

	public Map<String, WfmActivityDef> activityDefs;
	public Map<String, WfmAssignedDef> assignedDefs;
	public Map<String, WfmChangeDef> changeDefs;
	public Map<String, WfmActionDef> actionDefs;
	public Map<String, WfmConditionDef> conditionDefs;
	public Map<String, WfmFunctionDef> functionDefs;

}
