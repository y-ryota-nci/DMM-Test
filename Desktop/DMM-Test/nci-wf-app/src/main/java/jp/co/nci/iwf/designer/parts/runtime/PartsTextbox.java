package jp.co.nci.iwf.designer.parts.runtime;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.ws.rs.BadRequestException;

import jp.co.nci.iwf.component.MessageCd;
import jp.co.nci.iwf.designer.DesignerCodeBook.LengthType;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsCoodinationType;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsInputType;
import jp.co.nci.iwf.designer.DesignerCodeBook.PartsRoundType;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleTextbox;
import jp.co.nci.iwf.designer.DesignerCodeBook.ValidateType;
import jp.co.nci.iwf.designer.PartsUtils;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.RuntimeContext;
import jp.co.nci.iwf.designer.parts.design.PartsDesignTextbox;
import jp.co.nci.iwf.designer.service.EvaluateCondition;
import jp.co.nci.iwf.designer.service.PartsValidationResult;
import jp.co.nci.iwf.util.MiscUtils;
import jp.co.nci.iwf.util.ValidatorUtils;

/**
 * 【実行時】Textboxパーツ
 */
public class PartsTextbox extends PartsBase<PartsDesignTextbox> implements RoleTextbox {

	private static final BigDecimal PERCENT = new BigDecimal(100);

	/** パーツにユーザデータを反映 */
	@Override
	public void fromUserData(PartsDesignTextbox d, Map<String, Object> userData) {
		values.clear();
		for (PartsColumn pc : d.columns) {
			Object value = userData.get(pc.columnName);
			if (value instanceof BigDecimal && d.saveAsPercent) {
				// 値を％で格納されていたなら、100を掛ける
				value = ((BigDecimal)value).multiply(PERCENT);
			}
			else if (value instanceof Date) {
				// 日付
				if (eq(ValidateType.DATE, d.validateType))
					value = toStr((Date)value, "yyyy/MM/dd");
				else if (eq(ValidateType.YM, d.validateType))
					value = toStr((Date)value, "yyyy/MM");
				else
					;	// なんだこりゃ？
			}
			else if (value instanceof String) {
				// 値のトリム
				if (d.trimValue) {
					value = trim((String)value);
				}

				if (in(d.validateType, ValidateType.DATE, ValidateType.YM) && d.removeSlash) {
					// 保存時に '/' を除去してDBに書き込んでいるので、読み込み時に '/'を復元してやる
					String val = value.toString();
					if (val.length() == 6)
						value = val.substring(0, 4) + "/" + val.substring(4, 6);
					else if (val.length() == 8)
						value = val.substring(0, 4) + "/" + val.substring(4, 6) + "/" + val.substring(6);
				}
			}
			values.put(pc.roleCode, (value == null ? "" : value.toString()));
		}
	}

	/** パーツからユーザデータへ値を抜き出し */
	@Override
	public void toUserData(PartsDesignTextbox d, Map<String, Object> userData, RuntimeContext ctx, Map<String, EvaluateCondition> ecResults) {
		for (PartsColumn pc : d.columns) {
			Object val = null;
			{
				String value = values.get(pc.roleCode);

				// 値のトリム
				if (d.trimValue && value != null) {
					value = value.trim();
				}

				if (isEmpty(value)) {
					val = null;
				}
				else if (PartsInputType.NUMBER == d.inputType) {
					// 値を％で格納されているなら、100で割る
					if (d.saveAsPercent) {
						final int scale = MiscUtils.defaults(d.decimalPlaces, 0) + 2;
						final int roundType = MiscUtils.defaults(d.roundType, PartsRoundType.ROUND_HALF_UP);
						val = new BigDecimal(value).divide(PERCENT, scale, roundType);
					} else {
						val = new BigDecimal(value);
					}
				}
				else if (PartsInputType.DATE == d.inputType) {
					// 日付なら型変換
					if (eq(d.validateType, ValidateType.DATE))
						val = toDate(value, "yyyy/MM/dd");
					else if (eq(d.validateType, ValidateType.YM))
						val = toDate(value, "yyyy/MM");
					else
						throw new BadRequestException("型は日付ですが、バリデーションが日付以外になっています "
								+ "value=" + value + " validateType=" + d.validateType);
				}
				else if (in(d.validateType, ValidateType.DATE, ValidateType.YM) && d.removeSlash) {
					// 保存時に '/' を除去してDBに書き込み
					val = value.replaceAll("/", "");
				}
				else {
					val = value;
				}
			}
			userData.put(pc.columnName, val);
		}
	}

	/** バリデーション */
	@Override
	public PartsValidationResult validate(PartsDesignTextbox d, DesignerContext ctx, boolean checkRequired, Map<String, EvaluateCondition> ecResults) {
		final String val = values.get(TEXT);

		// 必須入力
		if (checkRequired && d.requiredFlag && MiscUtils.isEmpty(val)) {
			return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0074);
		}

