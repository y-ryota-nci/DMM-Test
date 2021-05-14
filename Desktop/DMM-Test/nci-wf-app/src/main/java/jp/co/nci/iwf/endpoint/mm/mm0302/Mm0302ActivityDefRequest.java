package jp.co.nci.iwf.endpoint.mm.mm0302;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmActionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActionTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignedDef;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignedTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeDef;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionTemplate;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * アクティビティ定義リクエスト
 */
public class Mm0302ActivityDefRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmActivityDef activityDef;

	public List<WfmActivityDef> activityDefs;
	public List<WfmAssignedDef> assignedDefs;
	public List<WfmActionDef> actionDefs;
	public List<WfmConditionDef> conditionDefs;
	public List<WfmFunctionDef> functionDefs;
	public List<WfmChangeDef> changeDefs;

	public List<WfmActivityTemplate> activityTemplates;
	public List<WfmAssignedTemplate> assignedTemplates;
	public List<WfmActionTemplate> actionTemplates;
	public List<WfmFunctionTemplate> functionTemplates;

}
