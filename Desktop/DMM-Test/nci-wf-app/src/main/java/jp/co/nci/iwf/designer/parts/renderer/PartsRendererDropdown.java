package jp.co.nci.iwf.designer.parts.renderer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.iwf.designer.DesignerCodeBook.RoleDropdown;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsOptionItem;
import jp.co.nci.iwf.designer.parts.design.PartsDesignDropdown;
import jp.co.nci.iwf.designer.parts.runtime.PartsDropdown;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * Dropdownパーツレンダラー
 */
@ApplicationScoped
public class PartsRendererDropdown
		extends PartsRenderer<PartsDropdown, PartsDesignDropdown>
		implements RoleDropdown {

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
	protected String renderInput(PartsDropdown parts, PartsDesignDropdown design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final boolean isDesignMode = RenderMode.DESIGN == ctx.renderMode;
		final HtmlStringBuilder html = new HtmlStringBuilder(256);

		if (isDesignMode) {
			html.append("<button class='btn btn-default dropdown-toggle' style='width:100%; text-align:right;'>");
			html.append("<span class='caret'></span>");
			html.append("</button>");
		}
		else {
			html.append("<select ");

			// CSSクラス
			final String required = design.requiredFlag ? "required" : "";
			// 読取条件により読取専用となった場合、下記のclass属性を付与
			final String readonly = design.readonly ? "readonly" : "";	// パーツ属性のreadonly
			final String ecReadonly = ec.readonly ? "ecReadonly" : "";	// パーツ条件設定の判定結果である読取専用
			html.append(toCssClassHtml(design.cssClass, "form-control", required, readonly, ecReadonly));

			// CSSスタイル
			final List<String> styles = toCssStyleList(parts, design, ctx);
			html.append(toCssStyleHtml(design.cssStyle, styles));

			// タブ順を付与
			if (!design.grantTabIndexFlag)
				html.appendProperty("tabindex", "-1");

			// 活性・非活性の設定
			// パーツ条件によりパーツが無効であれば非活性
			if (!ec.enabled)
				html.appendProperty("disabled");

			// 役割コード
			html.appendProperty("data-role", DROPDOWN_CODE);

			// 有効条件により活性・非活性を切り替えるための属性を付与
			// これがある要素が制御対象となる
			if (ec.control)
				html.appendProperty( getEcTargetProperty() );

			// Ajaxのトリガーとなれるパーツなら属性を付
			// これを設定しておくことでクライアント側で汎用マスタ検索パーツのトリガーパーツと認識してスクリプトが動く
			if (isNotEmpty(parts.ajaxTriggers))
				html.appendProperty("data-ajax-triggers");

			html.append(">");
			if (isNotEmpty(design.optionItems)) {
				html.append(getOptionItems(parts, design, ec));
			}
			html.append("</select>");
		}
		return html.toString();
	}

	/** ドロップダウンリストの選択肢である optionタグのHTMLを返す */
	private String getOptionItems(PartsDropdown parts, PartsDesignDropdown design, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		String partsValue = parts.getValue();
		// 選択肢：空行
		if (design.emptyLineType == EmptyLineType.USE_ALWAYS
				|| (design.emptyLineType == EmptyLineType.USE_MULTI && design.optionItems.size() > 1)
		) {
			html.append("<option value=''");
			if (isEmpty(partsValue)) {
				html.appendProperty("selected");
			}
			else if (design.readonly || ec.readonly) {
				// 読取専用条件が"true"であれば非活性・非表示にする
				html.appendProperty("disabled");
				html.append(toCssClassHtml(null, "hide"));
			}
			html.append(">--");
		}
		// 選択肢
		design.optionItems.forEach(o -> {
			String optionValue = StringUtils.defaultString(o.value);
			html.append("<option");
			html.appendProperty("value", optionValue);
			if (same(partsValue, optionValue) && ec.enabled) {
				html.appendProperty("selected");
			} else if (design.readonly || ec.readonly) {
				// 読取専用条件が"true"であれば非活性・非表示にする
				html.appendProperty("disabled");
				html.append(toCssClassHtml(null, "hide"));
			}
			html.append(">");
			html.appendEscape(o.label);
		});
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
	protected String renderReadonly(PartsDropdown parts, PartsDesignDropdown design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final HtmlStringBuilder html = new HtmlStringBuilder(256);

		html.append("<output ");
		// CSSクラス
		html.append(toCssClassHtml(design.cssClass, "form-control-static"));
		// CSSスタイル
		List<String> styles = toCssStyleList(parts, design, ctx);
		html.append(toCssStyleHtml(design.cssStyle, styles));

		html.append(">");

		String val = parts.getValue();
		if (isNotEmpty(design.optionItems)) {
			PartsOptionItem optionItem = design.optionItems.stream().collect(Collectors.toMap(o -> o.value, o -> o)).get(StringUtils.defaultString(val));
			html.appendEscape(isEmpty(optionItem) ? "" : optionItem.label);
		}
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
	public Object getPrintValue(PartsDropdown p, PartsDesignDropdown d, DesignerContext ctx) {
		String val = p.getValue();
		if (isNotEmpty(d.optionItems)) {
			return d.optionItems.stream()
					.filter(o -> eq(o.value, val))
					.map(o -> o.label)
					.findFirst()
					.orElse("");
		}
		return "";
	}

	/**
	 * パーツの表示用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキス
	 * @return パーツの表示用の値
	 */
	@Override
	public String getDisplayValue(PartsDropdown p, PartsDesignDropdown d, DesignerContext ctx) {
		String val = p.getValue();
		if (isNotEmpty(d.optionItems)) {
			return d.optionItems.stream()
					.filter(o -> eq(o.value, val))
					.map(o -> o.label)
					.findFirst()
					.orElse("");
		}
		return "";
	}
}
