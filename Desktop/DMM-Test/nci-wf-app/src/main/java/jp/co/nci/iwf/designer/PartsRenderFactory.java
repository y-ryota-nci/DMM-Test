package jp.co.nci.iwf.designer;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignAjax;
import jp.co.nci.iwf.designer.parts.design.PartsDesignContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRootContainer;
import jp.co.nci.iwf.designer.parts.design.PartsDesignStandAlone;
import jp.co.nci.iwf.designer.parts.renderer.IPartsRenderer;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererAttachFile;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererCheckbox;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererDropdown;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererEventButton;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererGrid;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererHyperlink;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererImage;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererLabel;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererMaster;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererNumbering;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererOrganizationSelect;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererRadio;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererRepeater;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererRootContainer;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererSearchButton;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererStamp;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererStandAlone;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererTextbox;
import jp.co.nci.iwf.designer.parts.renderer.PartsRendererUserSelect;
import jp.co.nci.iwf.designer.parts.runtime.PartsAjax;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.designer.parts.runtime.PartsStandAlone;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsAjaxUtils;

/**
 * パーツレンダラー用ファクトリ
 */
@ApplicationScoped
public class PartsRenderFactory implements DesignerCodeBook {

	// 実行時のレンダラー
	@Inject private PartsRendererTextbox rendererTextbox;
	@Inject private PartsRendererLabel rendererLabel;
	@Inject private PartsRendererCheckbox rendererCheckbox;
	@Inject private PartsRendererRadio rendererRadio;
	@Inject private PartsRendererDropdown rendererDropdown;
	@Inject private PartsRendererUserSelect rendererUserSelect;
	@Inject private PartsRendererOrganizationSelect rendererOrganizationSelect;
	@Inject private PartsRendererRepeater rendererRepeater;
	@Inject private PartsRendererGrid rendererGrid;
	@Inject private PartsRendererRootContainer rendererRoot;
	@Inject private PartsRendererNumbering rendererNumbering;
	@Inject private PartsRendererStamp rendererStamp;
	@Inject private PartsRendererSearchButton rendererSearchButton;
	@Inject private PartsRendererEventButton rendererEventButton;
	@Inject private PartsRendererMaster rendererMaster;
	@Inject private PartsRendererHyperlink rendererHyperlink;
	@Inject private PartsRendererAttachFile rendererAttachFile;
	@Inject private PartsRendererImage rendererImage;
	@Inject private PartsRendererStandAlone rendererStandAloneScreen;
	@Inject private PartsCalcUtils partsCalcUtils;
	@Inject private PartsCondUtils partsCondUtils;

	/**
	 * 実行時のパーツレンダラーを返す
	 * @param partsType
	 * @return
	 */
	public IPartsRenderer<? extends PartsBase<?>, ? extends PartsDesign> get(int partsType) {
		switch (partsType) {
		case PartsType.TEXTBOX: return rendererTextbox;
		case PartsType.LABEL: return rendererLabel;
		case PartsType.HYPERLINK: return rendererHyperlink;
		case PartsType.ATTACHFILE: return rendererAttachFile;
		case PartsType.IMAGE: return rendererImage;
		case PartsType.CHECKBOX: return rendererCheckbox;
		case PartsType.RADIO: return rendererRadio;
		case PartsType.DROPDOWN: return rendererDropdown;
		case PartsType.USER: return rendererUserSelect;
		case PartsType.ORGANIZATION: return rendererOrganizationSelect;
		case PartsType.REPEATER: return rendererRepeater;
		case PartsType.GRID: return rendererGrid;
		case PartsType.STAND_ALONE: return rendererStandAloneScreen;
		case PartsType.ROOT_CONTAINER: return rendererRoot;
		case PartsType.NUMBERING: return rendererNumbering;
		case PartsType.STAMP: return rendererStamp;
		case PartsType.SEARCH_BUTTON: return rendererSearchButton;
		case PartsType.EVENT_BUTTON: return rendererEventButton;
		case PartsType.MASTER: return rendererMaster;
		default:
		}
		String msg = String.format("partsType=[%s]に対応したレンダラーは未定義です", partsType);
		throw new InternalServerErrorException(msg);
	}

	/**
	 * デザイナのコンテンツを一括レンダリング
	 * @param ctx
	 * @return
	 */
	public String renderAll(DesignerContext ctx) {
		final Map<String, EvaluateCondition> ecCache = new HashMap<String, EvaluateCondition>();
		return renderAll(ctx, ecCache);
	}

	/**
	 * デザイナのコンテンツを一括レンダリング
	 * @param ctx
	 * @param ecCache 有効条件の判定結果キャッシュ
	 * @return
	 */
	public String renderAll(DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		final StringBuilder html = new StringBuilder(4096);
		switch (ctx.renderMode) {
		case RUNTIME:
			renderAllRuntime(html, ctx, ecCache);
			break;
		case DESIGN:
			renderAllDesignTime(html, ctx, ecCache);
			break;
		case PREVIEW:
			renderAllPreviewTime(html, ctx, ecCache);
			break;
		}
		return html.toString();
	}

