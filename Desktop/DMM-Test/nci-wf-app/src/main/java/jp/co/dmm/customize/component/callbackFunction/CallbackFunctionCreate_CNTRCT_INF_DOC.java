package jp.co.dmm.customize.component.callbackFunction;

import javax.transaction.Transactional;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

/**
 * 新規契約申請、新規契約申請（経常支払）の承認後に呼び出されるコールバックファンクション(文書管理連携のみ)。
 * 【変更申請は対象ではない】
 */
public class CallbackFunctionCreate_CNTRCT_INF_DOC extends BaseCoopWf2Doc4DmmCallbackFunction {
	@Transactional
	@Override
	public void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

//		// 契約部長承認＆利用雛形（変更し）以外の場合は処理をスキップ
//		String targetType = ctx.runtimeMap.get("RAD0010").getValue();
//
//		if ("0000000003".equals(contents.activityDefCode) && !"3".equals(targetType)) {
//			return;
//		}

		// WF→文書管理への連携処理を実行
		super.execute(param, result, actionType, contents, ctx, functionDef);
	}

}
