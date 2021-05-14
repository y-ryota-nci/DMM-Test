package jp.co.nci.iwf.component.route.download;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
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
import jp.co.nci.integrated_workflow.model.custom.WfmCorporation;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunction;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.model.custom.WfmVariableDef;
import jp.co.nci.integrated_workflow.model.custom.WfmWfRelationAuthDefEx;
import jp.co.nci.integrated_workflow.model.custom.WfmWfRelationDefEx;
import jp.co.nci.integrated_workflow.param.input.SearchWfmActionDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmActionInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmActivityDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignRoleDetailInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignRoleInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignedDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmChangeDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmChangeRoleDetailInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmChangeRoleInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmConditionDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmExpressionDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmFunctionDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmFunctionInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmInformationSharerDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmVariableDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmWfRelationAuthDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmWfRelationDefInParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.CorporationService;
import jp.co.nci.iwf.component.download.BaseDownloadService;
import jp.co.nci.iwf.component.system.DestinationDatabaseService;
import jp.co.nci.iwf.component.system.ManifestService;
import jp.co.nci.iwf.jpa.entity.wf.WfmNameLookup;

/**
 * プロセス定義ダウンロードサービス
 */
@BizLogic
public class ProcessDefDownloadService extends BaseDownloadService {
	@Inject private WfInstanceWrapper wf;
	@Inject private DestinationDatabaseService destination;
	@Inject private ManifestService manifest;
	@Inject private ProcessDefNameLookupService nameLookup;
	@Inject private CorporationService corp;

