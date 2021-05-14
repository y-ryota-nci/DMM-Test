package jp.co.nci.iwf.designer.parts.renderer;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesign;
import jp.co.nci.iwf.designer.parts.design.PartsDesignLabel;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsLabel;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * Labelパーツレンダラー
 */
@ApplicationScoped
public class PartsRendererLabel extends PartsRenderer<PartsLabel, PartsDesignLabel> {

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
	protected String renderInput(PartsLabel parts, PartsDesignLabel design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		return renderReadonly(parts, design, ctx, ecCache, ec);
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
	protected String renderReadonly(PartsLabel parts, PartsDesignLabel design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		if (DcType.HIDDEN != parts.dcType && design != null && isNotEmpty(design.labelText)) {
			html.append("<label");

			// 関連付けするパーツID→関連付けられたHtmlId
			boolean isRequired = design.requiredFlag;
			if (design.partsIdFor != null) {
				long partsIdFor = design.partsIdFor;
				for (PartsBase<?> p : ctx.runtimeMap.values()) {
					if (partsIdFor == p.partsId) {
						// 連動するパーツの htmlId を forへ設定
						html.appendProperty("for", p.htmlId);
						// 連動するパーツが必須なら、ラベル側も必須に。
						final PartsDesign d = ctx.designMap.get(p.partsId);
						isRequired |= d.requiredFlag;
						break;
					}
				}
			}

			// CSSクラス
			final String required = isRequired ? "required" : "";
			html.append(toCssClassHtml(design.cssClass, "form-control-static", required));
			// CSSスタイル
			final List<String> styles = toCssStyleList(parts, design, ctx);
			html.append(toCssStyleHtml(design.cssStyle, styles));
			html.append(">");

			html.appendEscape(design.labelText);
			html.append("</label>");
		}
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
	public Object getPrintValue(PartsLabel p, PartsDesignLabel d, DesignerContext ctx) {
		return d.labelText;
	}

	/**
	 * パーツの表示用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキス
	 * @return パーツの表示用の値
	 */
	@Override
	public String getDisplayValue(PartsLabel p, PartsDesignLabel d, DesignerContext ctx) {
		return null;
	}
}
