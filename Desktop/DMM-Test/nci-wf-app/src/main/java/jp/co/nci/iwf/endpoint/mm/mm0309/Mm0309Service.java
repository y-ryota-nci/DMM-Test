package jp.co.nci.iwf.endpoint.mm.mm0309;

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
public class Mm0309Service extends BaseService implements CodeMaster, RouteSettingCodeBook {

	@Inject
	private RouteSettingService routeSetting;
	@Inject
	private WfmLookupService lookup;

	/**
	 * 初期化
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mm0309Response init(Mm0309Request req) {
		// 初期検索
		final Mm0309Response res = createResponse(Mm0309Response.class, req);

		res.functions = routeSetting.getFunctions(req.functionDef.getCorporationCode());
		res.executionTimingTypes = lookup.getOptionItems(false, LookupTypeCode.EXECUTION_TIMING_TYPE);
		res.functionDef = RouteSettingHelper.createDefaultFunctionDef(req.functionDef);

		res.success = true;
		return res;
	}

	/**
	 * 作成処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0309Response create(Mm0309Request req) {
		final Mm0309Response res = createResponse(Mm0309Response.class, req);
		res.functionDef = routeSetting.insert(true, req.functionDef);
		res.success = true;
		return res;
	}

}
