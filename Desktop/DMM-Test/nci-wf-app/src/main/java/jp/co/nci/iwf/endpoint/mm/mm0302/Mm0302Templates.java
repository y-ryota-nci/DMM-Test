package jp.co.nci.iwf.endpoint.mm.mm0302;

import java.io.Serializable;
import java.util.Map;

import jp.co.nci.integrated_workflow.model.custom.WfmActionTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignedTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionTemplate;


public class Mm0302Templates implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	public Map<String, WfmActivityTemplate> activityTemplates;
	public Map<String, WfmAssignedTemplate> assignedTemplates;
	public Map<String, WfmActionTemplate> actionTemplates;
	public Map<String, WfmFunctionTemplate> functionTemplates;

}
