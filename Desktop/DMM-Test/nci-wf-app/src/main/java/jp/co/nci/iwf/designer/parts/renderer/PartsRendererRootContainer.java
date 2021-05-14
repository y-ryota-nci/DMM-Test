package jp.co.nci.iwf.designer.parts.renderer;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRootContainer;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsRootContainer;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * ルートコンテナ用のレンダラー
 */
@ApplicationScoped
public class PartsRendererRootContainer extends PartsRendererContainerBase<PartsRootContainer, PartsDesignRootContainer> {
	/**
	 * HTMLを生成
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @return
	 */
	@Override
	public String render(PartsRootContainer parts, PartsDesignRootContainer design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache) {
		// 描画時の画面幅を記録しておく
		parts.viewWidth = ctx.viewWidth;
		return super.render(parts, design, ctx, ecCache);
	}

	/**
	 * 入力項目としてHTMLを生成
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @param ec 有効条件
	 * @return
	 */
	@Override
	protected String renderInput(PartsRootContainer parts, PartsDesignRootContainer design, DesignerContext ctx, Map<String, EvaluateCondition> ecResult, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(4096);
		html.append("<span");
		final String fontSize = getFontSizeCssClass(design);
		if (isNotEmpty(fontSize))
			html.appendProperty("class", fontSize);
		html.append(">");
		for (PartsContainerRow row : parts.rows) {
			if (ViewWidth.isMobile(ctx)) {
				html.append(renderWithoutBgHtml(row, design, ctx, ecResult));
			}
			else {
				html.append(renderWithBgHtml(row, design, ctx, ecResult));
			}
		}
		html.append("</span>");
		return html.toString();
	}

	/**
	 * 読取専用項目としてHTMLを生成
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @param ec 有効条件
	 * @return
	 */
	@Override
	protected String renderReadonly(PartsRootContainer parts, PartsDesignRootContainer design, DesignerContext ctx, Map<String, EvaluateCondition> ecResult, EvaluateCondition ec) {
		return renderInput(parts, design, ctx, ecResult, ec);
	}

}
