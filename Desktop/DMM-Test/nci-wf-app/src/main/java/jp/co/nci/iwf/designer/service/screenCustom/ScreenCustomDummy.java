package jp.co.nci.iwf.designer.service.screenCustom;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import jp.co.nci.integrated_workflow.api.param.InParamCallbackBase;
import jp.co.nci.integrated_workflow.api.param.OutParamCallbackBase;
import jp.co.nci.integrated_workflow.model.view.WfvFunctionDef;
import jp.co.nci.iwf.component.accesslog.AccessLogRepository;
import jp.co.nci.iwf.component.authenticate.LoginInfo;
import jp.co.nci.iwf.component.pdf.PartsPdfValueService;
import jp.co.nci.iwf.component.pdf.PdfService;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.designer.service.userData.UserDataEntity;
import jp.co.nci.iwf.designer.service.userData.UserDataLoaderService;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100ExecuteResponse;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitRequest;
import jp.co.nci.iwf.endpoint.dc.dc0100.Dc0100InitResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310ExecuteResponse;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitRequest;
import jp.co.nci.iwf.endpoint.vd.vd0310.Vd0310InitResponse;

/**
 * 複数の画面カスタムクラスがあったときの識別子解決テスト用ダミー
 */
@ScreenCustomizable
@Named
public class ScreenCustomDummy extends ScreenCustomBase {
	/** ユーザデータ読み込みサービス */
	@Inject private UserDataLoaderService loader;

	/**
	 * 画面ロード直後に呼び出されるイベント
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void afterInitLoad(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {
		log.debug("afterInitLoad() ===");
	}

	/**
	 * 画面HTMLレンダリング前に呼び出されるイベント
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void beforeInitRender(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {
		log.debug("beforeInitRender() ===");
	}

	/**
	 * 画面HTMLレンダリング後に呼び出されるイベント
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void afterInitRender(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {
		log.debug("afterInitRender() ===");
	}
	/**
	 * 画面ロード時の処理結果レスポンス送信前イベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void beforeInitResponse(Vd0310InitRequest req, Vd0310InitResponse res, RuntimeContext ctx) {
		log.debug("beforeInitResponse() ===");
	}


	/**
	 * ユーザデータのエラーチェック前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void beforeValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		log.debug("beforeValidate() ===");
	}

	/**
	 * ユーザデータのエラーチェック後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void afterValidate(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		log.debug("afterValidate() ===");
	}

	/**
	 * ユーザデータ更新前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void beforeUpdateUserData(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		log.debug("beforeUpdateUserData() ===");
	}

	/**
	 * ユーザデータ更新後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void afterUpdateUserData(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		log.debug("afterUpdateUserData() ===");
	}

	/**
	 * ワークフロー更新前に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void beforeUpdateWF(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res) {
		log.debug("beforeUpdateWF() ===");
	}

	/**
	 * ワークフロー更新後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 * @param in WF遷移APIのINパラメータ
	 * @param out WF遷移APIのOUTパラメータ
	 */
	@Override
	public void afterUpdateWF(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, InParamCallbackBase in, OutParamCallbackBase out) {
		log.debug("afterUpdateWF() ===");
	}