	/**
	 * 指定されたプロセス定義とその関連テーブルをすべて抽出
	 * @param corporationCode 企業コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義明細コード
	 * @return
	 */
	public ProcessDefDownloadDto createDto(String corporationCode, String processDefCode, String processDefDetailCode) {
		if (isEmpty(corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (isEmpty(processDefCode))
			throw new BadRequestException("プロセス定義コードが未指定です");
		if (isEmpty(processDefDetailCode))
			throw new BadRequestException("プロセス定義明細コードが未指定です");

		final WfmCorporation c = corp.getWfmCorporation(corporationCode);
		final ProcessDefDownloadDto dto = new ProcessDefDownloadDto(
				corporationCode, c.getCorporationName(), processDefCode, processDefDetailCode);

		//------------------------------------------
		// ダウンロード時のAPPバージョンとDB接続先情報
		//------------------------------------------
		dto.appVersion = manifest.getVersion();
		dto.dbDestination = destination.getUrl();
		dto.dbUser = destination.getUser();
		dto.timestampCreated = timestamp();
		dto.hostIpAddr = hsr.getLocalAddr();
		dto.hostName = hsr.getLocalName();

		//------------------------------------------
		// プロセス定義とその関連テーブルのエンティティ
		//------------------------------------------
		// アクション定義リスト(WFM_ACTION_DEF)
		dto.actionList = getActionList(corporationCode);
		// アクション定義リスト(WFM_ACTION_DEF)
		dto.actionDefList = getActionDefList(corporationCode, processDefCode, processDefDetailCode);
		// アクティビティ定義リスト(WFM_ACTIVITY_DEF)
		dto.activityDefList = getActivityDefList(corporationCode, processDefCode, processDefDetailCode);
		// 参加者定義リスト(WFM_ASSIGNED_DEF)
		dto.assignedDefList = getAssignedDefList(corporationCode, processDefCode, processDefDetailCode);
		// 代理設定リスト(WFM_AUTH_TRANSFER)
		dto.authTransferList = getAuthTransferList(corporationCode, processDefCode, processDefDetailCode);
		// 参加者変更定義(WFM_CHANGE_DEF)
		dto.changeDefList = getChangeDefList(corporationCode, processDefCode, processDefDetailCode);
		// アクション遷移先定義リスト(WFM_CONDITION_DEF)
		dto.conditionDefList = getConditionDefList(corporationCode, processDefCode, processDefDetailCode);
		// 比較条件式定義リスト(WFM_EXPRESSION_DEF)
		dto.expressionDefList = getExpressionDefList(corporationCode, processDefCode, processDefDetailCode);
		// アクション機能リスト(WFM_FUNCTION)
		dto.functionList = getFunction(corporationCode);
		// アクション機能定義リスト(WFM_FUNCTION_DEF)
		dto.functionDefList = getFunctionDef(corporationCode, processDefCode, processDefDetailCode);
		// 情報共有者定義リスト(WFM_INFORMATION_SHARER_DEF)
		dto.informationSharerDefList = getInformationSharerDefList(corporationCode, processDefCode, processDefDetailCode);
		// プロセス定義リスト(WFM_PROCESS_DEF)
		dto.processDefList = getProcessDefList(corporationCode, processDefCode, processDefDetailCode);
		// 比較条件式変数定義リスト(WFM_VARIABLE_DEF)
		dto.variableDefList = getVariableDefList(corporationCode, processDefCode, processDefDetailCode);
		// WF間連携定義リスト(WFM_WF_RELATION_DEF)
		dto.wfRelationDefList = getWfRelationDefList(corporationCode, processDefCode, processDefDetailCode);
		// WF間連携権限定義リスト(WFM_WF_RELATION_DEF)
		dto.wfRelationAuthDefList = getWfRelationAuthDefList(corporationCode, processDefCode, processDefDetailCode);

		// 参加者ロールリスト(WFM_ASSIGN_ROLE)
		dto.assignRoleList = getAssignRoleList(corporationCode);
		// 参加者ロール構成リスト(WFM_ASSIGN_ROLE_DETAIL)
		dto.assignRoleDetailList = getAssignRoleDetailList(corporationCode);
		// 参加者変更ロール(WFM_CHANGE_ROLE)
		dto.changeRoleList = getChangeRoleList(corporationCode);
		// 参加者変更ロール構成(WFM_CHANGE_ROLE_DETAIL)
		dto.changeRoleDetailList = getChangeRoleDetailList(corporationCode);

		// 名称ルックアップリスト(WFM_NAME_LOOKUP)
		{
			final Map<Class<?>, List<?>> toAllEntities = toAllEntities(dto);
			dto.nameLookupList = nameLookup.getNameLookupList(toAllEntities);
		}

		// 抽出結果がユニークキーと矛盾してないか検証
		validateUniqueKeys(dto);

		return dto;
	}

	/** WF間連携権限定義リスト(WFM_WF_RELATION_DEF) */
	private List<WfmWfRelationAuthDefEx> getWfRelationAuthDefList(String corporationCode, String processDefCode,
			String processDefDetailCode) {
		final SearchWfmWfRelationAuthDefInParam in = new SearchWfmWfRelationAuthDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		return wf.searchWfmWfRelationAuthDef(in).getWfRelationAuthDefList();
	}

	/** WF間連携定義リスト(WFM_WF_RELATION_DEF) */
	private List<WfmWfRelationDefEx> getWfRelationDefList(String corporationCode, String processDefCode,
			String processDefDetailCode) {
		final SearchWfmWfRelationDefInParam in = new SearchWfmWfRelationDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		return wf.searchWfmWfRelationDefList(in).getWfRelationDefList();
	}

	/** アクション機能リスト(WFM_FUNCTION) */
	private List<WfmFunction> getFunction(String corporationCode) {
		final SearchWfmFunctionInParam in = new SearchWfmFunctionInParam();
		in.setCorporationCode(corporationCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "F." + WfmFunction.CORPORATION_CODE),
				new OrderBy(true, "F." + WfmFunction.FUNCTION_CODE),
		});
		return wf.searchWfmFunction(in).getFunctions();
	}

