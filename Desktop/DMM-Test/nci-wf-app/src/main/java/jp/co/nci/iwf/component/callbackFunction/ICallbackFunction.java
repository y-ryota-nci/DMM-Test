package jp.co.nci.iwf.component.callbackFunction;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;

/**
 * コールバックファンクション用インターフェース
 */
public interface ICallbackFunction {

	/**
	 * 業務機能処理実行.
	 *
	 * @param param 引継パラメータクラス
	 * @param result API結果パラメータクラス
	 * @param actionType アクション種別
	 * @param contents 申請・承認画面コンテンツ情報
	 * @param ctx デザイナーコンテキスト
	 * @param functionDef アクション機能定義
	 */
	void execute(InParamCallbackBase param, OutParamCallbackBase result, String actionType, Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef);
}
