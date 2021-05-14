package jp.co.nci.iwf.endpoint.mm.mm0307;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.WfmLookupService;
import jp.co.nci.iwf.component.route.RouteSettingCodeBook;
import jp.co.nci.iwf.component.route.RouteSettingHelper;
import jp.co.nci.iwf.component.route.RouteSettingService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * アクション定義作成サービス
 */
@BizLogic
public class Mm0307Service extends BaseService implements CodeMaster, RouteSettingCodeBook {

	@Inject
	private RouteSettingService routeSetting;
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mm0307Response init(Mm0307Request req) {
		// 初期検索
		final Mm0307Response res = createResponse(Mm0307Response.class, req);

		res.actions = routeSetting.getActions(req.actionDef.getCorporationCode());
		res.actionDefTypes = lookup.getOptionItems(false, LookupTypeCode.ACTION_DEF_TYPE);
		res.actionDef = RouteSettingHelper.createDefaultActionDef(req.actionDef);

		res.success = true;
		return res;
	}

	/**
	 * 作成処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0307Response create(Mm0307Request req) {
		final Mm0307Response res = createResponse(Mm0307Response.class, req);
		res.actionDef = routeSetting.insert(true, req.actionDef);
		res.success = true;
		return res;
	}

}
