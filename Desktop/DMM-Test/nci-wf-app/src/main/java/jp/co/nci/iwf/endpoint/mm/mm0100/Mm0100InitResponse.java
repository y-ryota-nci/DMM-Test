package jp.co.nci.iwf.endpoint.mm.mm0100;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 情報共有設定の初期化レスポンス
 */
public class Mm0100InitResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmProcessDef processDef;
	public List<WfmAssignRole> assignRoles;
	public List<WfmExpressionDef> expressionDefs;
	public List<OptionItem> expressionInfoSharerTypes;
	public List<OptionItem> informationSharerTypes;

}