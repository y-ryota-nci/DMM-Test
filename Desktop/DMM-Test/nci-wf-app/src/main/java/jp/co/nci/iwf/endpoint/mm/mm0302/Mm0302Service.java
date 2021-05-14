package jp.co.nci.iwf.endpoint.mm.mm0302;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.model.custom.WfmActionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActionTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignedDef;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignedTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeDef;
import jp.co.nci.integrated_workflow.model.custom.WfmConditionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmFunctionTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmVariableDef;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.route.RouteSettingCodeBook;
import jp.co.nci.iwf.component.route.RouteSettingHelper;
import jp.co.nci.iwf.component.route.RouteSettingService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ルート編集サービス
 */
@BizLogic
public class Mm0302Service extends BaseService implements CodeMaster, RouteSettingCodeBook {

	@Inject
	private WfmLookupService lookup;
	@Inject
	private RouteSettingService routeSetting;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0302InitResponse init(Mm0302InitRequest req) {
		// 起動パラメータのバリデーション
		validateInitParams(req);

		// 初期検索
		final Mm0302InitResponse res = createResponse(Mm0302InitResponse.class, req);
		res.templates = new Mm0302Templates();
		res.contents = new Mm0302Contents();

		// プロセス定義取得
		res.contents.processDef = routeSetting.getProcessDef(req.corporationCode, req.processDefCode, req.processDefDetailCode);

		if (!eq(res.contents.processDef.getTimestampUpdated().getTime(), req.timestampUpdated)) {
			res.success = false;
			res.addAlerts(i18n.getText(MessageCd.MSG0050));
			return res;
		}

		res.executionTermUnitTypes = lookup.getOptionItems(false, LookupTypeCode.EXECUTION_TERM_UNIT_TYPE);

		// プロセス用選択肢
		res.processOpeTypes = lookup.getOptionItems(false, LookupTypeCode.PROCESS_OPE_TYPE);

		// 比較条件式用選択肢
		res.operatorTypes = lookup.getOptionItems(false, LookupTypeCode.OPERATOR_TYPE);

		// アクティビティ用選択肢
		res.skipRuleTypes = lookup.getOptionItems(false, LookupTypeCode.SKIP_RULE_TYPE);
		res.activityKindTypes = lookup.getOptionItems(false, LookupTypeCode.ACTIVITY_KIND_TYPE);
		res.activityEndTypes = lookup.getOptionItems(false, LookupTypeCode.ACTIVITY_END_TYPE);
		res.activityTypes = lookup.getOptionItems(false, LookupTypeCode.ACTIVITY_TYPE);
		res.waitTypes = lookup.getOptionItems(false, LookupTypeCode.WAIT_TYPE);
		res.dcIds = routeSetting.getDcIdOptions();

		// 参加者用選択肢
		res.assignRoles = routeSetting.getAssignRoles(req.corporationCode);

		// 参加者用選択肢
		res.changeRoles = routeSetting.getChangeRoles(req.corporationCode);

		// アクション用選択肢
		res.actions = routeSetting.getActions(req.corporationCode);
		res.actionDefTypes = lookup.getOptionItems(false, LookupTypeCode.ACTION_DEF_TYPE);

		// 遷移先用選択肢
		res.businessProcessStatuses = lookup.getOptionItems(true, LookupTypeCode.BUSINESS_PROCESS_STATUS);
		res.businessActivityStatuses = lookup.getOptionItems(true, LookupTypeCode.BUSINESS_ACTIVITY_STATUS);

		// 機能用選択肢
		res.functions = routeSetting.getFunctions(req.corporationCode);
		res.executionTimingTypes = lookup.getOptionItems(false, LookupTypeCode.EXECUTION_TIMING_TYPE);

		// アクティビティテンプレート取得
		res.templates.activityTemplates = routeSetting.getActivityTemplates(req.corporationCode)
				.stream()
				.collect(Collectors.toMap(WfmActivityTemplate::getUniqueKey, at -> at, (f, s) -> f, LinkedHashMap::new));

		// テンプレートの会社コードを取得
		String templateCorporationCode = res.templates.activityTemplates.values().stream().findFirst().get().getCorporationCode();
		// 参加者テンプレート取得
		res.templates.assignedTemplates = routeSetting.getAssignedTemplates(templateCorporationCode)
				.stream()
				.collect(Collectors.toMap(WfmAssignedTemplate::getUniqueKey, at -> at, (f, s) -> f, LinkedHashMap::new));
		// アクションテンプレート取得
		res.templates.actionTemplates = routeSetting.getActionTemplates(templateCorporationCode)
				.stream()
				.collect(Collectors.toMap(WfmActionTemplate::getUniqueKey, at -> at, (f, s) -> f, LinkedHashMap::new));
		// アクション機能テンプレート取得
		res.templates.functionTemplates = routeSetting.getFunctionTemplates(templateCorporationCode)
				.stream()
				.collect(Collectors.toMap(WfmFunctionTemplate::getUniqueKey, at -> at, (f, s) -> f, LinkedHashMap::new));

		// アクティビティ定義取得
		res.contents.activityDefs = routeSetting.getActivityDefs(req.corporationCode, req.processDefCode, req.processDefDetailCode)
				.stream()
				.collect(Collectors.toMap(WfmActivityDef::getUniqueKey, ad -> ad, (f, s) -> f, LinkedHashMap::new));
		// 参加者定義取得
		res.contents.assignedDefs = routeSetting.getAssignedDefs(req.corporationCode, req.processDefCode, req.processDefDetailCode)
				.stream()
				.collect(Collectors.toMap(WfmAssignedDef::getUniqueKey, ad -> ad, (f, s) -> f, LinkedHashMap::new));
		// 参加者変更定義取得
		res.contents.changeDefs = routeSetting.getChangeDefs(req.corporationCode, req.processDefCode, req.processDefDetailCode)
				.stream()
				.collect(Collectors.toMap(WfmChangeDef::getUniqueKey, ad -> ad, (f, s) -> f, LinkedHashMap::new));
		// アクション定義取得
		res.contents.actionDefs = routeSetting.getActionDefs(req.corporationCode, req.processDefCode, req.processDefDetailCode)
				.stream()
				.collect(Collectors.toMap(WfmActionDef::getUniqueKey, ad -> ad, (f, s) -> f, LinkedHashMap::new));
		// アクション遷移先定義取得
		res.contents.conditionDefs = routeSetting.getConditionDefs(req.corporationCode, req.processDefCode, req.processDefDetailCode)
				.stream()
				.collect(Collectors.toMap(WfmConditionDef::getUniqueKey, cd -> cd, (f, s) -> f, LinkedHashMap::new));
		// アクション機能定義取得
		res.contents.functionDefs = routeSetting.getFunctionDefs(req.corporationCode, req.processDefCode, req.processDefDetailCode)
				.stream()
				.collect(Collectors.toMap(WfmFunctionDef::getUniqueKey, cd -> cd, (f, s) -> f, LinkedHashMap::new));
		// 比較条件式変数定義取得
		res.contents.variableDefs = routeSetting.getVariableDefs(req.corporationCode, req.processDefCode, req.processDefDetailCode)
				.stream()
				.filter(e -> isEmpty(e.getBusinessInfoNameId()) || (ValidFlag.VALID.equals(e.getValidFlag()) && (BusinessInfoType.WF_VARIABLE.equals(e.getBusinessInfoType()) || BusinessInfoType.BOTH.equals(e.getBusinessInfoType()))))
				.collect(Collectors.toMap(WfmVariableDef::getUniqueKey, ed -> ed, (f, s) -> f, LinkedHashMap::new));
		// 比較条件式定義取得
		res.contents.expressionDefs = routeSetting.getExpressionDefs(req.corporationCode, req.processDefCode, req.processDefDetailCode)
				.stream()
				.collect(Collectors.toMap(WfmExpressionDef::getUniqueKey, ed -> ed, (f, s) -> f, LinkedHashMap::new));

		res.success = true;
		return res;
	}

