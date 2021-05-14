package jp.co.nci.iwf.endpoint.mm.mm0308;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.route.RouteSettingCodeBook;
import jp.co.nci.iwf.component.route.RouteSettingHelper;
import jp.co.nci.iwf.component.route.RouteSettingService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * アクション遷移先定義作成サービス
 */
@BizLogic
public class Mm0308Service extends BaseService implements CodeMaster, RouteSettingCodeBook {

	@Inject
	private RouteSettingService routeSetting;
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mm0308Response init(Mm0308Request req) {
		// 初期検索
		final Mm0308Response res = createResponse(Mm0308Response.class, req);

		res.businessProcessStatuses = lookup.getOptionItems(false, LookupTypeCode.BUSINESS_PROCESS_STATUS);
		res.businessActivityStatuses = lookup.getOptionItems(false, LookupTypeCode.BUSINESS_ACTIVITY_STATUS);

		res.activityDefs = routeSetting.getActivityDefs(req.activityDef.getCorporationCode(), req.activityDef.getProcessDefCode(), req.activityDef.getProcessDefDetailCode())
				.stream().filter(e -> {
					// 自分自身のアクティビティには遷移不可
					if (req.activityDef.getActivityDefCode().equals(e.getActivityDefCode())) {
						return false;
					}
					// 開始、並行待合開始の場合
					if ((ActivityType.START.equals(req.activityDef.getActivityType()) || ActivityType.PARALLEL_S.equals(req.activityDef.getActivityType()))
							&& !ActivityType.NORMAL.equals(e.getActivityType())) {
						return false;
					}
					// 通常の場合
					if (ActivityType.NORMAL.equals(req.activityDef.getActivityType())
							&& ActivityType.START.equals(e.getActivityType())) {
						return false;
					}
					// 並行待合終了の場合
					if (ActivityType.PARALLEL_E.equals(req.activityDef.getActivityType())
							&& (!ActivityType.NORMAL.equals(e.getActivityType()) && !ActivityType.END.equals(e.getActivityType()))) {
						return false;
					}
					// 業務連携系の場合
					if (ActivityType.NORMAL_WF_LINK.equals(e.getActivityType())
							|| ActivityType.WF_LINK_D_E.equals(e.getActivityType())
							|| ActivityType.WF_LINK_I_E.equals(e.getActivityType())) {
						return false;
					}
					return true;
				})
				.collect(Collectors.toList());
		res.expressionDefs = routeSetting.getExpressionDefs(req.activityDef.getCorporationCode(), req.activityDef.getProcessDefCode(), req.activityDef.getProcessDefDetailCode());
		res.expressionDefs.add(0, new WfmExpressionDef());
		res.conditionDef = RouteSettingHelper.createDefaultConditionDef(req.conditionDef);

		res.success = true;
		return res;
	}

	/**
	 * 作成処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0308Response create(Mm0308Request req) {
		final Mm0308Response res = createResponse(Mm0308Response.class, req);
		res.conditionDef = routeSetting.insert(true, req.conditionDef);
		res.success = true;
		return res;
	}

}
