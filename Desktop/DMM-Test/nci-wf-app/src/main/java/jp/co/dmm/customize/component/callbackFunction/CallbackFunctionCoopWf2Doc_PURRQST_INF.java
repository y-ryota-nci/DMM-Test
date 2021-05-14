package jp.co.dmm.customize.component.callbackFunction;

import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

/**
 * 購入依頼の申請情報を文書管理側へ連携するためのコールバックファンクション.
 * 現状、購入依頼には変更申請は存在しないとのこと
 */
public class CallbackFunctionCoopWf2Doc_PURRQST_INF extends BaseCoopWf2Doc4DmmCallbackFunction {

	@Transactional
	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		// WF→文書管理への連携処理を実行
		super.execute(param, result, actionType, contents, ctx, functionDef);
	}
}
