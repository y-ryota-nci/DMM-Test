package jp.co.nci.iwf.component.route.upload;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.integrated_workflow.model.custom.WfmAction;
import jp.co.nci.integrated_workflow.model.custom.WfmActionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRoleDetail;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignedDef;
import jp.co.nci.integrated_workflow.model.custom.WfmAuthTransfer;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeDef;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRoleDetail;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunction;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.model.custom.WfmVariableDef;
import jp.co.nci.integrated_workflow.model.custom.WfmWfRelationAuthDefEx;
import jp.co.nci.integrated_workflow.model.custom.WfmWfRelationDefEx;
import jp.co.nci.iwf.component.download.BaseIdReplacer;

/**
 * プロセス定義のアップロードのID置換ロジック
 */
@ApplicationScoped
public class ProcessDefIdReplacer extends BaseIdReplacer {

	/**
	 * 初期化
	 */
	@PostConstruct
	public void init() {
		// クラスが示すテーブルの、プライマリキーのカラム名をMap化
		map.put(WfmAction.class, L(CORPORATION_CODE, WfmAction.ACTION_CODE));
		map.put(WfmActionDef.class, L(
				CORPORATION_CODE, PROCESS_DEF_CODE, PROCESS_DEF_DETAIL_CODE
				, ACTIVITY_DEF_CODE, SEQ_NO_ACTION_DEF));
		map.put(WfmActivityDef.class, L(
				CORPORATION_CODE, PROCESS_DEF_CODE, PROCESS_DEF_DETAIL_CODE
				, ACTIVITY_DEF_CODE));
		map.put(WfmAssignedDef.class, L(
				CORPORATION_CODE, PROCESS_DEF_CODE, PROCESS_DEF_DETAIL_CODE
				, ACTIVITY_DEF_CODE, SEQ_NO_ASSIGNED_DEF));
		map.put(WfmAssignRole.class, L(CORPORATION_CODE, ASSIGN_ROLE_CODE));
		map.put(WfmAssignRoleDetail.class, L(
				CORPORATION_CODE, ASSIGN_ROLE_CODE, SEQ_NO_ASSIGN_ROLE_DETAIL));
		map.put(WfmAuthTransfer.class, L(CORPORATION_CODE, SEQ_NO_AUTH_TRANSFER));
		map.put(WfmChangeDef.class, L(
				CORPORATION_CODE, PROCESS_DEF_CODE, PROCESS_DEF_DETAIL_CODE
				, ACTIVITY_DEF_CODE, SEQ_NO_CHANGE_DEF));
		map.put(WfmChangeRole.class, L(CORPORATION_CODE, CHANGE_ROLE_CODE));
		map.put(WfmChangeRoleDetail.class, L(
				CORPORATION_CODE, CHANGE_ROLE_CODE, SEQ_NO_CHANGE_ROLE_DETAIL));
		map.put(WfmConditionDef.class, L(
				CORPORATION_CODE, PROCESS_DEF_CODE, PROCESS_DEF_DETAIL_CODE
				, ACTIVITY_DEF_CODE, SEQ_NO_ACTION_DEF, SEQ_NO_CONDITION_DEF));
		map.put(WfmExpressionDef.class, L(
				CORPORATION_CODE, PROCESS_DEF_CODE, PROCESS_DEF_DETAIL_CODE
				, EXPRESSION_DEF_CODE));
		map.put(WfmFunction.class, L(CORPORATION_CODE, FUNCTION_CODE));
		map.put(WfmFunctionDef.class, L(
				CORPORATION_CODE, PROCESS_DEF_CODE, PROCESS_DEF_DETAIL_CODE
				, ACTIVITY_DEF_CODE, SEQ_NO_ACTION_DEF, SEQ_NO_FUNCTION_DEF));
		map.put(WfmInformationSharerDef.class, L(
				CORPORATION_CODE, PROCESS_DEF_CODE, PROCESS_DEF_DETAIL_CODE
				, SEQ_NO_INFO_SHARER_DEF));
		map.put(WfmProcessDef.class, L(
				CORPORATION_CODE, PROCESS_DEF_CODE, PROCESS_DEF_DETAIL_CODE));
		map.put(WfmVariableDef.class, L(
				CORPORATION_CODE, PROCESS_DEF_CODE, PROCESS_DEF_DETAIL_CODE
				, VARIABLE_DEF_CODE));
		map.put(WfmWfRelationDefEx.class, L(CORPORATION_CODE, PROCESS_DEF_CODE
				, PROCESS_DEF_DETAIL_CODE, SEQ_NO_WF_RELATION_DEF));
		map.put(WfmWfRelationAuthDefEx.class, L(CORPORATION_CODE, PROCESS_DEF_CODE
				, PROCESS_DEF_DETAIL_CODE, SEQ_NO_WF_RELATION_DEF, ASSIGN_ROLE_CODE));
	}
}
