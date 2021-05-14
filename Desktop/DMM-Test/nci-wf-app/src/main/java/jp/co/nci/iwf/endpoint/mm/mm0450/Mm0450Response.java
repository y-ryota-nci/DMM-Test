package jp.co.nci.iwf.endpoint.mm.mm0450;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmChangeDef;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 参加者定義作成レスポンス
 */
public class Mm0450Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<WfmChangeRole> changeRoles;
	public List<WfmExpressionDef> expressionDefs;

	public WfmChangeDef changeDef;

}