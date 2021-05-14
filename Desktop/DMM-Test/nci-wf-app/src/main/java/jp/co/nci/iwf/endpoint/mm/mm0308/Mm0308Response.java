package jp.co.nci.iwf.endpoint.mm.mm0308;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アクション遷移先定義作成レスポンス
 */
public class Mm0308Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<OptionItem> businessProcessStatuses;
	public List<OptionItem> businessActivityStatuses;

	public List<WfmActivityDef> activityDefs;
	public List<WfmExpressionDef> expressionDefs;

	public WfmConditionDef conditionDef;

}