package jp.co.nci.iwf.endpoint.mm.mm0305;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignedDef;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 参加者定義作成レスポンス
 */
public class Mm0305Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<WfmAssignRole> assignRoles;
	public List<WfmExpressionDef> expressionDefs;

	public WfmAssignedDef assignedDef;

}