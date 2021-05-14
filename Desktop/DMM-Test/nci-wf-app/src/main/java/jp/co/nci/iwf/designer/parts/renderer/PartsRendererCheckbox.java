package jp.co.nci.iwf.designer.parts.renderer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleCheckbox;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignCheckbox;
import jp.co.nci.iwf.designer.parts.runtime.PartsCheckbox;
import jp.co.nci.iwf.designer.service.CalculateCondition;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.jersey.SessionHolder;
import jp.co.nci.iwf.util.HtmlStringBuilder;

/**
 * チェックボックスパーツレンダラー
 */
@ApplicationScoped
public class PartsRendererCheckbox extends PartsRenderer<PartsCheckbox, PartsDesignCheckbox> implements RoleCheckbox{

	/** ログイン者情報 */
	@Inject
	protected SessionHolder sessionHolder;

	/**
	 * 入力項目としてHTMLを生成
	 * @param parts パーツ
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @param ec 有効条件
	 * @return
	 */
	@Override
	protected String renderInput(PartsCheckbox parts, PartsDesignCheckbox design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		// 計算式
		// ※チェックボックスは計算条件の判定元パーツになりうるのでCalculateConditionを取得する
		final CalculateCondition cc = getCalculate(parts, design, ctx);

		return this.render(parts, design, ctx, ec, cc, false);
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
	protected String renderReadonly(PartsCheckbox parts, PartsDesignCheckbox design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		// 読取専用時には有効条件、計算式は不要
		final CalculateCondition cc = null;
		return this.render(parts, design, ctx, ec, cc, true);
	}

	/**
	 * パーツ本体のHTMLを生成
	 * @param parts パーツ
	 * @param design パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @param ecCache 親コンテナの有効条件の判定結果を格納したマップ(キー:パーツのHtmlId)
	 * @param isReadOnly 読取専用ならtrue
	 * @return
	 */
	private String render(PartsCheckbox parts, PartsDesignCheckbox design, DesignerContext ctx, EvaluateCondition ec, CalculateCondition cc, boolean isReadOnly) {
		final boolean isDesignMode = RenderMode.DESIGN == ctx.renderMode;

		// Checkboxを囲むSPANへ htmlIdと有効条件、計算式を設定
		final HtmlStringBuilder html = new HtmlStringBuilder(256);

		// ラベル部分
		html.append("<label ");
		final String hidden = (design.renderAsHidden  && !isDesignMode) ? "hide" : "";
		html.append(toCssClassHtml(design.cssClass, "form-control-static", hidden));
		final List<String> styles = toCssStyleList(parts, design, ctx);
		html.append(toCssStyleHtml(design.cssStyle, styles));
		html.append(" >");	// end of <label>

		// CHECKBOX本体の出力開始
		html.append("<input type='checkbox'");

		// CHECKBOX本体のCSSクラス
		final String required = (!isDesignMode && design.requiredFlag) ? "required" : "";
		final String readonly = (design.readonly) ? "readonly" : "";
		html.appendProperty("class", ' ', Arrays.asList(required, readonly));

		// タブ順を付与
		if (!design.grantTabIndexFlag)
			html.appendProperty("tabindex", "-1");

		// 値
		// チェックありの時の値が指定されていればそれを、指定なしなら"1"をvalueとして設定する
		final String value = StringUtils.defaultIfBlank(design.checkedValue, CommonFlag.ON);
		html.appendProperty("value", value);

		// チェックの有無
		final String val = StringUtils.defaultString(parts.values.get(CHECK));
		final String checkedValue = StringUtils.defaultIfBlank(design.checkedValue, CommonFlag.ON);
		if (eq(checkedValue, val) || (isEmpty(val) && eq(CommonFlag.ON, design.defaultValue))) {
			html.appendProperty("checked");
		}

		// パーツ自身が読取専用か、または読取専用モードか、またはパーツ条件により無効ないし読取専用となっているか
		// 上記のどれかに合致する場合、パーツ自身を無効(非活性)にする
		if (design.readonly || isReadOnly || (ec != null && (!ec.enabled || ec.readonly)))
			html.appendProperty("disabled");

		// 役割コード
		html.appendProperty("data-role", "check");

		// 有効条件により活性・非活性を切り替えるための属性を付与
		// これがある要素が制御対象となる
		if (ec != null && ec.control)
			html.appendProperty( getEcTargetProperty() );

		// Ajaxのトリガーとなれるパーツなら属性を付与
		// これを設定しておくことでクライアント側で汎用マスタ検索パーツのトリガーパーツと認識してスクリプトが動く
		if (isNotEmpty(parts.ajaxTriggers))
			html.appendProperty("data-ajax-triggers");

		html.append(" />");
		// CHECKBOX本体の出力終了

		// ラベルのテキスト部分
		html.appendEscape(design.checkboxLabel);

		html.append("</label>");	// end of <label>

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
	public Object getPrintValue(PartsCheckbox p, PartsDesignCheckbox d, DesignerContext ctx) {
		if (eq(d.checkedValue, p.getValue())) {
			return d.checkboxLabel;
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
	public String getDisplayValue(PartsCheckbox p, PartsDesignCheckbox d, DesignerContext ctx) {
		if (eq(d.checkedValue, p.getValue())) {
			return d.checkboxLabel;
		}
		return "";
	}
}
