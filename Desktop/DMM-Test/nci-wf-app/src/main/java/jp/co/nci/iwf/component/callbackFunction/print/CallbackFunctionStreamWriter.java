package jp.co.nci.iwf.component.callbackFunction.print;

import java.io.OutputStream;
import java.util.Map;

import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.common.CodeMaster.ReturnCode;
import jp.co.nci.integrated_workflow.common.WfException;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.callbackFunction.BaseCallbackFunction;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.service.screenCustom.IScreenCustom;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310Contents;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.jersey.exception.AlreadyUpdatedException;
import jp.co.nci.iwf.jersey.exception.InvalidUserInputException;

/**
 * 画面カスタムクラス経由でコンテンツをストリームへ書き込むコールバックファンクション。
 * 主に帳票ダウンロードに使用される。
 */
public class CallbackFunctionStreamWriter extends BaseCallbackFunction {

	/**
	 * 業務機能処理実行.
	 *
	 * @param in 引継パラメータクラス
	 * @param out API結果パラメータクラス
	 * @param actionType アクション種別
	 * @param contents 申請・承認画面コンテンツ情報
	 * @param ctx デザイナーコンテキスト
	 * @param functionDef アクション機能定義
	 */
	@Override
	public void execute(InParamCallbackBase in, OutParamCallbackBase out, String actionType,
			Vd0310Contents contents, RuntimeContext ctx, WfvFunctionDef functionDef) {

		final Map<String, Object> p = in.getHandOverParam();
		final OutputStream output = (OutputStream)p.get(OutputStream.class.getSimpleName());
		final Vd0310ExecuteRequest req = (Vd0310ExecuteRequest)p.get(Vd0310ExecuteRequest.class.getSimpleName());
		final Vd0310ExecuteResponse res = (Vd0310ExecuteResponse)p.get(Vd0310ExecuteResponse.class.getSimpleName());
		final IScreenCustom screenCustom = IScreenCustom.get(contents.screenCustomClass);

		try {
			// 画面カスタムクラスでコンテンツを生成し、outputストリームへ書き出し
			final String fileName = screenCustom.doDownload(req, res, in, out, functionDef, output);

			// ダウンロード時のファイル名
			p.put("fileName", fileName);
		}
		catch (AlreadyUpdatedException | InvalidUserInputException e) {
			// 画面カスタムクラス側からユーザ入力不備として発生したエラーは、システムエラーとせず処理をブラウザに差し戻す
			throw new WfException(ReturnCode.OTHER_ERROR, e.getMessage(), e);
		}
		catch (Exception e) {
			throw new InternalServerErrorException("帳票の印刷中にエラーが発生しました", e);
		}
	}
}
