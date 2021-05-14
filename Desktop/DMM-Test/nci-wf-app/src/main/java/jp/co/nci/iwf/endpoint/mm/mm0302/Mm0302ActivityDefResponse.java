package jp.co.nci.iwf.endpoint.mm.mm0302;

import java.util.Map;

import jp.co.nci.integrated_workflow.model.custom.WfmActionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignedDef;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeDef;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionDef;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アクティビティ定義レスポンス
 */
public class Mm0302ActivityDefResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public Map<String, WfmActivityDef> activityDefs;
	public Map<String, WfmAssignedDef> assignedDefs;
	public Map<String, WfmActionDef> actionDefs;
	public Map<String, WfmConditionDef> conditionDefs;
	public Map<String, WfmFunctionDef> functionDefs;
	public Map<String, WfmChangeDef> changeDefs;

}