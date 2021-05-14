package jp.co.nci.iwf.endpoint.mm.mm0302;

import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * アクション遷移先定義リクエスト
 */
public class Mm0302ConditionDefRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmConditionDef conditionDef;

}
