package jp.co.nci.iwf.endpoint.vd.vd0320;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nci.iwf.cdi.annotation.BizLogic;
import jp.co.nci.iwf.designer.PartsRenderFactory;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignStandAlone;
import jp.co.nci.iwf.designer.parts.runtime.PartsStandAlone;
import jp.co.nci.iwf.designer.service.ScreenLoadService;
import jp.co.nci.iwf.designer.service.javascript.JavascriptService;
import jp.co.nci.iwf.designer.service.javascript.LoadFunction;
import jp.co.nci.iwf.designer.service.javascript.SubmitFunction;
import jp.co.nci.iwf.jersey.base.BaseService;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * 独立画面パーツ用ポップアップ画面サービス
 */
@BizLogic
public class Vd0320Service extends BaseService {
	@Inject private PartsRenderFactory renderFactory;
	@Inject private ScreenLoadService screenLoadService;
	@Inject private JavascriptService jsService;

	/**
	 * 初期化
	 * @param req
	 * @return
	 */
	public Vd0320Response init(Vd0320Request req) {
		// 独立画面パーツを単独でレンダリングするための材料集め
		DesignerContext ctx = getDesignContext(req);
		PartsStandAlone parts = (PartsStandAlone)ctx.runtimeMap.get(req.htmlId);
		PartsDesignStandAlone design = (PartsDesignStandAlone)ctx.designMap.get(parts.partsId);

		// 独立画面パーツを起点として、この画面だけに適用される外部Javascriptとロード時の呼び出し関数を生成
		final List<Long> javascriptIds = new ArrayList<>();
		final List<LoadFunction> loadFunctions = new ArrayList<>();
		final List<SubmitFunction> submitFunctions = new ArrayList<>();
		collectByStandAloneScreen(ctx, design, javascriptIds, loadFunctions, submitFunctions);
		// カスタムCSS
		final HtmlStringBuilder customCssStyleTag = new HtmlStringBuilder();
		PartsUtils.collectCustomStyles(customCssStyleTag, design, ctx);

		// 独立画面パーツの配下パーツ群のHTMLを生成
		final String html = renderFactory.renderAsStandAlone(req.htmlId, ctx);

		final Vd0320Response res = createResponse(Vd0320Response.class, req);
		res.html = html;
		res.containerName = design.containerName;
		res.javascriptIds = javascriptIds;
		res.loadFunctions = loadFunctions;
		res.submitFunctions = submitFunctions;
		res.customCssStyleTag = customCssStyleTag.toString();
		res.success = true;
		return res;
	}

	/** 独立画面パーツを起点として、この画面だけに適用される外部Javascriptとロード時／サブミット時の呼び出し関数を生成 */
	private void collectByStandAloneScreen(
			DesignerContext ctx,
			PartsDesignContainer c,
			List<Long> ids,
			List<LoadFunction> funcsL,
			List<SubmitFunction> funcsS
	) {
		// 外部Javascript
		jsService.collectJavascriptIds(c, ids, ctx, true);
		// ロード時の呼び出し関数
		if (isNotEmpty(c.loadFuncName))
			funcsL.add(new LoadFunction(c));
		// Submit時の呼び出し関数
		if (isNotEmpty(c.submitFuncName))
			funcsS.add(new SubmitFunction(c));

		// 子コンテナがあれば再帰呼び出し
		for (Long partsId : c.childPartsIds) {
			PartsDesign d = ctx.designMap.get(partsId);
			if (d instanceof PartsDesignContainer) {
				collectByStandAloneScreen(ctx, (PartsDesignContainer)d, ids, funcsL, funcsS);
			}
		}
	}

	/** デザイナーコンテキストを取得 */
	private DesignerContext getDesignContext(Vd0320Request req) {
		// 実行時/申請時はデータベースから読み直す。
		// プレビュー時は変更中の内容がリクエストとして送信されてくる。
		// 実行時/申請時はデータベースから読み直す。
		DesignerContext ctx = null;
		if (req.previewContext != null) {
			// リクエストのを流用
			ctx = req.previewContext;
			ctx.runtimeMap = req.runtimeMap;
		} else {
			// パラメータをもとにContextを再生成して、パーツ定義をDBから読み直し
			ctx = RuntimeContext.newInstance(req.contents, req.viewWidth, req.runtimeMap);
			screenLoadService.loadScreenParts(ctx.screenId, ctx);
		}
		ctx.viewWidth = req.viewWidth;
		return ctx;
	}
}