	/**
	 * 処理結果レスポンス送信前イベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 * @param in WF遷移APIのINパラメータ
	 * @param out WF遷移APIのOUTパラメータ
	 */
	@Override
	public void beforeResponse(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res, InParamCallbackBase in, OutParamCallbackBase out) {
		log.debug("beforeResponse() ===");
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
	@Override
	public String doDownload(Vd0310ExecuteRequest req, Vd0310ExecuteResponse res,
			InParamCallbackBase in, OutParamCallbackBase out,
			WfvFunctionDef functionDef, OutputStream output) throws IOException {

		log.debug("doDownload() ===");

		final PdfService pdfService = get(PdfService.class);
		final PartsRootContainer root = (PartsRootContainer)res.ctx.runtimeMap.get(res.ctx.root.containerCode);

		boolean isSample = false;	//
		Map<String, Object> header = null;
		List<?> details = null;
		String jasperFile = null;
		if (isSample) {
			// ------------------------------------------
			// パーツから値を抜き出してPDF用のパラメータを生成するサンプル
			// ------------------------------------------
			// でも当画面カスタムクラスはダミーで、実際にはどういうパーツか分からないから、こっちが通ることはない
			// あくまで書き方のサンプルというだけ
			jasperFile = functionDef.getFunctionParameter01();

			// PDF印刷用にパーツの値を抜き出してMap化
			final PartsPdfValueService pdfValueService = get(PartsPdfValueService.class);
			header = pdfValueService.toMap(root, res.ctx);

			// 子コンテナの値を抜き出し、明細行としてList化
			PartsContainerBase<?> container = (PartsContainerBase<?>)res.ctx.runtimeMap.get("RPT0000");
			details = pdfValueService.toMapList(container, res.ctx);
		}
		else {
			// 印刷用のパラメータを生成し、PDFを出力するサンプル（実際に）
			// パーツから値を抜き出すサンプルは上を見よ
			jasperFile = "sample.jasper";
			header = toParams(LoginInfo.get());	// 操作者情報の属性をMap化
			details = get(AccessLogRepository.class).selectTop(10);	// アクセスログの直近10レコードをリスト化
		}
		// PDF生成し、ストリームへ書き出し
		pdfService.writePdfStream(jasperFile, header, details.toArray(), output);

		// ダウンロード時のファイル名
		return "CallbackFunctionからのPDF印刷サンプル実装.pdf";
	}

	// 以下、文書管理側でのイベント定義
	/**
	 * 画面ロード直後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void afterInitLoad(Dc0100InitRequest req, Dc0100InitResponse res, RuntimeContext ctx) {
		log.debug("afterInitLoad() ===");
	}

	/**
	 * 画面ロード時の画面HTMLレンダリング前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void beforeInitRender(Dc0100InitRequest req, Dc0100InitResponse res, RuntimeContext ctx) {
		log.debug("DOC:beforeInitRender() ===");
	}

	/**
	 * 画面ロード時の画面HTMLレンダリング後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void afterInitRender(Dc0100InitRequest req, Dc0100InitResponse res, RuntimeContext ctx) {
		log.debug("DOC:afterInitRender() ===");
	}

	/**
	 * 画面ロード時の処理結果レスポンス送信前イベント
	 * @param req リクエスト
	 * @param res レスポンス
	 * @param ctx デザイナーコンテキスト
	 */
	@Override
	public void beforeInitResponse(Dc0100InitRequest req, Dc0100InitResponse res, RuntimeContext ctx) {
		log.debug("DOC:beforeInitResponse() ===");
	}

	/**
	 * ユーザデータのエラーチェック前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void beforeValidate(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		log.debug("DOC:beforeValidate() ===");
	}

	/**
	 * ユーザデータのエラーチェック後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void afterValidate(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		log.debug("DOC:afterValidate() ===");
	}

	/**
	 * ユーザデータ更新前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void beforeUpdateUserData(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		log.debug("DOC:beforeUpdateUserData() ===");
	}

	/**
	 * ユーザデータ更新後に呼び出されるイベント
	 * @param req リクエスト（含むアクション）
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void afterUpdateUserData(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		log.debug("DOC:afterUpdateUserData() ===");
	}

	/**
	 * 文書情報の登録・更新前に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void beforeUpdateDoc(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		log.debug("DOC:beforeUpdateDoc() ===");
	}

	/**
	 * 文書情報の登録・更新後に呼び出されるイベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void afterUpdateDoc(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		log.debug("DOC:afterUpdateDoc() ===");
	}

	/**
	 * 処理結果レスポンス送信前イベント
	 * @param req リクエスト
	 * @param res レスポンス（含むデザイナーコンテキスト）
	 */
	@Override
	public void beforeResponse(Dc0100ExecuteRequest req, Dc0100ExecuteResponse res) {
		log.debug("DOC:beforeResponse() ===");
	}


	/**
	 * ロード直後のデザインコンテキストに対して、パーツデザインの修正を行う。
	 * レスポンスに対しては直接影響はしないが、designMapやdcMap等を書き換えることで間接的にレスポンスへ影響を与える。
	 * サーバ側処理のすべてに対して直接的に影響があるので、特にパフォーマンスに厳重注意すること。
	 * 	・他の何物よりも先に実行される
	 * 	・どんなサーバ側処理でも必ず呼び出される（再描画やパーツ固有イベントなどすべて）
	 * @param ctx
	 * @param designCodeMap PartsDesign.designCodeをキーとしたMap
	 */
	@Override
	public void modifyDesignContext(DesignerContext ctx, Map<String, PartsDesign> designCodeMap) {
		log.debug("modifyDesignContext() ===");
	}

	/**
	 * 抽出キーをもとにユーザデータを抽出する。
	 * （UserDataLoaderService.fillUserData()を呼び出すためのユーザデータMapを生成する）
	 * @param keys 抽出キーMap
	 * @return
	 */
	@Override
	public Map<String, List<UserDataEntity>> createUserDataMap(Map<String, String> keys) {
		final Map<String, List<UserDataEntity>> tables = new HashMap<>();
		if (keys.get("applicationNo") != null) {
			// ヘッダ
			{
				final String tableName = "MWT_CNTR000084";	// ヘッダ部のコンテナのテーブル名
				final String sql = "select * from MWT_CNTR000084 where NMB0013 = ?";
				final Object[] params = { keys.get("applicationNo") };
				final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
				tables.put(tableName, userDataList);
			}
			// グリッドの明細行
			{
				final BigDecimal processId = (BigDecimal)tables.get("MWT_CNTR000084").get(0).values.get("PROCESS_ID");
				final String tableName = "MWT_CNTR000152";	// ヘッダ部のコンテナのテーブル名
				final String sql = "select * from MWT_CNTR000152 where PROCESS_ID = ?";
				final Object[] params = { processId };
				final List<UserDataEntity>  userDataList = loader.getUserData(tableName, sql, params);
				tables.put(tableName, userDataList);
			}
		}
		return tables;
	}
}
