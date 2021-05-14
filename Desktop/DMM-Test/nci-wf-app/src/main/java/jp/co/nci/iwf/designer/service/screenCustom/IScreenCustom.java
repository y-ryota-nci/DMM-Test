package jp.co.nci.iwf.designer.service.screenCustom;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.literal.NamedLiteral;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.lang3.StringUtils;

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
 * 画面カスタムクラスのインターフェース
 */
public interface IScreenCustom {
	/** 画面カスタムクラスが未定義の場合に使用されるNULLインスタンスの名称（ @Named の解決用） */
	static final String DEFAULT =
//			StringUtils.uncapitalize(ScreenCustomBase.class.getSimpleName());
			StringUtils.uncapitalize(ScreenCustomDummy.class.getSimpleName());

	/**
	 * 画面カスタムクラスのインスタンスを取得する
	 * @param fqcn 画面カスタムクラスの完全修飾クラス名（FQCN）
	 * @return
	 */
	public static IScreenCustom get(String fqcn) {
		String name = DEFAULT;
		try {
			if (MiscUtils.isNotEmpty(fqcn)) {
				// @Namedの仕様に合わせて、単純クラス名の先頭を小文字化
				final Class<?> clazz = Class.forName(fqcn);
				name = StringUtils.uncapitalize(clazz.getSimpleName());
			}
			// @Namedで指定された文字列をもとに、認証サービス(IScreenCustom)の実装クラスをインスタンス化
			final NamedLiteral selector = NamedLiteral.of(name);
			return CDI.current().select(IScreenCustom.class, selector).get();
		}
		catch (ClassNotFoundException e) {
			throw new InternalServerErrorException(
					"画面カスタマイズクラスが存在しません screenCustomClass=" + fqcn, e);
		}
	}

	/**
	 * 画面ロード直後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	void afterInitLoad(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx);

	/**
	 * 画面ロード時の画面HTMLレンダリング前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	void beforeInitRender(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx);

	/**
	 * 画面ロード時の画面HTMLレンダリング後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	void afterInitRender(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx);

	/**
	 * 画面ロード時の処理結果レスポンス送信前イベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	public void beforeInitResponse(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx);

	/**
	 * ユーザデータのエラーチェック前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	void beforeValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res);

	/**
	 * ユーザデータのエラーチェック後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	void afterValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res);

	/**
	 * ユーザデータ更新前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	void beforeUpdateUserData(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res);

	/**
	 * ユーザデータ更新後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	void afterUpdateUserData(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res);

	/**
	 * ワークフロー更新前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	void beforeUpdateWF(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res);

	/**
	 * ワークフロー更新後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 * @param in WF遷移APIのINパラメータ
	 * @param out WF遷移APIのOUTパラメータ
	 */
	void afterUpdateWF(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, InParamCallbackBase in, OutParamCallbackBase out);

	/**
	 * 処理結果レスポンス送信前イベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 * @param in WF遷移APIのINパラメータ
	 * @param out WF遷移APIのOUTパラメータ
	 */
	void beforeResponse(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, InParamCallbackBase in, OutParamCallbackBase out);

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
	String doDownload(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res,
			InParamCallbackBase in, OutParamCallbackBase out,
			WfvFunctionDef functionDef, OutputStream output) throws IOException;

	// 以下、文書管理側でのイベント定義
	/**
	 * 画面ロード直後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	void afterInitLoad(Dc0100InitRequest req, Dc0100InitResponse res, RuntimeContext ctx);

	/**
	 * 画面ロード時の画面HTMLレンダリング前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	void beforeInitRender(Dc0100InitRequest req, Dc0100InitResponse res, RuntimeContext ctx);

	/**
	 * 画面ロード時の画面HTMLレンダリング後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	void afterInitRender(Dc0100InitRequest req, Dc0100InitResponse res, RuntimeContext ctx);

	/**
	 * 画面ロード時の処理結果レスポンス送信前イベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	public void beforeInitResponse(Dc0100InitRequest req, Dc0100InitResponse res, RuntimeContext ctx);

	/**
	 * ユーザデータのエラーチェック前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	void beforeValidate(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res);

	/**
	 * ユーザデータのエラーチェック後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	void afterValidate(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res);

	/**
	 * ユーザデータ更新前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	void beforeUpdateUserData(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res);

	/**
	 * ユーザデータ更新後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	void afterUpdateUserData(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res);

	/**
	 * 文書情報の登録・更新前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	void beforeUpdateDoc(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res);

	/**
	 * 文書情報の登録・更新後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	void afterUpdateDoc(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res);

	/**
	 * 処理結果レスポンス送信前イベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	void beforeResponse(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res);

	/**
	 * ロード直後のデザインコンテキストに対して、パーツデザインの修正を行う。
	 * レスポンスに対しては直接影響はしないが、designMapやdcMap等を書き換えることで間接的にレスポンスへ影響を与える。
	 * サーバ側処理のすべてに対して直接的に影響があるので、特にパフォーマンスに厳重注意すること。
	 * 	・他の何物よりも先に実行される
	 * 	・どんなサーバ側処理でも必ず呼び出される（再描画やパーツ固有イベントなどすべて）
	 * @param ctx
	 * @param designCodeMap PartsDesign.designCodeをキーとしたMap
	 */
	void modifyDesignContext(DesignerContext ctx, Map<String, PartsDesign> designCodeMap);

	/**
	 * 抽出キーをもとにユーザデータを抽出する。
	 * （UserDataLoaderService.fillUserData()を呼び出すためのユーザデータMapを生成する）
	 * @param keys 抽出キーMap
	 * @return
	 */
	Map<String, List<UserDataEntity>> createUserDataMap(Map<String, String> keys);
}
