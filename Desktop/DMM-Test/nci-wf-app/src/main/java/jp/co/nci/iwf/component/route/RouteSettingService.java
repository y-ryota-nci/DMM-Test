package jp.co.nci.iwf.component.route;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.co.nci.integrated_workflow.api.param.input.CopyRouteDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmActionDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmActivityDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmAssignedDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmChangeDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmConditionDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmExpressionDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmFunctionDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmInformationSharerDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.DeleteWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertDefaultWfmVariableDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmActionDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmActivityDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmAssignedDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmChangeDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmConditionDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmExpressionDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmFunctionDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmInformationSharerDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.InsertWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmActionDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmActivityDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmAssignedDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmChangeDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmConditionDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmExpressionDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmFunctionDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmInformationSharerDefInParam;
import jp.co.nci.integrated_workflow.api.param.input.UpdateWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.api.param.output.CopyRouteDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmActionDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmActivityDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmAssignedDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmChangeDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmConditionDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmExpressionDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmFunctionDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmInformationSharerDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.InsertWfmProcessDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmActionDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmActivityDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmAssignedDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmChangeDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmConditionDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmExpressionDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmFunctionDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmInformationSharerDefOutParam;
import jp.co.nci.integrated_workflow.api.param.output.UpdateWfmProcessDefOutParam;
import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.common.message.MessageId;
import jp.co.nci.integrated_workflow.common.message.MessageResource;
import jp.co.nci.integrated_workflow.common.query.QueryDef.SearchMode;
import jp.co.nci.integrated_workflow.common.util.OrderBy;
import jp.co.nci.integrated_workflow.model.base.WfmMessage;
import jp.co.nci.integrated_workflow.model.custom.WfmAction;
import jp.co.nci.integrated_workflow.model.custom.WfmActionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActionTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignedDef;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignedTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeDef;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunction;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmInformationSharerDef;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.integrated_workflow.model.custom.WfmVariableDef;
import jp.co.nci.integrated_workflow.param.input.SearchIncrementActivityDefCodeInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmActionDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmActionInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmActionTemplateInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmActivityDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmActivityTemplateInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignRoleInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignedDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmAssignedTemplateInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmChangeDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmChangeRoleInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmConditionDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmExpressionDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmFunctionDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmFunctionInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmFunctionTemplateInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmInformationSharerDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmProcessDefInParam;
import jp.co.nci.integrated_workflow.param.input.SearchWfmVariableDefInParam;
import jp.co.nci.integrated_workflow.param.output.SearchIncrementActivityDefCodeOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmActionDefOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmActivityDefOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmAssignedDefOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmChangeDefOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmConditionDefOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmExpressionDefOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmFunctionDefOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmInformationSharerDefOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmProcessDefOutParam;
import jp.co.nci.integrated_workflow.param.output.SearchWfmVariableDefOutParam;
import jp.co.nci.integrated_workflow.wrapper.WfInstanceWrapper;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.endpoint.mm.mm0020.Mm0020Repository;
import jp.co.nci.iwf.endpoint.vd.vd0310.entity.TrayEntity;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ルート定義設定サービス
 */
@BizLogic
public class RouteSettingService extends BaseService implements CodeMaster {

	@Inject
	protected WfInstanceWrapper wf;
	@Inject
	private Mm0020Repository mm0020Repository;

