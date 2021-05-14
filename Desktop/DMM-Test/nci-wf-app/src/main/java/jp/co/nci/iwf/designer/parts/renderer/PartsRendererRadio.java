package jp.co.nci.iwf.designer.parts.renderer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.iwf.designer.DesignerCodeBook.RoleRadio;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsOptionItem;
import jp.co.nci.iwf.designer.parts.design.PartsDesignRadio;
import jp.co.nci.iwf.designer.parts.runtime.PartsRadio;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * Radioパーツレンダラー
 */
@ApplicationScoped
public class PartsRendererRadio
		extends PartsRenderer<PartsRadio, PartsDesignRadio>
		implements RoleRadio {

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
	protected String renderInput(PartsRadio parts, PartsDesignRadio design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final boolean isDesignMode = RenderMode.DESIGN == ctx.renderMode;
		if (isDesignMode && isEmpty(design.optionItems)) {
			design.optionItems = Arrays.asList(
					PartsOptionItem.SAMPLE_1,
					PartsOptionItem.SAMPLE_2,
					PartsOptionItem.SAMPLE_3
			);
		}

		final HtmlStringBuilder html = new HtmlStringBuilder(256);
		html.append("<div class='form-control-static'>");

		final List<String> styles = toCssStyleList(parts, design, ctx);
		final String required = (!isDesignMode && design.requiredFlag) ? "required" : "";
		final String readonly = (!isDesignMode && design.readonly) ? "readonly" : "";	// パーツ属性のreadonly
		final boolean isVertical = DisplayMethod.VERTICAL == design.displayMethod;

		design.optionItems.forEach(o -> {
			if (isVertical) {
				html.append("<div class='radio'>");
				html.append("<label>");
			} else {
				html.append("<label class='radio-inline'>");
			}

			html.append("<input type='radio'");
			html.appendProperty("name", parts.htmlId);
			html.appendProperty("value", StringUtils.defaultString(o.value));
			html.appendProperty("data-role", RADIO_CODE);
			html.append(toCssClassHtml(design.cssClass, required, readonly));
			// チェックの有無
			// 入力値と選択肢の値が同値かつ有効であればチェック
			if (same(o.value, parts.getValue()) && ec.enabled) {
				html.appendProperty("checked");
			}
			// 活性・非活性の設定
			// パーツ条件によりパーツが無効ないし読取専用であれば非活性
			if ((design.readonly || !ec.enabled || ec.readonly) || isRenderAsReadonly(parts.dcType, ctx)) {
				html.appendProperty("disabled");
			}
			// タブ順を付与するか
			if (!design.grantTabIndexFlag) {
				html.appendProperty("tabindex", "-1");
			}

			// 有効条件により活性・非活性を切り替えるための属性を付与
			// これがある要素が制御対象となる
			if (ec.control) {
				html.appendProperty(getEcTargetProperty());
			}

			// Ajaxのトリガーとなれるパーツなら属性を付与
			// これを設定しておくことでクライアント側で汎用マスタ検索パーツのトリガーパーツと認識してスクリプトが動く
			if (isNotEmpty(parts.ajaxTriggers))
				html.appendProperty("data-ajax-triggers");

			html.append(" />");

			// ラベルのテキスト部分
			html.append("<span ");
			html.append(toCssClassHtml(design.cssClass));
			html.append(toCssStyleHtml(design.cssStyle, styles));
			html.append(">");
			html.appendEscape(o.label);
			html.append("</span>");

			if (isVertical) {
				html.append("</label>");
				html.append("<div class='radio'>");
			} else {
				html.append("</label>");
			}
		});
		html.append("</div>");
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
	protected String renderReadonly(PartsRadio parts, PartsDesignRadio design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
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
	public Object getPrintValue(PartsRadio p, PartsDesignRadio d, DesignerContext ctx) {
		final String val = p.getValue();
		return d.optionItems.stream()
				.filter(o -> same(o.value, val))
				.map(o -> o.label)
				.findFirst()
				.orElse("");
	}

	/**
	 * パーツの表示用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキス
	 * @return パーツの表示用の値
	 */
	@Override
	public String getDisplayValue(PartsRadio p, PartsDesignRadio d, DesignerContext ctx) {
		final String val = p.getValue();
		return d.optionItems.stream()
				.filter(o -> same(o.value, val))
				.map(o -> o.label)
				.findFirst()
				.orElse("");
	}
}
