package jp.co.nci.iwf.component.route.download;

import java.io.Serializable;
import java.util.List;

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
import jp.co.nci.iwf.component.download.BaseDownloadDto;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;

/**
 * プロセス定義のダウンロードDTO
 */
public class ProcessDefDownloadDto extends BaseDownloadDto implements Serializable {
	/** アップロードファイルのプロセス定義コード */
	public String processDefCode;
	/** アップロードファイルのプロセス定義明細コード */
	public String processDefDetailCode;



	/** アクションリスト(WFM_ACTION) */
	public List<WfmAction> actionList;
	/** アクション定義リスト(WFM_ACTION_DEF) */
	public List<WfmActionDef> actionDefList;
	/** アクティビティ定義リスト(WFM_ACTIVITY_DEF) */
	public List<WfmActivityDef> activityDefList;
	/** アサイン定義リスト(WFM_ASSIGNED_DEF) */
	public List<WfmAssignedDef> assignedDefList;
	/** 参加者ロールリスト(WFM_ASSIGN_ROLE) */
	public List<WfmAssignRole> assignRoleList;
	/** 参加者ロールリスト構成(WFM_ASSIGN_ROLE_DETAIL) */
	public List<WfmAssignRoleDetail> assignRoleDetailList;
	/** 参加者変更定義(WFM_CHANGE_DEF) */
	public List<WfmChangeDef> changeDefList;
	/** 参加者変更ロール(WFM_CHANGE_ROLE) */
	public List<WfmChangeRole> changeRoleList;
	/** 参加者変更ロール構成(WFM_CHANGE_ROLE_DETAIL) */
	public List<WfmChangeRoleDetail> changeRoleDetailList;
	/** 代理設定リスト(WFM_AUTH_TRANSFER) */
	public List<WfmAuthTransfer> authTransferList;
	/** アクション遷移先定義リスト(WFM_CONDITION_DEF) */
	public List<WfmConditionDef> conditionDefList;
	/** 比較条件式定義リスト(WFM_EXPRESSION_DEF) */
	public List<WfmExpressionDef> expressionDefList;
	/** アクション機能定義リスト(WFM_FUNCTION_DEF) */
	public List<WfmFunctionDef> functionDefList;
	/** アクション機能リスト(WFM_FUNCTION) */
	public List<WfmFunction> functionList;
	/** 情報共有者定義リスト(WFM_INFORMATION_SHARER_DEF) */
	public List<WfmInformationSharerDef> informationSharerDefList;
	/** プロセス定義リスト(WFM_PROCESS_DEF) */
	public List<WfmProcessDef> processDefList;
	/** 比較条件式変数定義リスト(WFM_VARIABLE_DEF) */
	public List<WfmVariableDef> variableDefList;
	/** 名称ルックアップリスト(WFM_NAME_LOOKUP) */
	public List<WfmNameLookup> nameLookupList;
	/** WF間連携定義リスト(WFM_WF_RELATION_DEF) */
	public List<WfmWfRelationDefEx> wfRelationDefList;
	/** WF間連携権限定義リスト(WFM_WF_RELATION_AUTH_DEF) */
	public List<WfmWfRelationAuthDefEx> wfRelationAuthDefList;


	/**
	 * コンストラクタ
	 */
	public ProcessDefDownloadDto() {
	}

	/**
	 * コンストラクタ
	 * @param corporationCode
	 * @param corporationName
	 * @param processDefCode
	 * @param processDefDetailCode
	 */
	public ProcessDefDownloadDto(String corporationCode, String corporationName, String processDefCode, String processDefDetailCode) {
		super(corporationCode, corporationName);
		this.processDefCode = processDefCode;
		this.processDefDetailCode = processDefDetailCode;
	}
}
