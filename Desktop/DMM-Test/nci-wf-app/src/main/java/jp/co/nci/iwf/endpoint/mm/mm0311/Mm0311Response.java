package jp.co.nci.iwf.endpoint.mm.mm0311;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmVariableDef;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 比較条件式定義作成レスポンス
 */
public class Mm0311Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<OptionItem> operatorTypes;
	public List<WfmVariableDef> variableDefs;
	public List<WfmExpressionDef> expressionDefs;

	public WfmExpressionDef expressionDef;

}