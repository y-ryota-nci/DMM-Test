package jp.co.nci.iwf.endpoint.mm.mm0302;

import java.util.List;
import java.util.Map;

import jp.co.nci.integrated_workflow.model.custom.WfmActionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionDef;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アクション定義レスポンス
 */
public class Mm0302ActionDefResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmActionDef actionDef;
	public List<WfmActionDef> actionDefs;
	public Map<String, WfmConditionDef> conditionDefs;
	public Map<String, WfmFunctionDef> functionDefs;

}