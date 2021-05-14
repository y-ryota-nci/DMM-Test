package jp.co.nci.iwf.endpoint.mm.mm0302;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmAssignedDef;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 比較条件式定義リクエスト
 */
public class Mm0302ExpressionDefRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmExpressionDef expressionDef;
	public List<WfmConditionDef> conditionDefs;
	public List<WfmAssignedDef> assignedDefs;

}
