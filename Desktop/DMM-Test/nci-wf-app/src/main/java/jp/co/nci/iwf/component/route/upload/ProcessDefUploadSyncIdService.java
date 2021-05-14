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
import jp.co.nci.iwf.component.download.BaseSyncIdService;

/**
 * プロセス定義アップロードでの採番値同期サービス
 */
@ApplicationScoped
public class ProcessDefUploadSyncIdService extends BaseSyncIdService {
	/** 初期化 */
	@PostConstruct
	public void init() {
		// エンティティクラスに対する WFM_PROPERTY.PROPERTY_CODEの定義
		propertyCodes.put(WfmActionDef.class, "WFM_ACTION_DEF_ID");
		propertyCodes.put(WfmAction.class, "WFM_ACTION_ID");
		propertyCodes.put(WfmActivityDef.class, "WFM_ACTIVITY_DEF_ID");
		propertyCodes.put(WfmAssignedDef.class, "WFM_ASSIGNED_DEF_ID");
		propertyCodes.put(WfmAssignRole.class, "WFM_ASSIGN_ROLE_ID");
		propertyCodes.put(WfmAssignRoleDetail.class, "WFM_ASSIGN_ROLE_DETAIL_ID");
		propertyCodes.put(WfmAuthTransfer.class, "WFM_AUTH_TRANSFER_ID");
		propertyCodes.put(WfmConditionDef.class, "WFM_CONDITION_DEF_ID");
		propertyCodes.put(WfmExpressionDef.class, "WFM_EXPRESSION_DEF_ID");
		propertyCodes.put(WfmFunctionDef.class, "WFM_FUNCTION_DEF_ID");
		propertyCodes.put(WfmFunction.class, "WFM_FUNCTION_ID");
		propertyCodes.put(WfmInformationSharerDef.class, "WFM_INFORMATION_SHARER_DEF_ID");
		propertyCodes.put(WfmProcessDef.class, "WFM_PROCESS_DEF_ID");
		propertyCodes.put(WfmVariableDef.class, "WFM_VARIABLE_DEF_ID");
		propertyCodes.put(WfmChangeDef.class, "WFM_CHANGE_DEF_ID");
		propertyCodes.put(WfmChangeRole.class, "WFM_CHANGE_ROLE_ID");
		propertyCodes.put(WfmChangeRoleDetail.class, "WFM_CHANGE_ROLE_DETAIL_ID");
		propertyCodes.put(WfmWfRelationDefEx.class, "WFM_WF_RELATION_DEF_ID");
		propertyCodes.put(WfmWfRelationAuthDefEx.class, "WFM_WF_RELATION_AUTH_DEF_ID");
	}
}
