package jp.co.nci.iwf.endpoint.mm.mm0301;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.common.util.UniqueKeyUtil;
import jp.co.nci.integrated_workflow.model.custom.WfmActionDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActionTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityDef;
import jp.co.nci.integrated_workflow.model.custom.WfmActivityTemplate;
import jp.co.nci.integrated_workflow.model.custom.WfmProcessDef;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.route.RouteSettingCodeBook;
import jp.co.nci.iwf.component.route.RouteSettingHelper;
import jp.co.nci.iwf.component.route.RouteSettingService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * ルート作成サービス
 */
@BizLogic
public class Mm0301Service extends BaseService implements CodeMaster, RouteSettingCodeBook {

	@Inject
	private RouteSettingService routeSetting;
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Mm0301Response init(Mm0301Request req) {
		// 初期検索
		final Mm0301Response res = createResponse(Mm0301Response.class, req);
		res.processDefs = routeSetting.getProcessDefs(sessionHolder.getLoginInfo().getCorporationCode());
		res.processDefs.add(0, new WfmProcessDef());
		res.executionTermUnitTypes = lookup.getOptionItems(false, LookupTypeCode.EXECUTION_TERM_UNIT_TYPE);
		res.processDef = RouteSettingHelper.createDefaultProcessDef(sessionHolder.getLoginInfo().getCorporationCode());
		res.success = true;
		return res;
	}

	/**
	 * 登録処理
	 * @param req
	 * @return
	 */
	@Transactional
	public Mm0301Response create(Mm0301Request req) {
		final Mm0301Response res = createResponse(Mm0301Response.class, req);
		if (isEmpty(req.sourceRoute)) {
			res.processDef = insert(req);
		} else {
			res.processDef = copy(req);
		}
		res.success = true;
		return res;
	}

	/**
	 * 登録
	 * @param req
	 * @return
	 */
	private WfmProcessDef insert(Mm0301Request req) {
		// プロセス定義の登録
		final WfmProcessDef processDef = routeSetting.insert(true, req.processDef);

		// 開始、終了アクティビティの登録
		final String corporationCode = sessionHolder.getLoginInfo().getCorporationCode();
		// アクティビティテンプレート取得
		final List<WfmActivityTemplate> activityTemplates = routeSetting.getActivityTemplates(corporationCode);
		// テンプレートの会社コードを取得
		final String templateCorporationCode = activityTemplates.stream().findFirst().get().getCorporationCode();
		// アクションテンプレート取得
		final List<WfmActionTemplate> actionTemplates = routeSetting.getActionTemplates(templateCorporationCode);

		for (WfmActivityTemplate template : activityTemplates) {
			if (!ActivityTemplateCode.START.equals(template.getActivityTemplateCode())
					&& !ActivityTemplateCode.END.equals(template.getActivityTemplateCode())) {
				continue;
			}
			WfmActivityDef activityDef = new WfmActivityDef();
			copyProperties(template, activityDef);

			activityDef.setCorporationCode(processDef.getCorporationCode());
			activityDef.setProcessDefCode(processDef.getProcessDefCode());
			activityDef.setProcessDefDetailCode(processDef.getProcessDefDetailCode());
			activityDef.setActivityDefCode(template.getActivityTemplateCode());
			activityDef.setActivityDefName(template.getActivityTemplateName());
			activityDef.setValidStartDate(processDef.getValidStartDate());
			activityDef.setValidEndDate(processDef.getValidEndDate());
			// 位置情報
			if (ActivityTemplateCode.START.equals(template.getActivityTemplateCode())) {
				activityDef.setXcoordinate(ActivityDef.START_XCOORDINATE);
				activityDef.setYcoordinate(ActivityDef.START_YCOORDINATE);
			} else if (ActivityTemplateCode.END.equals(template.getActivityTemplateCode())) {
				activityDef.setXcoordinate(ActivityDef.END_XCOORDINATE);
				activityDef.setYcoordinate(ActivityDef.END_YCOORDINATE);
			}
			final WfmActivityDef result = routeSetting.insert(true, activityDef);

			actionTemplates.stream().filter(t -> t.getActivityTemplateCode().equals(template.getActivityTemplateCode())).forEach(t -> {
				WfmActionDef actionDef = new WfmActionDef();
				copyProperties(t, actionDef);
				actionDef.setCorporationCode(processDef.getCorporationCode());
				actionDef.setProcessDefCode(processDef.getProcessDefCode());
				actionDef.setProcessDefDetailCode(processDef.getProcessDefDetailCode());
				actionDef.setActivityDefCode(result.getActivityDefCode());
				routeSetting.insert(false, actionDef);
			});

		}

		// 比較条件式変数定義の登録
		routeSetting.insertDefaultVariableDef(processDef);

		return processDef;
	}

	/**
	 * コピー
	 * @param req
	 * @return
	 */
	private WfmProcessDef copy(Mm0301Request req) {
		String[] keys = UniqueKeyUtil.parseUniqueKey(req.sourceRoute);
		if (keys.length == 3) {
			// コピー元
			WfmProcessDef from = new WfmProcessDef();
			from.setCorporationCode(keys[0]);
			from.setProcessDefCode(keys[1]);
			from.setProcessDefDetailCode(keys[2]);

			// コピー先
			WfmProcessDef to = req.processDef;
			return routeSetting.copy(true, from, to);
		} else {
			throw new BadRequestException("コピー元プロセス定義が指定が非適切です");
		}
	}
}
