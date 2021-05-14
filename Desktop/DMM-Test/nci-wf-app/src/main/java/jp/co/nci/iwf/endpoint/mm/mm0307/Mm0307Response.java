package jp.co.nci.iwf.endpoint.mm.mm0307;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmAction;
import jp.co.nci.integrated_workflow.model.custom.WfmActionDef;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アクション定義作成レスポンス
 */
public class Mm0307Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<WfmAction> actions;
	public List<OptionItem> actionDefTypes;

	public WfmActionDef actionDef;

}