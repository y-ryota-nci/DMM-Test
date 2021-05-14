package jp.co.nci.iwf.endpoint.mm.mm0305;

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
 * 参加者定義作成サービス
 */
@BizLogic
public class Mm0305Service extends BaseService implements CodeMaster, RouteSettingCodeBook {

	@Inject
	private RouteSettingService routeSetting;

	/**
	 * 初期化
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mm0305Response init(Mm0305Request req) {
		// 初期検索
		final Mm0305Response res = createResponse(Mm0305Response.class, req);

		res.assignRoles = routeSetting.getAssignRoles(req.assignedDef.getCorporationCode());
		res.expressionDefs = routeSetting.getExpressionDefs(req.assignedDef.getCorporationCode(), req.assignedDef.getProcessDefCode(), req.assignedDef.getProcessDefDetailCode());
		res.expressionDefs.add(0, new WfmExpressionDef());
		res.assignedDef = RouteSettingHelper.createDefaultAssignedDef(req.assignedDef);

		res.success = true;
		return res;
	}

	/**
	 * 作成処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0305Response create(Mm0305Request req) {
		final Mm0305Response res = createResponse(Mm0305Response.class, req);
		res.assignedDef = routeSetting.insert(true, req.assignedDef);
		res.success = true;
		return res;
	}

}
