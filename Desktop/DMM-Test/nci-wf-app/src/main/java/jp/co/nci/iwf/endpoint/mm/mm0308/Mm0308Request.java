package jp.co.nci.iwf.endpoint.mm.mm0308;

import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * アクション遷移先定義作成リクエスト
 */
public class Mm0308Request extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmActivityDef activityDef;
	public WfmConditionDef conditionDef;

}
