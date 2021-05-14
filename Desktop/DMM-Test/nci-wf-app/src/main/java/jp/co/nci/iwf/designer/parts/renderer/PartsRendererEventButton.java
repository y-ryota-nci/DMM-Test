package jp.co.nci.iwf.designer.parts.renderer;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignEventButton;
import jp.co.nci.iwf.designer.parts.runtime.PartsEventButton;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * イベントパーツ用レンダラー
 */
@ApplicationScoped
public class PartsRendererEventButton extends PartsRenderer<PartsEventButton, PartsDesignEventButton> {

	/**
	 * 入力項目としてHTMLを生成
	 * @param parts パーツ
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @param ec 有効条件
	 * @return
	 */
	@Override
	protected String renderInput(PartsEventButton parts, PartsDesignEventButton design, DesignerContext ctx,
			Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {

		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		final String buttonSize = getButtonSizeCssClass(design.buttonSize);
		html.append("<button ");
		{
			// CSSクラス
			html.append(toButtonCssClassHtml(design, buttonSize));
			// CSSスタイル
			final List<String> styles = toCssStyleList(parts, design, ctx);
			html.append(toCssStyleHtml(design.cssStyle, styles));
			// タブ順を付与
			if (!design.grantTabIndexFlag)
				html.appendProperty("tabindex", "-1");
			// 条件の評価結果に伴う活性／非活性の切替
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
	protected String renderReadonly(PartsEventButton parts, PartsDesignEventButton design, DesignerContext ctx,
			Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		return renderInput(parts, design, ctx, ecCache, ec);
	}

	/**
	 * パーツの印刷用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @return
	 */
	@Override
	public Object getPrintValue(PartsEventButton p, PartsDesignEventButton d, DesignerContext ctx) {
		return null;
	}

	/**
	 * パーツの表示用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキス
	 * @return パーツの表示用の値
	 */
	@Override
	public String getDisplayValue(PartsEventButton p, PartsDesignEventButton d, DesignerContext ctx) {
		return null;
	}
}
