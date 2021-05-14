package jp.co.nci.iwf.endpoint.mm.mm0302;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmAssignedDef;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 比較条件式定義レスポンス
 */
public class Mm0302ExpressionDefResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmExpressionDef expressionDef;
	public List<WfmAssignedDef> assignedDefs;
	public List<WfmConditionDef> conditionDefs;
}