	/** アクション機能定義リスト(WFM_FUNCTION_DEF) */
	private List<WfmFunctionDef> getFunctionDef(String corporationCode, String processDefCode, String processDefDetailCode) {
		final SearchWfmFunctionDefInParam in = new SearchWfmFunctionDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "FD." + WfmFunctionDef.CORPORATION_CODE),
				new OrderBy(true, "FD." + WfmFunctionDef.PROCESS_DEF_CODE),
				new OrderBy(true, "FD." + WfmFunctionDef.PROCESS_DEF_DETAIL_CODE),
				new OrderBy(true, "FD." + WfmFunctionDef.ACTIVITY_DEF_CODE),
				new OrderBy(true, "FD." + WfmFunctionDef.SEQ_NO_ACTION_DEF),
				new OrderBy(true, "FD." + WfmFunctionDef.SEQ_NO_FUNCTION_DEF),
		});
		return wf.searchWfmFunctionDef(in).getFunctionDefs();
	}

	/** 比較条件式変数定義リスト(WFM_VARIABLE_DEF) */
	private List<WfmVariableDef> getVariableDefList(String corporationCode, String processDefCode, String processDefDetailCode) {
		final SearchWfmVariableDefInParam in = new SearchWfmVariableDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "VD." + WfmVariableDef.CORPORATION_CODE),
				new OrderBy(true, "VD." + WfmVariableDef.PROCESS_DEF_CODE),
				new OrderBy(true, "VD." + WfmVariableDef.PROCESS_DEF_DETAIL_CODE),
				new OrderBy(true, "VD." + WfmVariableDef.VARIABLE_DEF_CODE),
		});
		return wf.searchWfmVariableDef(in).getVariableDefs();
	}

	/** プロセス定義リスト(WFM_PROCESS_DEF) */
	private List<WfmProcessDef> getProcessDefList(String corporationCode, String processDefCode, String processDefDetailCode) {
		final SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setSearchType(SearchMode.SEARCH_MODE_OBJECT);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "PD." + WfmProcessDef.CORPORATION_CODE),
				new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_CODE),
				new OrderBy(true, "PD." + WfmProcessDef.PROCESS_DEF_DETAIL_CODE),
		});
		return wf.searchWfmProcessDef(in).getProcessDefs();
	}

	/** 情報共有者定義リスト(WFM_INFORMATION_SHARER_DEF) */
	private List<WfmInformationSharerDef> getInformationSharerDefList(String corporationCode, String processDefCode, String processDefDetailCode) {
		final SearchWfmInformationSharerDefInParam in = new SearchWfmInformationSharerDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "S." + WfmInformationSharerDef.CORPORATION_CODE),
				new OrderBy(true, "S." + WfmInformationSharerDef.PROCESS_DEF_CODE),
				new OrderBy(true, "S." + WfmInformationSharerDef.PROCESS_DEF_DETAIL_CODE),
				new OrderBy(true, "S." + WfmInformationSharerDef.SEQ_NO_INFO_SHARER_DEF),
		});
		return wf.searchWfmInformationSharerDef(in).getInformationSharerDefs();
	}

	/** 比較条件式定義リスト(WFM_EXPRESSION_DEF) */
	private List<WfmExpressionDef> getExpressionDefList(String corporationCode, String processDefCode, String processDefDetailCode) {
		final SearchWfmExpressionDefInParam in = new SearchWfmExpressionDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "ED." + WfmExpressionDef.CORPORATION_CODE),
				new OrderBy(true, "ED." + WfmExpressionDef.PROCESS_DEF_CODE),
				new OrderBy(true, "ED." + WfmExpressionDef.PROCESS_DEF_DETAIL_CODE),
				new OrderBy(true, "ED." + WfmExpressionDef.EXPRESSION_DEF_CODE),
		});
		return wf.searchWfmExpressionDef(in).getExpressionDefs();
	}

	/** アクション遷移先定義リスト(WFM_CONDITION_DEF) */
	private List<WfmConditionDef> getConditionDefList(String corporationCode, String processDefCode, String processDefDetailCode) {
		final SearchWfmConditionDefInParam in = new SearchWfmConditionDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "CD." + WfmConditionDef.CORPORATION_CODE),
				new OrderBy(true, "CD." + WfmConditionDef.PROCESS_DEF_CODE),
				new OrderBy(true, "CD." + WfmConditionDef.PROCESS_DEF_DETAIL_CODE),
				new OrderBy(true, "CD." + WfmConditionDef.ACTIVITY_DEF_CODE),
				new OrderBy(true, "CD." + WfmConditionDef.SEQ_NO_ACTION_DEF),
				new OrderBy(true, "CD." + WfmConditionDef.SEQ_NO_CONDITION_DEF),
		});
		return wf.searchWfmConditionDef(in).getConditionDefs();
	}

	/** 参加者変更ロール構成(WFM_CHANGE_ROLE_DETAIL) */
	private List<WfmChangeRoleDetail> getChangeRoleDetailList(String corporationCode) {
		final SearchWfmChangeRoleDetailInParam in = new SearchWfmChangeRoleDetailInParam();
		in.setCorporationCode(corporationCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "A." + WfmChangeRoleDetail.CORPORATION_CODE),
				new OrderBy(true, "A." + WfmChangeRoleDetail.CHANGE_ROLE_CODE),
				new OrderBy(true, "A." + WfmChangeRoleDetail.SEQ_NO_CHANGE_ROLE_DETAIL),
		});
		return wf.searchWfmChangeRoleDetail(in).getChangeRoleDetails();
	}

	/** 参加者変更ロール(WFM_CHANGE_ROLE) */
	private List<WfmChangeRole> getChangeRoleList(String corporationCode) {
		final SearchWfmChangeRoleInParam in = new SearchWfmChangeRoleInParam();
		in.setCorporationCode(corporationCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "A." + WfmChangeRole.CORPORATION_CODE),
				new OrderBy(true, "A." + WfmChangeRole.CHANGE_ROLE_CODE),
		});
		return wf.searchWfmChangeRole(in).getChangeRoles();
	}

	/** 参加者変更定義(WFM_CHANGE_DEF) */
	private List<WfmChangeDef> getChangeDefList(String corporationCode, String processDefCode, String processDefDetailCode) {
		final SearchWfmChangeDefInParam in = new SearchWfmChangeDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "a." + WfmChangeDef.CORPORATION_CODE),
				new OrderBy(true, "a." + WfmChangeDef.PROCESS_DEF_CODE),
				new OrderBy(true, "a." + WfmChangeDef.PROCESS_DEF_DETAIL_CODE),
				new OrderBy(true, "a." + WfmChangeDef.ACTIVITY_DEF_CODE),
				new OrderBy(true, "a." + WfmChangeDef.SEQ_NO_CHANGE_DEF),
		});
		return wf.searchWfmChangeDef(in).getChangeDefs();
	}

	/** 代理設定リスト(WFM_AUTH_TRANSFER) */
	private List<WfmAuthTransfer> getAuthTransferList(String corporationCode, String processDefCode, String processDefDetailCode) {
		// TODO:DMMの場合大量に代理設定があるためダウンロードを行うとシステムエラーが発生する為、取得データを0件にする
//		final SearchWfmAuthTransferInParam in = new SearchWfmAuthTransferInParam();
//		in.setCorporationCode(corporationCode);
//		in.setProcessDefCode(processDefCode);
//		in.setProcessDefDetailCode(processDefDetailCode);
//		in.setOrderBy(new OrderBy[] {
//				new OrderBy(true, WfmAuthTransfer.CORPORATION_CODE),
//				new OrderBy(true, WfmAuthTransfer.SEQ_NO_AUTH_TRANSFER),
//		});
//		return wf.searchWfmAuthTransfer(in).getList();
		return new ArrayList<>();
	}

	/** 参加者ロール構成リスト(WFM_ASSIGN_ROLE_DETAIL) */
	private List<WfmAssignRoleDetail> getAssignRoleDetailList(String corporationCode) {
		final SearchWfmAssignRoleDetailInParam in = new SearchWfmAssignRoleDetailInParam();
		in.setCorporationCode(corporationCode);
		return wf.searchWfmAssignRoleDetail(in).getAssignRoleDetails();
	}

	/** 参加者ロールリスト(WFM_ASSIGN_ROLE) */
	private List<WfmAssignRole> getAssignRoleList(String corporationCode) {
		final SearchWfmAssignRoleInParam in = new SearchWfmAssignRoleInParam();
		in.setCorporationCode(corporationCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "R." + WfmAssignRole.CORPORATION_CODE),
				new OrderBy(true, "R." + WfmAssignRole.ASSIGN_ROLE_CODE),
		});
		return wf.searchWfmAssignRole(in).getAssignRoles();
	}

	/** 参加者定義リスト(WFM_ASSIGNED_DEF) */
	private List<WfmAssignedDef> getAssignedDefList(String corporationCode, String processDefCode, String processDefDetailCode) {
		final SearchWfmAssignedDefInParam in = new SearchWfmAssignedDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "AD." + WfmAssignedDef.CORPORATION_CODE),
				new OrderBy(true, "AD." + WfmAssignedDef.PROCESS_DEF_CODE),
				new OrderBy(true, "AD." + WfmAssignedDef.PROCESS_DEF_DETAIL_CODE),
				new OrderBy(true, "AD." + WfmAssignedDef.ACTIVITY_DEF_CODE),
				new OrderBy(true, "AD." + WfmAssignedDef.SEQ_NO_ASSIGNED_DEF),
		});
		return wf.searchWfmAssignedDef(in).getAssignedDefs();
	}

	/** アクティビティ定義リスト(WFM_ACTIVITY_DEF) */
	private List<WfmActivityDef> getActivityDefList(String corporationCode, String processDefCode, String processDefDetailCode) {
		final SearchWfmActivityDefInParam in = new SearchWfmActivityDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "AD." + WfmActivityDef.CORPORATION_CODE),
				new OrderBy(true, "AD." + WfmActivityDef.PROCESS_DEF_CODE),
				new OrderBy(true, "AD." + WfmActivityDef.PROCESS_DEF_DETAIL_CODE),
				new OrderBy(true, "AD." + WfmActivityDef.ACTIVITY_DEF_CODE),
		});
		return wf.searchWfmActivityDef(in).getActivityDefs();
	}

	/** アクションリスト(WFM_ACTION) */
	private List<WfmAction> getActionList(String corporationCode) {
		final SearchWfmActionInParam in = new SearchWfmActionInParam();
		in.setCorporationCode(corporationCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "A." + WfmAction.CORPORATION_CODE),
				new OrderBy(true, "A." + WfmAction.ACTION_CODE),
		});
		return wf.searchWfmAction(in).getActions();
	}

	/** アクション定義リスト(WFM_ACTION_DEF) */
	private List<WfmActionDef> getActionDefList(String corporationCode, String processDefCode, String processDefDetailCode) {
		final SearchWfmActionDefInParam in = new SearchWfmActionDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(true, "AD." + WfmActionDef.CORPORATION_CODE),
				new OrderBy(true, "AD." + WfmActionDef.PROCESS_DEF_CODE),
				new OrderBy(true, "AD." + WfmActionDef.PROCESS_DEF_DETAIL_CODE),
				new OrderBy(true, "AD." + WfmActionDef.ACTIVITY_DEF_CODE),
				new OrderBy(true, "AD." + WfmActionDef.SEQ_NO_ACTION_DEF),
		});
		return wf.searchWfmActionDef(in).getActionDefs();
	}

	/** ダウンロード対象のエンティティリストをリフレクションで扱いやすいようフラットに配列化 */
	public Map<Class<?>, List<?>> toAllEntities(ProcessDefDownloadDto dto) {
		// ダウンロード対象のエンティティをフラットに扱うため、配列化
		Map<Class<?>, List<?>> map = new HashMap<>();
		map.put(WfmAction.class, dto.actionList);
		map.put(WfmActionDef.class, dto.actionDefList);
		map.put(WfmActivityDef.class, dto.activityDefList);
		map.put(WfmAssignedDef.class, dto.assignedDefList);
		map.put(WfmAssignRole.class, dto.assignRoleList);
		map.put(WfmAssignRoleDetail.class, dto.assignRoleDetailList);
		map.put(WfmChangeDef.class, dto.changeDefList);
		map.put(WfmChangeRole.class, dto.changeRoleList);
		map.put(WfmChangeRoleDetail.class, dto.changeRoleDetailList);
		map.put(WfmAuthTransfer.class, dto.authTransferList);
		map.put(WfmConditionDef.class, dto.conditionDefList);
		map.put(WfmExpressionDef.class, dto.expressionDefList);
		map.put(WfmFunctionDef.class, dto.functionDefList);
		map.put(WfmFunction.class, dto.functionList);
		map.put(WfmInformationSharerDef.class, dto.informationSharerDefList);
		map.put(WfmProcessDef.class, dto.processDefList);
		map.put(WfmVariableDef.class, dto.variableDefList);
		map.put(WfmNameLookup.class, dto.nameLookupList);
		map.put(WfmWfRelationDefEx.class, dto.wfRelationDefList);
		map.put(WfmWfRelationAuthDefEx.class, dto.wfRelationAuthDefList);
		return map;
	}
}
