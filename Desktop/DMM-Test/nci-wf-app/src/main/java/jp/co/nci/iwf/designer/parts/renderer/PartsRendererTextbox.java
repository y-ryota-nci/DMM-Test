package jp.co.nci.iwf.designer.parts.renderer;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import jp.co.nci.iwf.designer.DesignerCodeBook.RoleTextbox;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignTextbox;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsTextbox;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.util.HtmlStringBuilder;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * Textboxパーツレンダラー
 */
@ApplicationScoped
public class PartsRendererTextbox
		extends PartsRenderer<PartsTextbox, PartsDesignTextbox>
		implements RoleTextbox {

	/**
	 * 数値の最大値。Javascriptが精度を保証できる最大値が 2^53(=9007199254740992)なので、それより１ケタ少なく
	 */
	private static final BigDecimal MAX = new BigDecimal(999999999999999L).setScale(0);

	/**
	 * 数値の最小値。Javascriptが精度を保証できる最小値が -2^53(=-9007199254740992)なので、それより１ケタ少なく
	 */
	private static final BigDecimal MIN = MAX.negate();

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
	protected String renderInput(PartsTextbox parts, PartsDesignTextbox design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final boolean isDesignMode = RenderMode.DESIGN == ctx.renderMode;
		final HtmlStringBuilder html = new HtmlStringBuilder(256);

		boolean isTextarea = false;
		if (PartsInputType.CLOB == design.inputType || PartsInputType.TEXTAREA == design.inputType) {
			html.append("<textarea");
			if (design.rowCount != null) {
				html.appendProperty("rows", design.rowCount);
			}
			isTextarea = true;
		}
		else if ((design.renderAsHidden  && !isDesignMode))
			html.append("<input type='hidden'");
		else
			html.append("<input type='text'");

		// CSSクラス
		final String red = isRedIfNegative(parts, design, ctx) ? "red" : "";
		final String right = (PartsInputType.NUMBER == design.inputType) ? "text-right" : "";
		final String hidden = (design.renderAsHidden  && !isDesignMode) ? "hide" : "";
		final String required = (!isDesignMode && design.requiredFlag) ? "required" : "";
		final String date = (design.useCalendarUI && ValidateType.DATE.equals(design.validateType)) ? "ymdPicker" : "";
		final String ym = (design.useCalendarUI && ValidateType.YM.equals(design.validateType)) ? "ymPicker" : "";
		final String imeMode = isEmpty(design.imeMode) ? "" : "ime-" + design.imeMode;
		final String readonly = (design.readonly) ? "readonly" : "";
		html.append(toCssClassHtml(design.cssClass, "form-control", red, right, hidden, required, date, ym, imeMode, readonly));

		// CSSスタイル
		final List<String> styles = toCssStyleList(parts, design, ctx);
		if (isDesignMode && design.renderAsHidden) {	// 隠し項目はデザイン時だけ見た目を変える
			if (isEmpty(design.bgColorInput)) {
			styles.add("background-color: transparent");
			}
			styles.add("border:2px dotted silver");
		}
		html.append(toCssStyleHtml(design.cssStyle, styles));

		// タブ順を付与
		if (!design.grantTabIndexFlag)
			html.appendProperty("tabindex", "-1");

		// 読取専用
		// パーツ自身が読取専用またはパーツ条件の読取専用条件の評価結果にて読取専用となった場合
		if (design.readonly || ec.readonly)
			html.appendProperty("readonly");

		// 値(Textbox)
		if (!isTextarea) {
			final String val = getFormatedValue(parts, design, ctx.renderMode);
			html.appendProperty("value", val);
		}

		// バリデーション
		if (ctx.renderMode != RenderMode.DESIGN) {
			final String json = toValidateJsonString(design, parts, ctx);
			html.appendProperty("data-validate", json);
		}

		// 最大桁数
		if (design.maxLength != null)
			html.appendProperty("maxlength", design.maxLength);

		// パーツ条件の評価結果に伴う活性／非活性の設定
		if (!ec.enabled)
			html.appendProperty("disabled");

		// 役割コード
		html.appendProperty("data-role", TEXT);

		// 有効条件により活性・非活性を切り替えるための属性を付与
		// これがある要素が制御対象となる
		if (ec.control)
			html.appendProperty( getEcTargetProperty() );

		// Ajaxのトリガーとなれるパーツなら属性を付与
		// これを設定しておくことでクライアント側で汎用マスタ検索パーツのトリガーパーツと認識してスクリプトが動く
		if (isNotEmpty(parts.ajaxTriggers))
			html.appendProperty("data-ajax-triggers");

		// 値(Textarea)
		if (isTextarea) {
			html.append(">");
			final String val = getFormatedValue(parts, design, ctx.renderMode);
			html.appendEscape(val);
			html.append("</textarea>");
		} else {
			html.append(" />");	// end of <input type="text">
		}

		// 日付の場合はそれと分かるよう、フィードバック用アイコンを追加
		if (hasFeedbackCssClass(design)) {
			if (design.useCalendarUI && in(design.validateType, ValidateType.DATE, ValidateType.YM))
				html.append("<i class='glyphicon glyphicon-calendar form-control-feedback'></i>");
			else if (ValidateType.TIME.equals(design.validateType))
				html.append("<i class='glyphicon glyphicon-time form-control-feedback'></i>");
		}
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
	protected String renderReadonly(PartsTextbox parts, PartsDesignTextbox design, DesignerContext ctx, Map<String, EvaluateCondition> ecCache, EvaluateCondition ec) {
		final boolean isDesignMode = RenderMode.DESIGN == ctx.renderMode;
		final HtmlStringBuilder html = new HtmlStringBuilder(256);

		html.append("<output ");
		// CSSクラス
		final String red = isRedIfNegative(parts, design, ctx) ? "red" : "";
		final String right = (PartsInputType.NUMBER == design.inputType) ? "text-right" : "";
		final String hidden = (design.renderAsHidden  && !isDesignMode) ? "hide" : "";
		html.append(toCssClassHtml(design.cssClass, "form-control-static", red, right, hidden));
		// CSSスタイル
		List<String> styles = toCssStyleList(parts, design, ctx);
		html.append(toCssStyleHtml(design.cssStyle, styles));

		html.append(">");

		// 値
		final String val = getFormatedValue(parts, design, ctx.renderMode);
		if (isNotEmpty(val)) {
			// テキストエリアなら改行コードを活かすため、エスケープしたうえで改行コードをBRタグへ変換
			if (PartsInputType.CLOB == design.inputType || PartsInputType.TEXTAREA == design.inputType) {
				html.append(MiscUtils.escapeHtmlBR(val));
			} else {
				html.appendEscape(val);
			}
		}

		html.append("</output>");
		return html.toString();
	}

	/** マイナス値なら赤に該当するか */
	private boolean isRedIfNegative(PartsTextbox parts, PartsDesignTextbox design, DesignerContext ctx) {
		if (!design.redIfNegative || PartsInputType.NUMBER != design.inputType)
			return false;

		String val;
		if (RenderMode.DESIGN == ctx.renderMode)
			val = design.defaultValue;
		else
			val = parts.values.get(TEXT);

		if (isEmpty(val))
			return false;

		try {
			return Double.valueOf(val) < 0;
		}
		catch (Exception e) {
			return false;
		}
	}

	/** バリデーション定義をJSON化して返す */
	private String toValidateJsonString(final PartsDesignTextbox design, final PartsTextbox parts, DesignerContext ctx) {
		final Map<String, Object> validate = new HashMap<>();

		// 型チェック
		if (isNotEmpty(design.validateType)) {
			validate.put("pattern", design.validateType);

			// 数値型
			if (PartsInputType.NUMBER == design.inputType) {
				// 数値フォーマット
				int numberFormat = defaults(design.numberFormat, PartsNumberFormat.N9999_9);
				if (PartsNumberFormat.N9999_0 == numberFormat) {
					// カンマ区切りなし＋小数点以下を０埋め
					validate.put("zeroPadRight", true);
					validate.put("notAddComma", true);
				}
				else if (PartsNumberFormat.N9999_9 == numberFormat) {
					// カンマ区切りなし＋小数点以下は入力値のまま
					validate.put("notAddComma", true);
				}
				else if (PartsNumberFormat.N9_999_0 == numberFormat) {
					// カンマ区切りあり＋小数点以下を０埋め
					validate.put("zeroPadRight", true);
				}
				else if (PartsNumberFormat.N9_999_9 == numberFormat) {
					// カンマ区切りあり＋小数点以下は入力値のまま
				}

				// 端数処理
				if (design.roundType != null)
					validate.put("roundType", design.roundType);

				// 最小値
				validate.put("min", max(design.min, MIN));
				// 最大値
				validate.put("max", min(design.max, MAX));
				// 小数点桁数
				if (design.decimalPlaces != null)
					validate.put("decimalPlaces", design.decimalPlaces);
				// マイナスなら赤字
				if (design.redIfNegative)
					validate.put("redIfNegative", true);
			}
		}

		// 日付バリデーション（文字列型でも日付としてバリデーションは出来るので、入力タイプではチェックできない）
		if (in(design.validateType, ValidateType.DATE, ValidateType.YM)) {
			// 対応するFROM日付のパーツが定義されていれば、そのパーツをバリデーション対象に含める
			if (design.partsIdFor != null) {
				final PartsBase<?> p = PartsUtils.findParts(design.partsIdFor, parts.htmlId, ctx);
				String selector = String.format("#%s > input", p.htmlId);
				if (eq(design.coodinationType, PartsCoodinationType.PERIOD_FROM))
					validate.put("from", selector);
				else if (eq(design.coodinationType, PartsCoodinationType.PERIOD_TO))
					validate.put("to", selector);
			}
		}

		// 桁数とバイト数
		final int lengthType = design.lengthType;
		final Integer maxLength = design.maxLength;
		if (maxLength != null) {
			if (LengthType.CHAR_LENGTH == lengthType) 		// 文字数
				validate.put("maxlength", maxLength);
			else if (LengthType.BYTE_SJIS == lengthType) 	// Shift-JIS換算のバイト数
				validate.put("maxByteSjis", maxLength);
			else if (LengthType.BYTE_UTF8 == lengthType) 	// UTF-8換算のバイト数
				validate.put("maxByteUtf8", maxLength);
		}
		final Integer minLength = design.minLength;
		if (minLength != null) {
			if (LengthType.CHAR_LENGTH == lengthType) 		// 文字数
				validate.put("minlength", minLength);
		}

		// プレフィックスとサフィックス
		if (isNotEmpty(design.prefix))
			validate.put("prefix", design.prefix);
		if (isNotEmpty(design.suffix))
			validate.put("suffix", design.suffix);

		return toJson(validate);
	}

	/** 出力値に適切な書式設定を付与して返す */
	private String getFormatedValue(PartsTextbox parts, PartsDesignTextbox design, RenderMode renderMode) {
		StringBuilder sb = new StringBuilder(32);
		String val = parts.getValue();
		if (isNotEmpty(val)) {
			if (isNotEmpty(design.prefix))
				sb.append(design.prefix);

			if (PartsInputType.NUMBER == design.inputType && design.numberFormat != null && design.numberFormat != 0)
				sb.append(toDecimalFormat(val, design));
			else
				sb.append(val);

			if (isNotEmpty(design.suffix))
				sb.append(design.suffix);
		}
		return sb.toString();
	}

	/** 出力値へ数値フォーマットを適用 */
	private String toDecimalFormat(String val, PartsDesignTextbox design) {
		if (isEmpty(val))
			return "";

		// -------------------------------------------------------------------
		// 当メソッドの動作は validate.js の _formatDecimalPoint() とまったく同じロジックで
		// 実装されているので扱いには注意すること。
		// -------------------------------------------------------------------

		// 端数の丸め処理
		final Integer roundType = design.roundType, decimalPlaces = design.decimalPlaces;
		final int MAX_DIGIT = 9;
		final BigDecimal value;
		if (roundType != null && decimalPlaces != null) {
			value = new BigDecimal(val).setScale(decimalPlaces, roundType);
		}
		else {
			value = new BigDecimal(val);
		}
		// フォーマット
		final boolean addComma = in(design.numberFormat, PartsNumberFormat.N9_999_0, PartsNumberFormat.N9_999_9);
		final boolean paddZero = in(design.numberFormat, PartsNumberFormat.N9999_0, PartsNumberFormat.N9_999_0);
		final NumberFormat f = NumberFormat.getNumberInstance();
		// 最小小数点桁数：ゼロ埋めするなら最大小数点桁数に等しい、ゼロ埋めしないなら最小小数点桁数は0
		f.setMinimumFractionDigits(paddZero ? decimalPlaces : 0);
		// 最大小数点桁数：端数処理するならオプションの小数点桁数のまま、端数処理しないなら小数点桁数の最大値である9
		f.setMaximumFractionDigits(roundType != null ? decimalPlaces : MAX_DIGIT);
		// カンマ区切り
		f.setGroupingUsed(addComma);
		return f.format(value.doubleValue());
	}

	/** パーツは 'input-group-feedback'の要素を内包するか（内包するなら位置解決のために親要素が CSSクラス'has-feedback'を持つ必要がある） */
	@Override
	protected boolean hasFeedbackCssClass(PartsDesignTextbox design) {
		// 日付などはTextbox内にアイコンを表示する
		return in(design.validateType, ValidateType.DATE, ValidateType.YM, ValidateType.TIME);
	}

	/**
	 * パーツの印刷用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキスト
	 * @return
	 */
	@Override
	public Object getPrintValue(PartsTextbox p, PartsDesignTextbox d, DesignerContext ctx) {
		return getFormatedValue(p, d, ctx.renderMode);
	}

	/**
	 * パーツの表示用の値を返す
	 * @param p パーツ
	 * @param d パーツ定義
	 * @param ctx レンダリング用コンテキス
	 * @return パーツの表示用の値
	 */
	@Override
	public String getDisplayValue(PartsTextbox p, PartsDesignTextbox d, DesignerContext ctx) {
		return p.getValue();
	}
}
