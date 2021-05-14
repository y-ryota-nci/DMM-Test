package jp.co.nci.iwf.endpoint.mm.mm0311;

import java.util.stream.Collectors;

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
 * 比較条件式定義作成サービス
 */
@BizLogic
public class Mm0311Service extends BaseService implements CodeMaster, RouteSettingCodeBook {

	@Inject
	private WfmLookupService lookup;
	@Inject
	private RouteSettingService routeSetting;

	/**
	 * 初期化
	 * @param req リクエスト
	 * @return レスポンス
	 */
	public Mm0311Response init(Mm0311Request req) {
		// 初期検索
		final Mm0311Response res = createResponse(Mm0311Response.class, req);

		res.operatorTypes = lookup.getOptionItems(false, LookupTypeCode.OPERATOR_TYPE);
		res.variableDefs = routeSetting.getVariableDefs(req.expressionDef.getCorporationCode(), req.expressionDef.getProcessDefCode(), req.expressionDef.getProcessDefDetailCode())
				.stream()
				.filter(e -> isEmpty(e.getBusinessInfoNameId()) || (ValidFlag.VALID.equals(e.getValidFlag()) && (BusinessInfoType.WF_VARIABLE.equals(e.getBusinessInfoType()) || BusinessInfoType.BOTH.equals(e.getBusinessInfoType()))))
				.collect(Collectors.toList());

		res.expressionDefs = routeSetting.getExpressionDefs(req.expressionDef.getCorporationCode(), req.expressionDef.getProcessDefCode(), req.expressionDef.getProcessDefDetailCode());
		res.expressionDef = RouteSettingHelper.createDefaultExpressionDef(req.expressionDef);

		res.success = true;
		return res;
	}

	/**
	 * 作成処理
	 * @param req リクエスト
	 * @return レスポンス
	 */
	@Transactional
	public Mm0311Response create(Mm0311Request req) {
		final Mm0311Response res = createResponse(Mm0311Response.class, req);
		res.expressionDef = routeSetting.insert(true, req.expressionDef);
		res.success = true;
		return res;
	}

}
