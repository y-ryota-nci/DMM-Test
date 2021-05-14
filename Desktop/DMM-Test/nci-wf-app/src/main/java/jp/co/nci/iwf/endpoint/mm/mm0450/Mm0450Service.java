package jp.co.nci.iwf.endpoint.mm.mm0450;

import javax.inject.Inject;
import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.common.CodeMaster;
import jp.co.nci.integrated_workflow.model.custom.WfmExpressionDef;
import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.component.route.RouteSettingCodeBook;
import jp.co.nci.iwf.component.route.RouteSettingHelper;
import jp.co.nci.iwf.component.route.RouteSettingService;
import jp.co.nci.iwf.jersey.base.BaseService;

/**
 * 参加者変更定義作成サービス
 */
@BizLogic
public class Mm0450Service extends BaseService implements CodeMaster, RouteSettingCodeBook {

	@Inject
	private RouteSettingService routeSetting;

	/**
	 * 初期化
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mm0450Response init(Mm0450Request req) {
		// 初期検索
		final Mm0450Response res = createResponse(Mm0450Response.class, req);

		res.changeRoles = routeSetting.getChangeRoles(req.changeDef.getCorporationCode());
		res.expressionDefs = routeSetting.getExpressionDefs(req.changeDef.getCorporationCode(), req.changeDef.getProcessDefCode(), req.changeDef.getProcessDefDetailCode());
		res.expressionDefs.add(0, new WfmExpressionDef());
		res.changeDef = RouteSettingHelper.createDefaultChangeDef(req.changeDef);

		res.success = true;
		return res;
	}

	/**
	 * 作成処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0450Response create(Mm0450Request req) {
		final Mm0450Response res = createResponse(Mm0450Response.class, req);
		res.changeDef = routeSetting.insert(true, req.changeDef);
		res.success = true;
		return res;
	}

}