	private void validateInitParams(Mm0302InitRequest req) {
		// 新規以外
		if (isEmpty(req.corporationCode))
			throw new BadRequestException("企業コードが未指定です");
		if (req.processDefCode == null)
			throw new BadRequestException("プロセス定義コードが未指定です");
		if (req.processDefDetailCode == null)
			throw new BadRequestException("プロセス定義コード枝番が未指定です");
		if (req.timestampUpdated == null)
			throw new BadRequestException("更新日時が未指定です");
	}

	/**
	 * プロセス定義更新
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ProcessDefResponse update(Mm0302ProcessDefRequest req) {
		final Mm0302ProcessDefResponse res = createResponse(Mm0302ProcessDefResponse.class, req);
		res.processDef = routeSetting.update(true, req.processDef);
		res.success = true;
		return res;
	}

	/**
	 * プロセス定義削除
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ProcessDefResponse remove(Mm0302ProcessDefRequest req) {
		final Mm0302ProcessDefResponse res = createResponse(Mm0302ProcessDefResponse.class, req);
		res.processDef = routeSetting.delete(req.processDef);
		res.success = true;
		return res;
	}

	/**
	 * 比較条件式定義更新
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ExpressionDefResponse update(Mm0302ExpressionDefRequest req) {
		final Mm0302ExpressionDefResponse res = createResponse(Mm0302ExpressionDefResponse.class, req);
		res.expressionDef = routeSetting.update(true, req.expressionDef);

		// 名称変更に伴いアクション遷移先定義再取得
		res.conditionDefs = routeSetting.getConditionDefs(req.expressionDef.getCorporationCode(), req.expressionDef.getProcessDefCode(), req.expressionDef.getProcessDefDetailCode())
				.stream()
				.filter(e -> req.conditionDefs.stream().anyMatch(c -> c.getUniqueKey().equals(e.getUniqueKey())))
				.collect(Collectors.toList());
		res.success = true;
		return res;
	}

	/**
	 * 比較条件式定義削除
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ExpressionDefResponse remove(Mm0302ExpressionDefRequest req) {
		final Mm0302ExpressionDefResponse res = createResponse(Mm0302ExpressionDefResponse.class, req);
		res.expressionDef = routeSetting.delete(req.expressionDef);

		// 遷移先定義削除
		List<WfmConditionDef> updateConditionDefs = new ArrayList<>();
		req.conditionDefs.forEach(cd -> {
			cd.setExpressionDefCode(null);
			updateConditionDefs.add(routeSetting.update(true, cd));
		});
		res.conditionDefs = updateConditionDefs;

		// 参加者定義削除
		List<WfmAssignedDef> updateAssignedDefs = new ArrayList<>();
		req.assignedDefs.forEach(ad -> {
			ad.setExpressionDefCode(null);
			updateAssignedDefs.add(routeSetting.update(true, ad));
		});
		res.assignedDefs = updateAssignedDefs;

		res.success = true;
		return res;
	}

	/**
	 * アクティビティ定義追加
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ActivityDefResponse create(Mm0302ActivityDefRequest req) {
		final Mm0302ActivityDefResponse res = createResponse(Mm0302ActivityDefResponse.class, req);

		final List<WfmActivityDef> activityDefs = new ArrayList<>();
		final List<WfmAssignedDef> assignedDefs = new ArrayList<>();
		final List<WfmActionDef> actionDefs = new ArrayList<>();
		final List<WfmFunctionDef> functionDefs = new ArrayList<>();

		final String corporationCode = req.activityDef.getCorporationCode();
		final String processDefCode = req.activityDef.getProcessDefCode();
		final String processDefDetailCode = req.activityDef.getProcessDefDetailCode();
		// アクティビティテンプレートが複数ある場合は平行アクティビティ
		final String activityDefCode = req.activityTemplates.size() == 2 ? routeSetting.getIncrementParallelActivityDefCode(corporationCode, processDefCode, processDefDetailCode) : null;

		req.activityTemplates
		.stream()
		.forEach(temp -> {
			final WfmActivityDef activityDef = new WfmActivityDef();
			// テンプレートをコピー
			copyProperties(temp, activityDef);
			// 入力値をコピー
			activityDef.setCorporationCode(corporationCode);
			activityDef.setProcessDefCode(processDefCode);
			activityDef.setProcessDefDetailCode(processDefDetailCode);
			activityDef.setValidStartDate(req.activityDef.getValidStartDate());
			activityDef.setValidEndDate(req.activityDef.getValidEndDate());
			activityDef.setXcoordinate(req.activityDef.getXcoordinate());
			activityDef.setYcoordinate(req.activityDef.getYcoordinate());

			if (ActivityTemplateCode.PARALLEL_S.equals(temp.getActivityTemplateCode())) {
				activityDef.setActivityDefCode(ActivityDefCodePrefix.PARALLEL_S + activityDefCode);
			} else if (ActivityTemplateCode.PARALLEL_E.equals(temp.getActivityTemplateCode())) {
				activityDef.setActivityDefCode(ActivityDefCodePrefix.PARALLEL_E + activityDefCode);
				activityDef.setActivityDefCodeParallelS(ActivityDefCodePrefix.PARALLEL_S + activityDefCode);
				activityDef.setXcoordinate(activityDef.getXcoordinate() +  + 80l);
			}
			activityDef.setActivityDefName(temp.getActivityTemplateName());

			final WfmActivityDef result = routeSetting.insert(true, activityDef);
			activityDefs.add(result);

			// 参加者定義の追加
			req.assignedTemplates
			.stream()
			.filter(a -> a.getActivityTemplateCode().equals(temp.getActivityTemplateCode()))
			.forEach(a -> {
				final WfmAssignedDef assignedDef = new WfmAssignedDef();
				copyProperties(a, assignedDef);
				assignedDef.setCorporationCode(result.getCorporationCode());
				assignedDef.setProcessDefCode(result.getProcessDefCode());
				assignedDef.setProcessDefDetailCode(result.getProcessDefDetailCode());
				assignedDef.setActivityDefCode(result.getActivityDefCode());
				assignedDefs.add(routeSetting.insert(true, assignedDef));
			});

			// アクション定義の追加
			req.actionTemplates
			.stream()
			.filter(a -> a.getActivityTemplateCode().equals(temp.getActivityTemplateCode()))
			.forEach(a -> {
				final WfmActionDef actionDef = new WfmActionDef();
				copyProperties(a, actionDef);
				actionDef.setCorporationCode(result.getCorporationCode());
				actionDef.setProcessDefCode(result.getProcessDefCode());
				actionDef.setProcessDefDetailCode(result.getProcessDefDetailCode());
				actionDef.setActivityDefCode(result.getActivityDefCode());
				actionDefs.add(routeSetting.insert(true, actionDef));

				// アクション機能定義の追加
				req.functionTemplates
				.stream()
				.filter(f -> a.getActivityTemplateCode().equals(f.getActivityTemplateCode()) && a.getSeqNoActionDef().equals(f.getSeqNoActionDef()))
				.forEach(f -> {
					final WfmFunctionDef functionDef = new WfmFunctionDef();
					copyProperties(f, functionDef);
					functionDef.setCorporationCode(result.getCorporationCode());
					functionDef.setProcessDefCode(result.getProcessDefCode());
					functionDef.setProcessDefDetailCode(result.getProcessDefDetailCode());
					functionDef.setActivityDefCode(result.getActivityDefCode());
					functionDefs.add(routeSetting.insert(true, functionDef));
				});
			});
		});

//		if (ActivityType.PARALLEL_S.equals(req.activityDef.getActivityType())) {
//			final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
//			List<OptionItem> activityTypes = lookup.getOptionItems(false, corporationCode, LookupTypeCode.ACTIVITY_TYPE, ActivityType.PARALLEL_S, ActivityType.PARALLEL_E);
//			String activityDefCode = routeSetting.getIncrementParallelActivityDefCode(corporationCode, req.activityDef.getProcessDefCode(), req.activityDef.getProcessDefDetailCode());
//			for (OptionItem item : activityTypes) {
//				if (ActivityType.PARALLEL_S.equals(item.getValue())) {
//					activityDefs.add(routeSetting.insert(true, RouteSettingHelper.createParallelStartActivityDef(req.activityDef, ActivityDefCodePrefix.PARALLEL_S + activityDefCode, item.getLabel() + activityDefCode.substring(8))));
//				} else if (ActivityType.PARALLEL_E.equals(item.getValue())) {
//					activityDefs.add(routeSetting.insert(true, RouteSettingHelper.createParallelEndActivityDef(req.activityDef, ActivityDefCodePrefix.PARALLEL_E + activityDefCode, item.getLabel() + activityDefCode.substring(8))));
//				}
//			}
//		} else {
//			activityDefs.add(routeSetting.insert(true, RouteSettingHelper.createDefaultActivityDef(req.activityDef)));
//		}
//
//		for (WfmActivityDef activityDef : activityDefs) {
//			actionDefs.add(routeSetting.insert(true, RouteSettingHelper.createDefaultActionDef(activityDef)));
//		}

		res.activityDefs = activityDefs.stream().collect(Collectors.toMap(WfmActivityDef::getUniqueKey, ad -> ad, (f, s) -> f, LinkedHashMap::new));
		res.assignedDefs = assignedDefs.stream().collect(Collectors.toMap(WfmAssignedDef::getUniqueKey, ad -> ad, (f, s) -> f, LinkedHashMap::new));
		res.actionDefs = actionDefs.stream().collect(Collectors.toMap(WfmActionDef::getUniqueKey, ad -> ad, (f, s) -> f, LinkedHashMap::new));
		res.functionDefs = functionDefs.stream().collect(Collectors.toMap(WfmFunctionDef::getUniqueKey, ad -> ad, (f, s) -> f, LinkedHashMap::new));

		res.success = true;
		return res;
	}

	/**
	 * アクティビティ定義更新
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ActivityDefResponse update(Mm0302ActivityDefRequest req) {
		final Mm0302ActivityDefResponse res = createResponse(Mm0302ActivityDefResponse.class, req);
		List<WfmActivityDef> activityDefs = new ArrayList<>();
		activityDefs.add(routeSetting.update(true, req.activityDef));
		res.activityDefs = activityDefs.stream().collect(Collectors.toMap(WfmActivityDef::getUniqueKey, ad -> ad));
		res.success = true;
		return res;
	}

	/**
	 * アクティビティ定義削除
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ActivityDefResponse remove(Mm0302ActivityDefRequest req) {
		final Mm0302ActivityDefResponse res = createResponse(Mm0302ActivityDefResponse.class, req);

		// 機能定義削除
		List<WfmFunctionDef> removeFunctionDefs = new ArrayList<>();
		req.functionDefs.forEach(fd -> removeFunctionDefs.add(routeSetting.delete(fd)));
		res.functionDefs = removeFunctionDefs.stream().collect(Collectors.toMap(WfmFunctionDef::getUniqueKey, cd -> cd));
		// 遷移先定義削除
		List<WfmConditionDef> removeConditionDefs = new ArrayList<>();
		req.conditionDefs.forEach(cd -> removeConditionDefs.add(routeSetting.delete(cd)));
		res.conditionDefs = removeConditionDefs.stream().collect(Collectors.toMap(WfmConditionDef::getUniqueKey, cd -> cd));
		// アクション定義削除
		List<WfmActionDef> removeActionDefs = new ArrayList<>();
		req.actionDefs.forEach(ad -> removeActionDefs.add(routeSetting.delete(ad)));
		res.actionDefs = removeActionDefs.stream().collect(Collectors.toMap(WfmActionDef::getUniqueKey, ad -> ad));
		// 参加者定義削除
		List<WfmAssignedDef> removeAssignedDefs = new ArrayList<>();
		req.assignedDefs.forEach(ad -> removeAssignedDefs.add(routeSetting.delete(ad)));
		res.assignedDefs = removeAssignedDefs.stream().collect(Collectors.toMap(WfmAssignedDef::getUniqueKey, ad -> ad));
		// 参加者定義削除
		List<WfmChangeDef> removeChangeDefs = new ArrayList<>();
		req.changeDefs.forEach(ad -> removeChangeDefs.add(routeSetting.delete(ad)));
		res.changeDefs = removeChangeDefs.stream().collect(Collectors.toMap(WfmChangeDef::getUniqueKey, ad -> ad));
		// アクティビティ定義削除
		List<WfmActivityDef> removeActivityDefs = new ArrayList<>();
		req.activityDefs.forEach(ad -> removeActivityDefs.add(routeSetting.delete(ad)));
		res.activityDefs = removeActivityDefs.stream().collect(Collectors.toMap(WfmActivityDef::getUniqueKey, ad -> ad));

		res.success = true;
		return res;
	}

	/**
	 * 参加者定義更新
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302AssignedDefResponse update(Mm0302AssignedDefRequest req) {
		final Mm0302AssignedDefResponse res = createResponse(Mm0302AssignedDefResponse.class, req);
		res.assignedDef = routeSetting.update(true, req.assignedDef);
		res.success = true;
		return res;
	}

	/**
	 * 参加者定義削除
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302AssignedDefResponse remove(Mm0302AssignedDefRequest req) {
		final Mm0302AssignedDefResponse res = createResponse(Mm0302AssignedDefResponse.class, req);
		res.assignedDef = routeSetting.delete(req.assignedDef);
		res.success = true;
		return res;
	}

	/**
	 * 参加者変更定義更新
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ChangeDefResponse update(Mm0302ChangeDefRequest req) {
		final Mm0302ChangeDefResponse res = createResponse(Mm0302ChangeDefResponse.class, req);
		res.changeDef = routeSetting.update(true, req.changeDef);
		res.success = true;
		return res;
	}

	/**
	 * 参加者変更定義削除
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ChangeDefResponse remove(Mm0302ChangeDefRequest req) {
		final Mm0302ChangeDefResponse res = createResponse(Mm0302ChangeDefResponse.class, req);
		res.changeDef = routeSetting.delete(req.changeDef);
		res.success = true;
		return res;
	}

	/**
	 * アクション定義更新
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ActionDefResponse update(Mm0302ActionDefRequest req) {
		final Mm0302ActionDefResponse res = createResponse(Mm0302ActionDefResponse.class, req);
		List<WfmActionDef> updateActionDefs = new ArrayList<>();
		req.actionDefs.forEach(ad -> updateActionDefs.add(routeSetting.update(true, ad)));
		res.actionDefs = updateActionDefs;
		res.success = true;
		return res;
	}

	/**
	 * アクション定義削除
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ActionDefResponse remove(Mm0302ActionDefRequest req) {
		final Mm0302ActionDefResponse res = createResponse(Mm0302ActionDefResponse.class, req);

		// 機能定義削除
		List<WfmFunctionDef> removeFunctionDefs = new ArrayList<>();
		req.functionDefs.forEach(fd -> removeFunctionDefs.add(routeSetting.delete(fd)));
		res.functionDefs = removeFunctionDefs.stream().collect(Collectors.toMap(WfmFunctionDef::getUniqueKey, cd -> cd));
		// 遷移先定義削除
		List<WfmConditionDef> removeConditionDefs = new ArrayList<>();
		req.conditionDefs.forEach(cd -> removeConditionDefs.add(routeSetting.delete(cd)));
		res.conditionDefs = removeConditionDefs.stream().collect(Collectors.toMap(WfmConditionDef::getUniqueKey, cd -> cd));
		res.actionDef = routeSetting.delete(req.actionDef);
		res.success = true;
		return res;
	}

	/**
	 * アクション遷移先定義追加
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ConditionDefResponse create(Mm0302ConditionDefRequest req) {
		final Mm0302ConditionDefResponse res = createResponse(Mm0302ConditionDefResponse.class, req);
		if (isEmpty(req.conditionDef.getBusinessProcessStatus())) {
			res.conditionDef = routeSetting.insert(true, RouteSettingHelper.createDefaultConditionDef(req.conditionDef));
		} else {
			res.conditionDef = routeSetting.insert(true, req.conditionDef);
		}

		res.success = true;
		return res;
	}

	/**
	 * アクション遷移先定義更新
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ConditionDefResponse update(Mm0302ConditionDefRequest req) {
		final Mm0302ConditionDefResponse res = createResponse(Mm0302ConditionDefResponse.class, req);
		res.conditionDef = routeSetting.update(true, req.conditionDef);
		res.success = true;
		return res;
	}

	/**
	 * アクション遷移先定義削除
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302ConditionDefResponse remove(Mm0302ConditionDefRequest req) {
		final Mm0302ConditionDefResponse res = createResponse(Mm0302ConditionDefResponse.class, req);
		res.conditionDef = routeSetting.delete(req.conditionDef);
		res.success = true;
		return res;
	}

	/**
	 * アクション機能定義更新
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302FunctionDefResponse update(Mm0302FunctionDefRequest req) {
		final Mm0302FunctionDefResponse res = createResponse(Mm0302FunctionDefResponse.class, req);
		res.functionDef = routeSetting.update(true, req.functionDef);
		res.success = true;
		return res;
	}

	/**
	 * アクション機能定義削除
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0302FunctionDefResponse remove(Mm0302FunctionDefRequest req) {
		final Mm0302FunctionDefResponse res = createResponse(Mm0302FunctionDefResponse.class, req);
		res.functionDef = routeSetting.delete(req.functionDef);
		res.success = true;
		return res;
	}

}