	/**
	 * プロセス定義一覧取得
	 * @param corporationCode 会社コード
	 * @return プロセス定義一覧
	 */
	public List<WfmProcessDef> getProcessDefs(String corporationCode) {
		SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(OrderBy.ASC, "PD.PROCESS_DEF_CODE"),
				new OrderBy(OrderBy.ASC, "PD.PROCESS_DEF_DETAIL_CODE")
		});
		return wf.searchWfmProcessDef(in).getProcessDefs();
	}

	/**
	 * プロセス定義を取得する
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @return プロセス定義
	 */
	public WfmProcessDef getProcessDef(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode) {

		SearchWfmProcessDefInParam in = new SearchWfmProcessDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setSearchType(SearchMode.SEARCH_MODE_OBJECT);
		SearchWfmProcessDefOutParam out = wf.searchWfmProcessDef(in);
		if (isEmpty(out.getProcessDefs())) {
			final WfmMessage msg = MessageResource.getMessage(wf, MessageId.IWFZ01001001I, "searchWfmProcessDef");
			throw new WfException(ReturnCode.INVALID_PARAMETER, msg);
		}
		return out.getProcessDefs().get(0);
	}

	/**
	 * 比較条件式定義一覧取得
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @return 比較条件式定義一覧
	 */
	public List<WfmExpressionDef> getExpressionDefs(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode) {

		SearchWfmExpressionDefInParam in = new SearchWfmExpressionDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setDeleteFlag(DeleteFlag.OFF);

		in.setOrderBy(new OrderBy[] {
				new OrderBy(OrderBy.ASC, "ED.EXPRESSION_DEF_CODE")
		});

		return wf.searchWfmExpressionDef(in).getExpressionDefs();
	}

	/**
	 * 比較条件式定義を取得する
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @param expressionDefCode 比較条件式定義コード
	 * @return 比較条件式定義
	 */
	public WfmExpressionDef getExpressionDef(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode
			, String expressionDefCode) {

		SearchWfmExpressionDefInParam in = new SearchWfmExpressionDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setExpressionDefCode(expressionDefCode);
		SearchWfmExpressionDefOutParam out = wf.searchWfmExpressionDef(in);
		if (isEmpty(out.getExpressionDefs())) {
			final WfmMessage msg = MessageResource.getMessage(wf, MessageId.IWFZ01001001I, "searchWfmExpressionDef");
			throw new WfException(ReturnCode.INVALID_PARAMETER, msg);
		}
		return out.getExpressionDefs().get(0);
	}

	/**
	 * 情報共有者定義一覧取得
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @return 情報共有者定義一覧
	 */
	public List<WfmInformationSharerDef> getInformationSharerDefs(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode) {

		SearchWfmInformationSharerDefInParam in = new SearchWfmInformationSharerDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setDeleteFlag(DeleteFlag.OFF);

		in.setOrderBy(new OrderBy[] {
				new OrderBy(OrderBy.ASC, "S.SEQ_NO_INFO_SHARER_DEF")
		});

		return wf.searchWfmInformationSharerDef(in).getInformationSharerDefs();
	}

	/**
	 * 情報共有者定義を取得する
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @param seqNoInfoSharerDef 情報共有者定義連番
	 * @return 情報共有者定義
	 */
	public WfmInformationSharerDef getInformationSharerDef(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode
			, Long seqNoInfoSharerDef) {

		SearchWfmInformationSharerDefInParam in = new SearchWfmInformationSharerDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setSeqNoInfoSharerDef(seqNoInfoSharerDef);
		SearchWfmInformationSharerDefOutParam out = wf.searchWfmInformationSharerDef(in);
		if (isEmpty(out.getInformationSharerDefs())) {
			final WfmMessage msg = MessageResource.getMessage(wf, MessageId.IWFZ01001001I, "searchWfmInformationSharerDef");
			throw new WfException(ReturnCode.INVALID_PARAMETER, msg);
		}
		return out.getInformationSharerDefs().get(0);
	}

	/**
	 * 比較条件式変数定義一覧取得
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @return 比較条件式変数定義一覧
	 */
	public List<WfmVariableDef> getVariableDefs(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode) {

		SearchWfmVariableDefInParam in = new SearchWfmVariableDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setDeleteFlag(DeleteFlag.OFF);

		in.setOrderBy(new OrderBy[] {
				new OrderBy(OrderBy.DESC, "VD.VARIABLE_DEF_TYPE"),
				new OrderBy(OrderBy.ASC, "VD.VARIABLE_DEF_CODE")
		});
		return wf.searchWfmVariableDef(in).getVariableDefs();
	}

	/**
	 * 比較条件式変数定義を取得する
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @param variableDefCode 比較条件式変数定義コード
	 * @return 比較条件式変数定義
	 */
	public WfmVariableDef getVariableDef(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode
			, String variableDefCode) {

		SearchWfmVariableDefInParam in = new SearchWfmVariableDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setVariableDefCode(variableDefCode);
		SearchWfmVariableDefOutParam out = wf.searchWfmVariableDef(in);
		if (isEmpty(out.getVariableDefs())) {
			final WfmMessage msg = MessageResource.getMessage(wf, MessageId.IWFZ01001001I, "searchWfmVariableDef");
			throw new WfException(ReturnCode.INVALID_PARAMETER, msg);
		}
		return out.getVariableDefs().get(0);
	}

	/**
	 * アクティビティテンプレート一覧取得
	 * ※会社毎のテンプレートが存在しない場合はASPのレコードを返す
	 * @param corporationCode 会社コード
	 * @return アクティビティテンプレート一覧
	 */
	public List<WfmActivityTemplate> getActivityTemplates(String corporationCode) {
		SearchWfmActivityTemplateInParam in = new SearchWfmActivityTemplateInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);

		Map<String, List<WfmActivityTemplate>> templates =
				wf.searchWfmActivityTemplate(in).getActivityTemplates().stream().collect(Collectors.groupingBy(WfmActivityTemplate::getCorporationCode, Collectors.toList()));
		if (isEmpty(templates.get(corporationCode))) {
			return templates.get(CorporationCode.ASP);
		}
		return templates.get(corporationCode);
	}

	/**
	 * 参加者テンプレート一覧取得
	 * @param corporationCode 会社コード
	 * @return 参加者テンプレート一覧
	 */
	public List<WfmAssignedTemplate> getAssignedTemplates(String corporationCode) {
		SearchWfmAssignedTemplateInParam in = new SearchWfmAssignedTemplateInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		return wf.searchWfmAssignedTemplate(in).getAssignedTemplates();
	}

	/**
	 * アクションテンプレート一覧取得
	 * @param corporationCode 会社コード
	 * @return アクションテンプレート一覧
	 */
	public List<WfmActionTemplate> getActionTemplates(String corporationCode) {
		SearchWfmActionTemplateInParam in = new SearchWfmActionTemplateInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		return wf.searchWfmActionTemplate(in).getActionTemplates();
	}

	/**
	 * アクション機能テンプレート一覧取得
	 * @param corporationCode 会社コード
	 * @return アクション機能テンプレート一覧
	 */
	public List<WfmFunctionTemplate> getFunctionTemplates(String corporationCode) {
		SearchWfmFunctionTemplateInParam in = new SearchWfmFunctionTemplateInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		return wf.searchWfmFunctionTemplate(in).getFunctionTemplates();
	}

	/**
	 * アクティビティ定義一覧取得
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @return アクティビティ定義一覧
	 */
	public List<WfmActivityDef> getActivityDefs(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode) {

		SearchWfmActivityDefInParam in = new SearchWfmActivityDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[] {new OrderBy(OrderBy.ASC, "AD.ACTIVITY_DEF_CODE")});
		return wf.searchWfmActivityDef(in).getActivityDefs();
	}

	/**
	 * アクティビティ定義を取得する
	 * @param tray トレイ情報
	 * @return
	 */
	public WfmActivityDef getActivityDef(TrayEntity tray) {
		return getActivityDef(tray.corporationCode, tray.processDefCode, tray.processDefDetailCode, tray.activityDefCode);
	}

	/**
	 * アクティビティ定義を取得する
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @param activityDefCode アクティビティ定義コード
	 * @return アクティビティ定義
	 */
	public WfmActivityDef getActivityDef(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode
			, String activityDefCode) {

		SearchWfmActivityDefInParam in = new SearchWfmActivityDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setActivityDefCode(activityDefCode);
		SearchWfmActivityDefOutParam out = wf.searchWfmActivityDef(in);
		if (isEmpty(out.getActivityDefs())) {
			final WfmMessage msg = MessageResource.getMessage(wf, MessageId.IWFZ01001001I, "searchWfmActivityDef");
			throw new WfException(ReturnCode.INVALID_PARAMETER, msg);
		}
		return out.getActivityDefs().get(0);
	}

	/**
	 * 参加者定義一覧取得
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @return 参加者定義
	 */
	public List<WfmAssignedDef> getAssignedDefs(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode) {

		SearchWfmAssignedDefInParam in = new SearchWfmAssignedDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(OrderBy.ASC, "AD.ACTIVITY_DEF_CODE"),
				new OrderBy(OrderBy.ASC, "AD.SORT_ORDER"),
				new OrderBy(OrderBy.ASC, "AD.SEQ_NO_ASSIGNED_DEF"),
				new OrderBy(OrderBy.ASC, "AD.ASSIGN_ROLE_CODE")
		});
		return wf.searchWfmAssignedDef(in).getAssignedDefs();
	}

	/**
	 * 参加者定義を取得する
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @param activityDefCode アクティビティ定義コード
	 * @param seqNoActionDef アクション定義連番
	 * @return アクション定義
	 */
	public WfmAssignedDef getAssignedDef(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode
			, String activityDefCode
			, Long seqNoAssignedDef) {

		SearchWfmAssignedDefInParam in = new SearchWfmAssignedDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setActivityDefCode(activityDefCode);
		in.setSeqNoAssignedDef(seqNoAssignedDef);
		SearchWfmAssignedDefOutParam out = wf.searchWfmAssignedDef(in);
		if (isEmpty(out.getAssignedDefs())) {
			final WfmMessage msg = MessageResource.getMessage(wf, MessageId.IWFZ01001001I, "searchWfmAssignedDef");
			throw new WfException(ReturnCode.INVALID_PARAMETER, msg);
		}
		return out.getAssignedDefs().get(0);
	}

	/**
	 * 参加者変更定義一覧取得
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @return 参加者変更定義
	 */
	public List<WfmChangeDef> getChangeDefs(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode) {

		SearchWfmChangeDefInParam in = new SearchWfmChangeDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[] {
				new OrderBy(OrderBy.ASC, "A.ACTIVITY_DEF_CODE"),
				new OrderBy(OrderBy.ASC, "A.SORT_ORDER"),
				new OrderBy(OrderBy.ASC, "A.SEQ_NO_CHANGE_DEF"),
				new OrderBy(OrderBy.ASC, "A.CHANGE_ROLE_CODE")
		});
		return wf.searchWfmChangeDef(in).getChangeDefs();
	}

	/**
	 * 参加者変更定義を取得する
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @param activityDefCode アクティビティ定義コード
	 * @param seqNoChangeDef 参加者変更定義連番
	 * @return 参加者変更定義
	 */
	public WfmChangeDef getChangeDef(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode
			, String activityDefCode
			, Long seqNoChangeDef) {

		SearchWfmChangeDefInParam in = new SearchWfmChangeDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setActivityDefCode(activityDefCode);
		in.setSeqNoChangeDef(seqNoChangeDef);
		SearchWfmChangeDefOutParam out = wf.searchWfmChangeDef(in);
		if (isEmpty(out.getChangeDefs())) {
			final WfmMessage msg = MessageResource.getMessage(wf, MessageId.IWFZ01001001I, "searchWfmChangeDef");
			throw new WfException(ReturnCode.INVALID_PARAMETER, msg);
		}
		return out.getChangeDefs().get(0);
	}

	/**
	 * アクション定義一覧取得
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @return アクション定義
	 */
	public List<WfmActionDef> getActionDefs(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode) {

		SearchWfmActionDefInParam in = new SearchWfmActionDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setDeleteFlag(DeleteFlag.OFF);

		in.setOrderBy(new OrderBy[] {
				new OrderBy(OrderBy.ASC, "AD.ACTIVITY_DEF_CODE"),
				new OrderBy(OrderBy.ASC, "AD.SORT_ORDER"),
				new OrderBy(OrderBy.DESC, "AD.DEFAULT_FLAG"),
				new OrderBy(OrderBy.ASC, "AD.SEQ_NO_ACTION_DEF"),
				new OrderBy(OrderBy.ASC, "AD.ACTION_CODE")
		});
		return wf.searchWfmActionDef(in).getActionDefs();
	}

	/**
	 * アクション定義を取得する
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @param activityDefCode アクティビティ定義コード
	 * @param seqNoActionDef アクション定義連番
	 * @return アクション定義
	 */
	public WfmActionDef getActionDef(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode
			, String activityDefCode
			, Long seqNoActionDef) {

		SearchWfmActionDefInParam in = new SearchWfmActionDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setActivityDefCode(activityDefCode);
		in.setSeqNoActionDef(seqNoActionDef);
		SearchWfmActionDefOutParam out = wf.searchWfmActionDef(in);
		if (isEmpty(out.getActionDefs())) {
			final WfmMessage msg = MessageResource.getMessage(wf, MessageId.IWFZ01001001I, "searchWfmActionDef");
			throw new WfException(ReturnCode.INVALID_PARAMETER, msg);
		}
		return out.getActionDefs().get(0);
	}

	/**
	 * アクション遷移先定義一覧取得
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @return アクション遷移先定義一覧
	 */
	public List<WfmConditionDef> getConditionDefs(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode) {

		SearchWfmConditionDefInParam in = new SearchWfmConditionDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setDeleteFlag(DeleteFlag.OFF);

		in.setOrderBy(new OrderBy[] {
				new OrderBy(OrderBy.ASC, "CD.ACTIVITY_DEF_CODE"),
				new OrderBy(OrderBy.ASC, "CD.SEQ_NO_ACTION_DEF"),
				new OrderBy(OrderBy.DESC, "CD.DEFAULT_FLAG"),
				new OrderBy(OrderBy.ASC, "CD.SORT_ORDER"),
				new OrderBy(OrderBy.ASC, "CD.SEQ_NO_CONDITION_DEF")
		});
		return wf.searchWfmConditionDef(in).getConditionDefs();
	}

	/**
	 * アクション遷移先定義を取得する
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @param activityDefCode アクティビティ定義コード
	 * @param seqNoActionDef アクション定義連番
	 * @param seqNoConditionDef アクション遷移先定義連番
	 * @return アクション遷移先定義
	 */
	public WfmConditionDef getConditionDef(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode
			, String activityDefCode
			, Long seqNoActionDef
			, Long seqNoConditionDef) {

		SearchWfmConditionDefInParam in = new SearchWfmConditionDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setActivityDefCode(activityDefCode);
		in.setSeqNoActionDef(seqNoActionDef);
		in.setSeqNoConditionDef(seqNoConditionDef);
		SearchWfmConditionDefOutParam out = wf.searchWfmConditionDef(in);
		if (isEmpty(out.getConditionDefs())) {
			final WfmMessage msg = MessageResource.getMessage(wf, MessageId.IWFZ01001001I, "searchWfmConditionDef");
			throw new WfException(ReturnCode.INVALID_PARAMETER, msg);
		}
		return out.getConditionDefs().get(0);
	}

	/**
	 * アクション機能定義一覧取得
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @return アクション機能定義一覧
	 */
	public List<WfmFunctionDef> getFunctionDefs(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode) {

		SearchWfmFunctionDefInParam in = new SearchWfmFunctionDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setDeleteFlag(DeleteFlag.OFF);

		in.setOrderBy(new OrderBy[] {
				new OrderBy(OrderBy.ASC, "FD.ACTIVITY_DEF_CODE"),
				new OrderBy(OrderBy.ASC, "FD.SEQ_NO_ACTION_DEF"),
				new OrderBy(OrderBy.ASC, "FD.FUNCTION_EXECUTION_ORDER"),
				new OrderBy(OrderBy.ASC, "FD.SORT_ORDER"),
				new OrderBy(OrderBy.ASC, "FD.SEQ_NO_FUNCTION_DEF")
		});
		return wf.searchWfmFunctionDef(in).getFunctionDefs();
	}

	/**
	 * アクション機能定義を取得する
	 * @param corporationCode 会社コード
	 * @param processDefCode プロセス定義コード
	 * @param processDefDetailCode プロセス定義枝番
	 * @param activityDefCode アクティビティ定義コード
	 * @param seqNoActionDef アクション定義連番
	 * @param seqNoFunctionDef アクション機能定義連番
	 * @return アクション機能定義
	 */
	public WfmFunctionDef getFunctionDef(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode
			, String activityDefCode
			, Long seqNoActionDef
			, Long seqNoFunctionDef) {

		SearchWfmFunctionDefInParam in = new SearchWfmFunctionDefInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setActivityDefCode(activityDefCode);
		in.setSeqNoActionDef(seqNoActionDef);
		in.setSeqNoFunctionDef(seqNoFunctionDef);
		SearchWfmFunctionDefOutParam out = wf.searchWfmFunctionDef(in);
		if (isEmpty(out.getFunctionDefs())) {
			final WfmMessage msg = MessageResource.getMessage(wf, MessageId.IWFZ01001001I, "searchWfmConditionDef");
			throw new WfException(ReturnCode.INVALID_PARAMETER, msg);
		}
		return out.getFunctionDefs().get(0);
	}

	public WfmProcessDef insert(boolean reSearch, WfmProcessDef processDef) {
		InsertWfmProcessDefInParam in = new InsertWfmProcessDefInParam();
		in.setWfmProcessDef(processDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmProcessDefOutParam out = wf.insertWfmProcessDef(in);

		if (!reSearch) return null;

		return getProcessDef(
				out.getWfmProcessDef().getCorporationCode()
				, out.getWfmProcessDef().getProcessDefCode()
				, out.getWfmProcessDef().getProcessDefDetailCode());
	}

	public void insertDefaultVariableDef(WfmProcessDef processDef) {
		InsertDefaultWfmVariableDefInParam in = new InsertDefaultWfmVariableDefInParam();
		in.setWfmProcessDef(processDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		wf.insertDefaultWfmVariableDef(in);
	}

	public WfmProcessDef copy(boolean reSearch, WfmProcessDef from, WfmProcessDef to) {
		CopyRouteDefInParam in = new CopyRouteDefInParam();
		in.setWfmProcessDefFrom(from);
		in.setWfmProcessDefTo(to);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		CopyRouteDefOutParam out = wf.copyRouteDef(in);

		if (!reSearch) return null;

		return getProcessDef(
				out.getWfmProcessDef().getCorporationCode()
				, out.getWfmProcessDef().getProcessDefCode()
				, out.getWfmProcessDef().getProcessDefDetailCode());
	}

	public WfmProcessDef update(boolean reSearch, WfmProcessDef processDef) {
		UpdateWfmProcessDefInParam in = new UpdateWfmProcessDefInParam();
		in.setWfmProcessDef(processDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmProcessDefOutParam out = wf.updateWfmProcessDef(in);

		if (!reSearch) return null;

		return getProcessDef(
				out.getWfmProcessDef().getCorporationCode()
				, out.getWfmProcessDef().getProcessDefCode()
				, out.getWfmProcessDef().getProcessDefDetailCode());
	}

	public WfmProcessDef delete(WfmProcessDef processDef) {
		DeleteWfmProcessDefInParam in = new DeleteWfmProcessDefInParam();
		in.setWfmProcessDef(processDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmProcessDef(in);
		return processDef;
	}

	public WfmExpressionDef insert(boolean reSearch, WfmExpressionDef expressionDef) {
		InsertWfmExpressionDefInParam in = new InsertWfmExpressionDefInParam();
		in.setWfmExpressionDef(expressionDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmExpressionDefOutParam out = wf.insertWfmExpressionDef(in);

		if (!reSearch) return null;

		return getExpressionDef(
				out.getWfmExpressionDef().getCorporationCode()
				, out.getWfmExpressionDef().getProcessDefCode()
				, out.getWfmExpressionDef().getProcessDefDetailCode()
				, out.getWfmExpressionDef().getExpressionDefCode());
	}

	public WfmExpressionDef update(boolean reSearch, WfmExpressionDef expressionDef) {
		UpdateWfmExpressionDefInParam in = new UpdateWfmExpressionDefInParam();
		in.setWfmExpressionDef(expressionDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmExpressionDefOutParam out = wf.updateWfmExpressionDef(in);

		if (!reSearch) return null;

		return getExpressionDef(
				out.getWfmExpressionDef().getCorporationCode()
				, out.getWfmExpressionDef().getProcessDefCode()
				, out.getWfmExpressionDef().getProcessDefDetailCode()
				, out.getWfmExpressionDef().getExpressionDefCode());
	}

	public WfmExpressionDef delete(WfmExpressionDef expressionDef) {
		DeleteWfmExpressionDefInParam in = new DeleteWfmExpressionDefInParam();
		in.setWfmExpressionDef(expressionDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmExpressionDef(in);
		return expressionDef;
	}

	public WfmInformationSharerDef insert(boolean reSearch, WfmInformationSharerDef informationSharerDef) {
		InsertWfmInformationSharerDefInParam in = new InsertWfmInformationSharerDefInParam();
		in.setWfmInformationSharerDef(informationSharerDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmInformationSharerDefOutParam out = wf.insertWfmInformationSharerDef(in);

		if (!reSearch) return null;

		return getInformationSharerDef(
				out.getWfmInformationSharerDef().getCorporationCode()
				, out.getWfmInformationSharerDef().getProcessDefCode()
				, out.getWfmInformationSharerDef().getProcessDefDetailCode()
				, out.getWfmInformationSharerDef().getSeqNoInfoSharerDef());
	}

	public WfmInformationSharerDef update(boolean reSearch, WfmInformationSharerDef informationSharerDef) {
		UpdateWfmInformationSharerDefInParam in = new UpdateWfmInformationSharerDefInParam();
		in.setWfmInformationSharerDef(informationSharerDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmInformationSharerDefOutParam out = wf.updateWfmInformationSharerDef(in);

		if (!reSearch) return null;

		return getInformationSharerDef(
				out.getWfmInformationSharerDef().getCorporationCode()
				, out.getWfmInformationSharerDef().getProcessDefCode()
				, out.getWfmInformationSharerDef().getProcessDefDetailCode()
				, out.getWfmInformationSharerDef().getSeqNoInfoSharerDef());
	}

	public WfmInformationSharerDef delete(WfmInformationSharerDef informationSharerDef) {
		DeleteWfmInformationSharerDefInParam in = new DeleteWfmInformationSharerDefInParam();
		in.setWfmInformationSharerDef(informationSharerDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmInformationSharerDef(in);
		return informationSharerDef;
	}

	public WfmActivityDef insert(boolean reSearch, WfmActivityDef activityDef) {
		InsertWfmActivityDefInParam in = new InsertWfmActivityDefInParam();
		in.setWfmActivityDef(activityDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmActivityDefOutParam out = wf.insertWfmActivityDef(in);

		if (!reSearch) return null;

		return getActivityDef(
				out.getWfmActivityDef().getCorporationCode()
				, out.getWfmActivityDef().getProcessDefCode()
				, out.getWfmActivityDef().getProcessDefDetailCode()
				, out.getWfmActivityDef().getActivityDefCode());
	}

	public WfmActivityDef update(boolean reSearch, WfmActivityDef activityDef) {
		UpdateWfmActivityDefInParam in = new UpdateWfmActivityDefInParam();
		in.setWfmActivityDef(activityDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmActivityDefOutParam out = wf.updateWfmActivityDef(in);

		if (!reSearch) return null;

		return getActivityDef(
				out.getWfmActivityDef().getCorporationCode()
				, out.getWfmActivityDef().getProcessDefCode()
				, out.getWfmActivityDef().getProcessDefDetailCode()
				, out.getWfmActivityDef().getActivityDefCode());
	}

	public WfmActivityDef delete(WfmActivityDef activityDef) {
		DeleteWfmActivityDefInParam in = new DeleteWfmActivityDefInParam();
		in.setWfmActivityDef(activityDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmActivityDef(in);
		return activityDef;
	}

	public WfmAssignedDef insert(boolean reSearch, WfmAssignedDef assignedDef) {
		InsertWfmAssignedDefInParam in = new InsertWfmAssignedDefInParam();
		in.setWfmAssignedDef(assignedDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmAssignedDefOutParam out = wf.insertWfmAssignedDef(in);

		if (!reSearch) return null;

		return getAssignedDef(
				out.getWfmAssignedDef().getCorporationCode()
				, out.getWfmAssignedDef().getProcessDefCode()
				, out.getWfmAssignedDef().getProcessDefDetailCode()
				, out.getWfmAssignedDef().getActivityDefCode()
				, out.getWfmAssignedDef().getSeqNoAssignedDef());
	}

	public WfmAssignedDef update(boolean reSearch, WfmAssignedDef assignedDef) {
		UpdateWfmAssignedDefInParam in = new UpdateWfmAssignedDefInParam();
		in.setWfmAssignedDef(assignedDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmAssignedDefOutParam out = wf.updateWfmAssignedDef(in);

		if (!reSearch) return null;

		return getAssignedDef(
				out.getWfmAssignedDef().getCorporationCode()
				, out.getWfmAssignedDef().getProcessDefCode()
				, out.getWfmAssignedDef().getProcessDefDetailCode()
				, out.getWfmAssignedDef().getActivityDefCode()
				, out.getWfmAssignedDef().getSeqNoAssignedDef());
	}

	public WfmAssignedDef delete(WfmAssignedDef assignedDef) {
		DeleteWfmAssignedDefInParam in = new DeleteWfmAssignedDefInParam();
		in.setWfmAssignedDef(assignedDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmAssignedDef(in);
		return assignedDef;
	}

	public WfmChangeDef insert(boolean reSearch, WfmChangeDef changeDef) {
		InsertWfmChangeDefInParam in = new InsertWfmChangeDefInParam();
		in.setWfmChangeDef(changeDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmChangeDefOutParam out = wf.insertWfmChangeDef(in);

		if (!reSearch) return null;

		return getChangeDef(
				out.getWfmChangeDef().getCorporationCode()
				, out.getWfmChangeDef().getProcessDefCode()
				, out.getWfmChangeDef().getProcessDefDetailCode()
				, out.getWfmChangeDef().getActivityDefCode()
				, out.getWfmChangeDef().getSeqNoChangeDef());
	}

	public WfmChangeDef update(boolean reSearch, WfmChangeDef changeDef) {
		UpdateWfmChangeDefInParam in = new UpdateWfmChangeDefInParam();
		in.setWfmChangeDef(changeDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmChangeDefOutParam out = wf.updateWfmChangeDef(in);

		if (!reSearch) return null;

		return getChangeDef(
				out.getWfmChangeDef().getCorporationCode()
				, out.getWfmChangeDef().getProcessDefCode()
				, out.getWfmChangeDef().getProcessDefDetailCode()
				, out.getWfmChangeDef().getActivityDefCode()
				, out.getWfmChangeDef().getSeqNoChangeDef());
	}

	public WfmChangeDef delete(WfmChangeDef changeDef) {
		DeleteWfmChangeDefInParam in = new DeleteWfmChangeDefInParam();
		in.setWfmChangeDef(changeDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmChangeDef(in);
		return changeDef;
	}

	public WfmActionDef insert(boolean reSearch, WfmActionDef actionDef) {
		InsertWfmActionDefInParam in = new InsertWfmActionDefInParam();
		in.setWfmActionDef(actionDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmActionDefOutParam out = wf.insertWfmActionDef(in);

		if (!reSearch) return null;

		return getActionDef(
				out.getWfmActionDef().getCorporationCode()
				, out.getWfmActionDef().getProcessDefCode()
				, out.getWfmActionDef().getProcessDefDetailCode()
				, out.getWfmActionDef().getActivityDefCode()
				, out.getWfmActionDef().getSeqNoActionDef());
	}

	public WfmActionDef update(boolean reSearch, WfmActionDef actionDef) {
		UpdateWfmActionDefInParam in = new UpdateWfmActionDefInParam();
		in.setWfmActionDef(actionDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmActionDefOutParam out = wf.updateWfmActionDef(in);

		if (!reSearch) return null;

		return getActionDef(
				out.getWfmActionDef().getCorporationCode()
				, out.getWfmActionDef().getProcessDefCode()
				, out.getWfmActionDef().getProcessDefDetailCode()
				, out.getWfmActionDef().getActivityDefCode()
				, out.getWfmActionDef().getSeqNoActionDef());
	}

	public WfmActionDef delete(WfmActionDef actionDef) {
		DeleteWfmActionDefInParam in = new DeleteWfmActionDefInParam();
		in.setWfmActionDef(actionDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmActionDef(in);
		return actionDef;
	}

	public WfmConditionDef insert(boolean reSearch, WfmConditionDef conditionDef) {
		InsertWfmConditionDefInParam in = new InsertWfmConditionDefInParam();
		in.setWfmConditionDef(conditionDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmConditionDefOutParam out = wf.insertWfmConditionDef(in);

		if (!reSearch) return null;

		return getConditionDef(
				out.getWfmConditionDef().getCorporationCode()
				, out.getWfmConditionDef().getProcessDefCode()
				, out.getWfmConditionDef().getProcessDefDetailCode()
				, out.getWfmConditionDef().getActivityDefCode()
				, out.getWfmConditionDef().getSeqNoActionDef()
				, out.getWfmConditionDef().getSeqNoConditionDef());
	}

	public WfmConditionDef update(boolean reSearch, WfmConditionDef conditionDef) {
		UpdateWfmConditionDefInParam in = new UpdateWfmConditionDefInParam();
		in.setWfmConditionDef(conditionDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmConditionDefOutParam out = wf.updateWfmConditionDef(in);

		if (!reSearch) return null;

		return getConditionDef(
				out.getWfmConditionDef().getCorporationCode()
				, out.getWfmConditionDef().getProcessDefCode()
				, out.getWfmConditionDef().getProcessDefDetailCode()
				, out.getWfmConditionDef().getActivityDefCode()
				, out.getWfmConditionDef().getSeqNoActionDef()
				, out.getWfmConditionDef().getSeqNoConditionDef());
	}

	public WfmConditionDef delete(WfmConditionDef conditionDef) {
		DeleteWfmConditionDefInParam in = new DeleteWfmConditionDefInParam();
		in.setWfmConditionDef(conditionDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmConditionDef(in);
		return conditionDef;
	}

	public WfmFunctionDef insert(boolean reSearch, WfmFunctionDef functionDef) {
		InsertWfmFunctionDefInParam in = new InsertWfmFunctionDefInParam();
		in.setWfmFunctionDef(functionDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		InsertWfmFunctionDefOutParam out = wf.insertWfmFunctionDef(in);

		if (!reSearch) return null;

		return getFunctionDef(
				out.getWfmFunctionDef().getCorporationCode()
				, out.getWfmFunctionDef().getProcessDefCode()
				, out.getWfmFunctionDef().getProcessDefDetailCode()
				, out.getWfmFunctionDef().getActivityDefCode()
				, out.getWfmFunctionDef().getSeqNoActionDef()
				, out.getWfmFunctionDef().getSeqNoFunctionDef());
	}

	public WfmFunctionDef update(boolean reSearch, WfmFunctionDef functionDef) {
		UpdateWfmFunctionDefInParam in = new UpdateWfmFunctionDefInParam();
		in.setWfmFunctionDef(functionDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		UpdateWfmFunctionDefOutParam out = wf.updateWfmFunctionDef(in);

		if (!reSearch) return null;

		return getFunctionDef(
				out.getWfmFunctionDef().getCorporationCode()
				, out.getWfmFunctionDef().getProcessDefCode()
				, out.getWfmFunctionDef().getProcessDefDetailCode()
				, out.getWfmFunctionDef().getActivityDefCode()
				, out.getWfmFunctionDef().getSeqNoActionDef()
				, out.getWfmFunctionDef().getSeqNoFunctionDef());
	}

	public WfmFunctionDef delete(WfmFunctionDef functionDef) {
		DeleteWfmFunctionDefInParam in = new DeleteWfmFunctionDefInParam();
		in.setWfmFunctionDef(functionDef);
		in.setWfUserRole(sessionHolder.getWfUserRole());
		wf.deleteWfmFunctionDef(in);
		return functionDef;
	}

	public String getIncrementParallelActivityDefCode(
			String corporationCode
			, String processDefCode
			, String processDefDetailCode) {
		SearchIncrementActivityDefCodeInParam in = new SearchIncrementActivityDefCodeInParam();
		in.setCorporationCode(corporationCode);
		in.setProcessDefCode(processDefCode);
		in.setProcessDefDetailCode(processDefDetailCode);
		in.setActivityDefCode(ActivityDefCodePrefix.PARALLEL_S);
		in.setActivityType(ActivityType.PARALLEL_S);
		SearchIncrementActivityDefCodeOutParam out = wf.searchIncrementActivityDefCode(in);
		return out.getActivityDefCode();
	}

	public List<OptionItem> getDcIdOptions() {
		final List<OptionItem> optionItems = mm0020Repository.getMwmDc(sessionHolder.getLoginInfo().getLocaleCode())
				.stream()
				.filter(e -> (e.getDcId() < 100L))	// 文書管理用の表示条件定義マスタは除外（文書管理用の表示条件IDは101から始まるが100未満をWF用とする）
				.map(e -> new OptionItem(e.getDcId(), e.getDcName()))
				.collect(Collectors.toList());
		optionItems.add(0, OptionItem.EMPTY);
		return optionItems;
	}

	public List<WfmAssignRole> getAssignRoles(String corporationCode) {
		SearchWfmAssignRoleInParam in = new SearchWfmAssignRoleInParam();
		//検索条件
		in.setCorporationCode(corporationCode);
		in.setValidStartDate(now());
		in.setValidEndDate(now());
		in.setDeleteFlag(CodeMaster.DefaultFlag.OFF);
		//ソート順
		in.setOrderBy(new OrderBy[] {new OrderBy(OrderBy.ASC, "R.ASSIGN_ROLE_CODE")});
		return wf.searchWfmAssignRole(in).getAssignRoles();
	}

	public List<WfmChangeRole> getChangeRoles(String corporationCode) {
		SearchWfmChangeRoleInParam in = new SearchWfmChangeRoleInParam();
		//検索条件
		in.setCorporationCode(corporationCode);
		in.setValidStartDate(now());
		in.setValidEndDate(now());
		in.setDeleteFlag(CodeMaster.DefaultFlag.OFF);
		//ソート順
		in.setOrderBy(new OrderBy[] {new OrderBy(OrderBy.ASC, "A.CHANGE_ROLE_CODE")});
		return wf.searchWfmChangeRole(in).getChangeRoles();
	}

	public List<WfmAction> getActions(String corporationCode) {
		SearchWfmActionInParam in = new SearchWfmActionInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[] {new OrderBy(OrderBy.ASC, "A.ACTION_CODE")});
		return wf.searchWfmAction(in).getActions().stream().filter(e -> !ActionCode.WF_LINK.equals(e.getActionCode())).collect(Collectors.toList());
	}

	public List<WfmFunction> getFunctions(String corporationCode) {
		SearchWfmFunctionInParam in = new SearchWfmFunctionInParam();
		in.setCorporationCode(corporationCode);
		in.setDeleteFlag(DeleteFlag.OFF);
		in.setOrderBy(new OrderBy[] {new OrderBy(OrderBy.ASC, "F.FUNCTION_CODE")});
		return wf.searchWfmFunction(in).getFunctions();
	}

}
