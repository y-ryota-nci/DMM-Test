package jp.co.nci.iwf.designer.parts.renderer;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.designer.DesignerCodeBook.RoleNumbering;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignNumbering;
import jp.co.nci.iwf.designer.parts.runtime.PartsNumbering;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * 採番パーツレンダラー
 */
@ApplicationScoped
public class PartsRendererNumbering extends PartsRenderer<PartsNumbering, PartsDesignNumbering> implements RoleNumbering {

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
	protected String renderInput(PartsNumbering parts, PartsDesignNumbering design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		html.append("<input type='text'");
		// CSSクラス
		html.append(toCssClassHtml(design.cssClass, "form-control"));
		// CSSスタイル
		final List<String> styles = toCssStyleList(parts, design, ctx);
		html.append(toCssStyleHtml(design.cssStyle, styles));
		// 役割コード
		html.appendProperty("data-role", NUMBERING);
		// 読取専用
		html.appendProperty("readonly");
		// タブ順を付与
		if (!design.grantTabIndexFlag)
			html.appendProperty("tabindex", "-1");
		// 値
		String value = defaults(parts.values.get(NUMBERING));
		html.appendProperty("value", value);
		html.append(" />");
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
	protected String renderReadonly(PartsNumbering parts, PartsDesignNumbering design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);

		html.append("<output ");
		// CSSクラス
		html.append(toCssClassHtml(design.cssClass, "form-control-static"));
		// CSSスタイル
		List<String> styles = toCssStyleList(parts, design, ctx);
		html.append(toCssStyleHtml(design.cssStyle, styles));
		html.append(">");

		// 値
		final String val = parts.values.get(NUMBERING);
		html.appendEscape(val);

		html.append("</output>");
		return html.toString();
	}

	/**
	 * パーツの印刷用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @return
	 */
	@Override
	public Object getPrintValue(PartsNumbering p, PartsDesignNumbering d, DesignerContext ctx) {
		return p.getValue();
	}

	/**
	 * パーツの表示用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキス
	 * @return パーツの表示用の値
	 */
	@Override
	public String getDisplayValue(PartsNumbering p, PartsDesignNumbering d, DesignerContext ctx) {
		return p.getValue();
	}
}
