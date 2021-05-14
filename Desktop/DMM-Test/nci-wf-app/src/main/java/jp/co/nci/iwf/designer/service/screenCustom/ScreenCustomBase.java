package jp.co.nci.iwf.designer.service.screenCustom;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 画面カスタムクラスの基底クラスで、何もしないNULLインスタンス
 */
@ScreenCustomizable
@Named
public class ScreenCustomBase extends MiscUtils implements IScreenCustom {
	/** ロガー */
	protected Logger log = LoggerFactory.getLogger(getClass());

	/** CDI管理下にあるインスタンスを取得 */
	protected <T> T get(Class<T> clazz) {
		return CDI.current().select(clazz).get();
	}

	/** CDI管理下にあるインスタンスを取得 */
	protected <T> T get(Class<T> clazz, Annotation... qualifiers) {
		return CDI.current().select(clazz, qualifiers).get();
	}

	/**
	 * 画面ロード直後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	public void afterInitLoad(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {}

	/**
	 * 画面ロード時の画面HTMLレンダリング前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	public void beforeInitRender(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {}

	/**
	 * 画面ロード時の画面HTMLレンダリング後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	public void afterInitRender(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {}

	/**
	 * 画面ロード時の処理結果レスポンス送信前イベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	public void beforeInitResponse(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {}

	/**
	 * ユーザデータのエラーチェック前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void beforeValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {}

	/**
	 * ユーザデータのエラーチェック後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void afterValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {}

	/**
	 * ユーザデータ更新前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void beforeUpdateUserData(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {}

	/**
	 * ユーザデータ更新後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void afterUpdateUserData(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {}

	/**
	 * ワークフロー更新前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void beforeUpdateWF(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {}

	/**
	 * ワークフロー更新後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 * @param in WF遷移APIのINパラメータ
	 * @param out WF遷移APIのOUTパラメータ
	 */
	public void afterUpdateWF(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, InParamCallbackBase in, OutParamCallbackBase out) {}

	/**
	 * 処理結果レスポンス送信前イベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 * @param in WF遷移APIのINパラメータ
	 * @param out WF遷移APIのOUTパラメータ
	 */
	public void beforeResponse(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, InParamCallbackBase in, OutParamCallbackBase out) {}

	/**
	 * 印刷実行
	 * @param req リクエスト(含むアクション）
	 * @param res（含むデザイナーコンテキスト）
	 * @param functionDef アクション機能
	 * @param output 書き込み先ストリーム（close()不要）
	 * @return ファイルダウンロード用のファイル名
	 */
	public String doPrint(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res,
			WfvFunctionDef functionDef, OutputStream output)  throws IOException{
		return null;
	}

	/**
	 * ダウンロードコンテンツをoutputへ書き込む
	 * @param req リクエスト(含むアクション）
	 * @param res（含むデザイナーコンテキスト）
	 * @param in API呼出し時のINパラメータ
	 * @param out API呼出し時のOUTパラメータ
	 * @param functionDef アクション機能
	 * @param output 書き込み先ストリーム（close()不要）
	 * @return ファイルダウンロード用のファイル名
	 */
	public String doDownload(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res,
			InParamCallbackBase in, OutParamCallbackBase out,
			WfvFunctionDef functionDef, OutputStream output) throws IOException {
		return null;
	}

	// 以下、文書管理側でのイベント定義
	/**
	 * 画面ロード直後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	public void afterInitLoad(Dc0100InitRequest req, Dc0100InitResponse res, RuntimeContext ctx) {}

	/**
	 * 画面ロード時の画面HTMLレンダリング前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	public void beforeInitRender(Dc0100InitRequest req, Dc0100InitResponse res, RuntimeContext ctx) {}

	/**
	 * 画面ロード時の画面HTMLレンダリング後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	public void afterInitRender(Dc0100InitRequest req, Dc0100InitResponse res, RuntimeContext ctx) {}

	/**
	 * 画面ロード時の処理結果レスポンス送信前イベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	public void beforeInitResponse(Dc0100InitRequest req, Dc0100InitResponse res, RuntimeContext ctx) {}

	/**
	 * ユーザデータのエラーチェック前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void beforeValidate(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {}

	/**
	 * ユーザデータのエラーチェック後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void afterValidate(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {}

	/**
	 * ユーザデータ更新前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void beforeUpdateUserData(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {}

	/**
	 * ユーザデータ更新後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void afterUpdateUserData(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {}

	/**
	 * 文書情報の登録・更新前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void beforeUpdateDoc(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {}

	/**
	 * 文書情報の登録・更新後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void afterUpdateDoc(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {}

	/**
	 * 処理結果レスポンス送信前イベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	public void beforeResponse(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {}

	/**
	 * ロード直後のデザインコンテキストに対して、パーツデザインの修正を行う。
	 * レスポンスに対しては直接影響はしないが、designMapやdcMap等を書き換えることで間接的にレスポンスへ影響を与える。
	 * サーバ側処理のすべてに対して直接的に影響があるので、特にパフォーマンスに厳重注意すること。
	 * 	・他の何物よりも先に実行される
	 * 	・どんなサーバ側処理でも必ず呼び出される（再描画やパーツ固有イベントなどすべて）
	 * @param ctx
	 * @param designCodeMap PartsDesign.designCodeをキーとしたMap
	 */
	public void modifyDesignContext(DesignerContext ctx, Map<String, PartsDesign> designCodeMap) {}

	/**
	 * 抽出キーをもとにユーザデータを抽出する。
	 * （UserDataLoaderService.fillUserData()を呼び出すためのユーザデータMapを生成する）
	 * @param keys 抽出キーMap
	 * @return
	 */
	public Map<String, List<UserDataEntity>> createUserDataMap(Map<String, String> keys) { return new HashMap<>(); }
}
