package jp.co.nci.iwf.endpoint.mm.mm0302;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmActionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionDef;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * アクション定義リクエスト
 */
public class Mm0302ActionDefRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmActionDef actionDef;
	public List<WfmActionDef> actionDefs;
	public List<WfmConditionDef> conditionDefs;
	public List<WfmFunctionDef> functionDefs;

}
