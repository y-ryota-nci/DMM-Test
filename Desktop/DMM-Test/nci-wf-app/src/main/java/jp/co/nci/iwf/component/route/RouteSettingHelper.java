package jp.co.nci.iwf.component.route;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.model.custom.WfmActionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignedDef;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeDef;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.iwf.util.MiscUtils;

public class RouteSettingHelper extends MiscUtils implements CodeMaster, RouteSettingCodeBook {

	protected RouteSettingHelper() {}

	public static WfmProcessDef createDefaultProcessDef(String corporationCode) {
		WfmProcessDef processDef = new WfmProcessDef();
		processDef.setCorporationCode(corporationCode);
		processDef.setProcessDefDetailCode(ProcessDefDetailCode.DEFAULT);
		processDef.setProcessOpeType(ProcessOpeType.NORMAL);
		processDef.setSkipFlag(SkipFlag.ON);
		processDef.setBatchProcessingFlag(BatchProcessingFlag.OFF);
		processDef.setExecutionTermUnitType(ExecutionTermUnitType.NOT_USE);
		processDef.setCommentDisplayFlag(CommentDisplayFlag.ON);
		processDef.setSortOrder(1L);
		processDef.setValidStartDate(now());
		processDef.setValidEndDate(ENDDATE);

		return processDef;
	}

	public static WfmExpressionDef createDefaultExpressionDef(WfmExpressionDef src) {
		WfmExpressionDef expressionDef = new WfmExpressionDef();
		copyProperties(src, expressionDef);

		expressionDef.setExpressionLeftType(ExpressionLeftType.VARIABLE);
		expressionDef.setOperatorType(OperatorType.EQUAL);
		expressionDef.setExpressionRightType(ExpressionRightType.INPUT_VALUE);

		return expressionDef;
	}

	public static WfmAssignedDef createDefaultAssignedDef(WfmAssignedDef src) {
		WfmAssignedDef assignedDef = new WfmAssignedDef();
		copyProperties(src, assignedDef);

		assignedDef.setSortOrder(1L);
		assignedDef.setValidStartDate(now());
		assignedDef.setValidEndDate(ENDDATE);

		return assignedDef;

	}

	public static WfmChangeDef createDefaultChangeDef(WfmChangeDef src) {
		WfmChangeDef changeDef = new WfmChangeDef();
		copyProperties(src, changeDef);

		changeDef.setSortOrder(1L);
		changeDef.setValidStartDate(now());
		changeDef.setValidEndDate(ENDDATE);

		return changeDef;
	}

	public static WfmActionDef createDefaultActionDef(WfmActionDef src) {
		WfmActionDef actionDef = new WfmActionDef();
		copyProperties(src, actionDef);
		actionDef.setActionCode(ActionCode.DEFAULT);
		actionDef.setTransitFlag(TransitFlag.ON);
		actionDef.setDefaultFlag(DefaultFlag.OFF);
		actionDef.setFunctionSkipFlag(FunctionSkipFlag.OFF);
		actionDef.setSortOrder(new Long(1));
		return actionDef;
	}

	public static WfmConditionDef createDefaultConditionDef(WfmConditionDef src) {
		WfmConditionDef conditionDef = new WfmConditionDef();
		copyProperties(src, conditionDef);

		conditionDef.setBusinessProcessStatus("0");
		conditionDef.setBusinessActivityStatusPre("0");
		conditionDef.setBusinessActivityStatus("0");
		conditionDef.setDefaultFlag(DefaultFlag.ON);
		conditionDef.setDisplayFlag(DisplayFlag.ON);
		conditionDef.setTransitCorporationType(TransitCorporationType.DESIGNATED);

		return conditionDef;
	}

	public static WfmFunctionDef createDefaultFunctionDef(WfmFunctionDef src) {
		WfmFunctionDef functionDef = new WfmFunctionDef();
		copyProperties(src, functionDef);
		functionDef.setExecutionTimingType(ExecutionTimingType.POST);
		functionDef.setFunctionSkipFlag(FunctionSkipFlag.OFF);

		return functionDef;
	}
}