		// 最大桁数／バイト数
		final int maxlength = MiscUtils.defaults(d.maxLength, 1000);
		if (LengthType.CHAR_LENGTH == d.lengthType) {
			// 桁数
			if (!ValidatorUtils.maxlength(val, maxlength))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0092, maxlength);
		}
		else if (LengthType.BYTE_SJIS == d.lengthType) {
			// Shift-JIS (MS932)
			if (!ValidatorUtils.maxbyteSJIS(val, maxlength))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0093, maxlength);
		}
		else if (LengthType.BYTE_UTF8 == d.lengthType) {
			// UTF-8
			if (!ValidatorUtils.maxbyteUTF8(val, maxlength))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0093, maxlength);
		}

		// 最少桁数
		final int minlength = MiscUtils.defaults(d.minLength, 0);
		if (minlength > 0 && !ValidatorUtils.minlength(val, minlength))
			return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0097, minlength);

		// 型
		if (PartsInputType.NUMBER == d.inputType) {
			// 数値の型／最大値／最少値／小数点桁数チェック
			final PartsValidationResult error = validateNumericOption(d, val);
			if (error != null)
				return error;
		}

		// フォーマット
		switch (d.validateType) {
		case ValidateType.ALPHA:
			// 英字のみ
			if (!ValidatorUtils.isAlphabetOnly(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0076);
			break;
		case ValidateType.ALPHA_NUMBER:
			// 英数字のみ
			if (!ValidatorUtils.isAlphaNumber(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0082);
			break;
		case ValidateType.ALPHA_SYMBOL:
			// 英数字または記号
			if (!ValidatorUtils.isAlphaSymbol(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0083);
			break;
		case ValidateType.ALPHA_SYMBOL_NUMBER:
			// 英数字または記号
			if (!ValidatorUtils.isAlphaNumberSymbol(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0084);
			break;
		case ValidateType.ALPHA_NUMBER_UNDERSCORE:
			// 英数字またはアンダースコア
			if (!ValidatorUtils.isAlphaNumberUnderscore(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0085);
			break;
		case ValidateType.ALPHA_SYMBOL_NUMBER_LF:
			// 英数字記号＋改行
			if (!ValidatorUtils.isAlphaNumberSymbolLf(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0086);
			break;
		case ValidateType.FULL_KANA_ONLY:
			// 全角カナのみ
			if (!ValidatorUtils.isFullKanaOnly(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0091);
			break;
		case ValidateType.FULL_WIDTH_ONLY:
			// 全角のみ
			if (!ValidatorUtils.isFullWidthOnly(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0078);
			break;
		case ValidateType.HALF_KANA_ONLY:
			// 半角カナのみ
			if (!ValidatorUtils.isHalfKanaOnly(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0079);
			break;
		case ValidateType.HALF_WIDTH_ONLY:
			// 半角のみ
			if (!ValidatorUtils.isHalfWidthOnly(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0080);
			break;
		case ValidateType.INTEGER:
			// 整数のみ
			if (!ValidatorUtils.isInteger(val)) {
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0081);
			}
			else {
				// 数値の型／最大値／最少値／小数点桁数チェック
				final PartsValidationResult error = validateNumericOption(d, val);
				if (error != null)
					return error;
			}
			break;
		case ValidateType.POSTCODE:
			// 郵便番号（日本）
			if (!ValidatorUtils.isPostcodeJP(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0087);
			break;
		case ValidateType.IPADDR:
			// IPアドレス
			if (!ValidatorUtils.isIpAddr(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0088);
			break;
		case ValidateType.TEL:
			// 電話番号
			if (!ValidatorUtils.isTelJP(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0089);
			break;
		case ValidateType.MAIL:
			// メール
			if (!ValidatorUtils.isMail(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0090);
			break;
		case ValidateType.NUMBER_ONLY:
			// 数字のみ
			if (!ValidatorUtils.isNumberOnly(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0077);
			break;
		case ValidateType.NUMERIC:
			// 数値（＋－小数点含む）
			if (!ValidatorUtils.isNumeric(val)) {
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0077);
			}
			else {
				// 数値の型／最大値／最少値／小数点桁数チェック
				final PartsValidationResult error = validateNumericOption(d, val);
				if (error != null)
					return error;
			}
			break;
		case ValidateType.YM:
			// 年月
			if (!ValidatorUtils.isYM(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0100);
			break;
		case ValidateType.DATE:
			// 年月日
			if (!ValidatorUtils.isYMD(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0075);
			break;
		case ValidateType.TIME:
			if (!ValidatorUtils.isTime(val))
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0131);
		case ValidateType.URL:
			// URL
			if (!ValidatorUtils.isValidURL(val))
				if (!ValidatorUtils.isYMD(val))
					return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0136, MessageCd.uri);
			break;
		}

		// 連動パーツに対するバリデーション
		if (d.partsIdFor != null && isNotEmpty(val) && isNotEmpty(d.coodinationType)) {
			final PartsBase<?> p = PartsUtils.findParts(d.partsIdFor, htmlId, ctx);
			// 期間の開始≦終了
			String from = null, to = null;
			if (eq(d.coodinationType, PartsCoodinationType.PERIOD_FROM)) {
				from = p.getValue();
				to = val;
			}
			if (eq(d.coodinationType, PartsCoodinationType.PERIOD_TO)) {
				from = val;
				to = p.getValue();
			}
			if (isNotEmpty(from) && isNotEmpty(to) && compareTo(from, to) > 0)
				return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0107);
		}
		return null;
	}

	/** 数値の追加バリデーション */
	private PartsValidationResult validateNumericOption(PartsDesignTextbox d, final String val) {
		if (MiscUtils.isEmpty(val))
			return null;

		// 数値とみなせるか
		if (!ValidatorUtils.isNumeric(val))
			return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0077);

		// 範囲
		final BigDecimal v = new BigDecimal(val);
		if (d.min != null && d.max != null && !ValidatorUtils.between(v, d.min, d.max))
			return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0104, d.min, d.max);

		// 最小値
		if (d.min != null && !ValidatorUtils.isGTE(v, d.min))
			return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0105, d.min);

		// 最大値
		if (d.max != null && !ValidatorUtils.isLTE(v, d.max))
			return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0106, d.max);

		// 小数点桁数の超過チェック
		if (d.decimalPlaces != null && !ValidatorUtils.isMaxDecimalPoint(v, d.decimalPlaces))
			return new PartsValidationResult(htmlId, TEXT, d.labelText, MessageCd.MSG0096, d.decimalPlaces);

		return null;
	}
}