	/**
	 * 実行時の一括レンダリング
	 * @param html
	 * @param ctx
	 * @param ecCache
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void renderAllRuntime(StringBuilder html, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		final PartsDesignRootContainer root = ctx.root;
		final PartsRootContainer rc = (PartsRootContainer)ctx.runtimeMap.get(ctx.root.containerCode);
		final PartsDesignRootContainer rd = ctx.root;

		// レンダリングの前処理として、計算式と有効条件をランタイムパーツに設定
		prepareRendering(rc, rd, ctx, ecCache);

		// レンダリング
		final IPartsRenderer r = get(PartsType.ROOT_CONTAINER);
		html.append(r.render(rc, root, ctx, ecCache));
	}

	/**
	 * プレビュー時の一括レンダリング
	 * @param html
	 * @param ctx
	 * @param ecCache
	 */
	private void renderAllPreviewTime(StringBuilder html, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		final PartsDesignRootContainer rd = ctx.root;

		// 申請時と異なるのは、ランタイムデータをでっち上げてレンダリングする点だ。
		// デザイン時と異なるのは、いったんレンダリングしたあともプレビュー画面上で継続的に
		// オペレーションが可能なので、毎回ランタイムを生成は出来ない点だ。
		final PartsRootContainer rc;
		if (ctx.runtimeMap.isEmpty()) {
			rc = rd.newParts(null, null, ctx);
			ctx.runtimeMap.put(rc.htmlId, rc);
		} else {
			rc = (PartsRootContainer)ctx.runtimeMap.get(rd.containerCode);
		}

		// レンダリング
		html.append(renderDiff(rc, rd, ctx, ecCache));
	}

	/**
	 * デザイン時の一括レンダリング
	 * @param html
	 * @param ctx
	 * @param ecCache
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void renderAllDesignTime(StringBuilder html, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		final PartsDesignRootContainer root = ctx.root;

		// 申請時と異なるのは、ランタイムデータをでっち上げてレンダリングする点だ。
		// ランタイムデータはHTMLレンダリング中に相互に参照することがあるので、
		// レンダリング前に必ずランタイムデータが生成し終わっている必要がある
		ctx.runtimeMap.clear();
		final PartsRootContainer rc = root.newParts(null, null, ctx);
		ctx.runtimeMap.put(rc.htmlId, rc);

		// レンダリング
		final IPartsRenderer r = get(PartsType.ROOT_CONTAINER);
		html.append(r.render(rc, root, ctx, ecCache));
	}

	/** 計算式と有効条件をランタイムパーツに設定 */
	private void prepareRendering(DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		final PartsDesignContainer dc = ctx.root;
		final PartsContainerBase<?> rc = (PartsContainerBase<?>)ctx.runtimeMap.get(dc.containerCode);
		prepareRendering(rc, dc, ctx, ecCache);
	}

	/** 計算式と有効条件をランタイムパーツに設定 */
	private void prepareRendering(PartsContainerBase<?> rc, PartsDesignContainer dc, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		// 条件評価結果
		rc.evaluateCondition = partsCondUtils.getEvaluateCondition(rc, dc, ctx, ecCache);
		// 計算式
		rc.calculateCondition = partsCalcUtils.getCalculate(rc, dc, ctx);
		// (自身がトリガーパーツとなっている)汎用マスタ検索パーツ一覧
		rc.ajaxTriggers = PartsAjaxUtils.getAjaxTriggerSet(rc, dc, ctx);

		// コンテナ行に定義されている子パーツに対しても有効条件と計算式を設定
		for (PartsContainerRow row : rc.rows) {
			for (String htmlId : row.children) {
				final PartsBase<?> p = ctx.runtimeMap.get(htmlId);
				final PartsDesign d = ctx.designMap.get(p.partsId);
				// 条件評価結果
				p.evaluateCondition = partsCondUtils.getEvaluateCondition(p, d, ctx, ecCache);
				// 計算式
				p.calculateCondition = partsCalcUtils.getCalculate(p, d, ctx);
				// (自身がトリガーパーツとなっている)汎用マスタ検索パーツ一覧
				p.ajaxTriggers = PartsAjaxUtils.getAjaxTriggerSet(p, d, ctx);

				// 汎用マスタを使うパーツなら、検索条件と検索結果の定義を出力
				// Runtimeパーツがすべて揃ってからでないと正確なものが生成できないので、仕方なくここで実行している
				if (p instanceof PartsAjax) {
					final PartsAjax<?> pa = (PartsAjax<?>)p;
					final PartsDesignAjax pda = (PartsDesignAjax)d;
					pa.tableSearchId = pda.tableSearchId;
					pa.conditions = PartsMasterHelper.toConditions(pda.relations, pa, ctx);
					pa.results = PartsMasterHelper.toResults(pda.relations, pa, ctx);
				}

				// 再帰的に。
				if (p instanceof PartsContainerBase) {
					prepareRendering((PartsContainerBase<?>)p, (PartsDesignContainer)d, ctx, ecCache);
				}
			}
		}
	}

	/**
	 * 独立画面としてHTMLをレンダリング
	 * @param htmlId 独立画面パーツのHTML ID
	 * @param ctx デザイナーコンテキスト
	 * @return 独立画面のHTML
	 */
	public String renderAsStandAlone(String htmlId, DesignerContext ctx) {
		final PartsStandAlone parts = (PartsStandAlone)ctx.runtimeMap.get(htmlId);
		final PartsDesignStandAlone design = (PartsDesignStandAlone)ctx.designMap.get(parts.partsId);
		final Map<String, EvaluateCondition> ecCache = new HashMap<>();

		parts.viewWidth = ctx.viewWidth;
		parts.renderAsStandAlone = true;	// 独立画面モードに切り替え
		final String html = renderDiff(parts, design, ctx, ecCache);
		parts.renderAsStandAlone = false;	// パーツモードに切り替え

		return html;
	}

	/**
	 * 指定されたコンテナ配下だけの差分HTMLを生成
	 * @param p コンテナパーツ
	 * @param d コンテナパーツ定義
	 * @param ctx デザイナーコンテキスト
	 * @param ecCache
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String renderDiff(PartsBase<?> p, PartsDesign d, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		// レンダリングの前処理として、計算式と有効条件をランタイムパーツに設定
		prepareRendering(ctx, ecCache);

		final IPartsRenderer r = get(d.partsType);
		return r.render(p, d, ctx, ecCache);
	}
}
