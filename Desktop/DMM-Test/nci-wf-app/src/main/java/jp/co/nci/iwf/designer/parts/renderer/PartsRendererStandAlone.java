package jp.co.nci.iwf.designer.parts.renderer;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignStandAlone;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerRow;
import jp.co.nci.iwf.designer.parts.runtime.PartsStandAlone;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * 独立画面パーツレンダラー
 */
@ApplicationScoped
public class PartsRendererStandAlone
		extends PartsRendererPagingBase<PartsStandAlone, PartsDesignStandAlone> {

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
	protected String renderInput(PartsStandAlone parts, PartsDesignStandAlone design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		// 独立した画面としてレンダリングするか、それともボタン／リンクとしてレンダリングするか
		if (parts.renderAsStandAlone)
			return renderStandAlone(parts, design, ctx, ecCache, ec);
		else
			return renderAsButton(parts, design, ctx, ecCache, ec);
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
	protected String renderReadonly(PartsStandAlone parts, PartsDesignStandAlone design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		return renderInput(parts, design, ctx, ecCache, ec);
	}

	/**
	 * 独立した画面としてHTMLを作成
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @param ec 有効条件
	 * @return
	 */
	public String renderStandAlone(PartsStandAlone parts, PartsDesignStandAlone design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);

		html.append("<span");
		final String fontSize = getFontSizeCssClass(design);
		if (isNotEmpty(fontSize))
			html.appendProperty("class", fontSize);
		html.append(">");

		for (PartsContainerRow row : parts.rows) {
			// 配下パーツを展開したHTML
			if (ViewWidth.isMobile(ctx)) {
				html.append(renderWithoutBgHtml(row, design, ctx, ecCache));
			}
			else {
				html.append(renderWithBgHtml(row, design, ctx, ecCache));
			}
		}
		html.append("</span>");
		return html.toString();
	}

	/**
	 * ボタン／リンクとしてHTMLを作成
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @param ec 有効条件
	 * @return
	 */
	private String renderAsButton(PartsStandAlone parts, PartsDesignStandAlone design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		// TODO : ボタンまたはリンクとしてレンダリングしたい
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		final String buttonSize = getButtonSizeCssClass(design.buttonSize);
		html.append("<button ");
		{
			// CSSクラス
			html.append(toButtonCssClassHtml(design, buttonSize, "btn-stand-alone-screen"));
			// CSSスタイル
			final List<String> styles = toCssStyleList(parts, design, ctx);
			html.append(toCssStyleHtml(design.cssStyle, styles));
			// タブ順を付与
			if (!design.grantTabIndexFlag)
				html.appendProperty("tabindex", "-1");
			// パーツ条件の評価結果に伴う活性／非活性の切替
			// パーツ条件によりパーツが無効であればボタンは非活性（読取専用の場合は非活性にはしない）
			if (!ec.enabled)
				html.appendProperty("disabled");
			// 有効条件により活性・非活性を切り替えるための属性を付与
			// これがある要素が制御対象となる
			if (ec.control)
				html.appendProperty( getEcTargetProperty() );

			html.append(">");

			// ボタン内のアイコン
			if (isNotEmpty(design.iconCssClass)) {
				html.append("<i ");
				html.append(toCssClassHtml(design.iconCssClass));
				html.append("></i>");
			}

			// ボタンのテキスト
			if (isNotEmpty(design.buttonLabel)) {
				html.appendEscape(design.buttonLabel);
			}
		}
		html.append("</button>");

		return html.toString();
	}
}
