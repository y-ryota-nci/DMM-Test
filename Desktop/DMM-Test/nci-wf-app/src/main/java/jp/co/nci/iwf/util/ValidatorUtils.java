package jp.co.nci.iwf.util;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * バリデーションヘルパー
 */
public class ValidatorUtils extends MiscUtils {
	private static final Charset MS932 = Charset.forName("MS932");
	private static final Charset UTF8 = StandardCharsets.UTF_8;
	private static final BigDecimal MAX = new BigDecimal(Long.MAX_VALUE);
	private static final BigDecimal MIN = new BigDecimal(Long.MIN_VALUE);

	private static final Pattern NUMERIC = Pattern.compile("^[+,-]?[\\d\\.]*$");
	private static final Pattern ALPHABET_ONLY = Pattern.compile("^[a-zA-Z]*$");
	private static final Pattern ALPHA_NUMBER = Pattern.compile("^[a-zA-Z0-9]*$");
	private static final Pattern ALPHA_NUMBER_UNDERSCORE = Pattern.compile("^[a-zA-Z0-9_]*$");
	private static final Pattern ALPHA_NUMBER_SYMBOL = Pattern.compile("^[ -~]*$");
	private static final Pattern ALPHA_SYMBOL = Pattern.compile("^[ -/:-~]*$");
	private static final Pattern ALPHA_NUMBER_SYMBOL_LF = Pattern.compile("^[ -~/\n]*$");
	private static final Pattern FULL_KANA_ONLY = Pattern.compile("^[ァ-ヶー 　]*$");
	private static final Pattern FULL_WIDTH_ONLY = Pattern.compile("^[^ -~｡-ﾟ]*$");
	private static final Pattern HALF_KANA_ONLY = Pattern.compile("^[｡-ﾟ ]*$");
	private static final Pattern HALF_WIDTH_ONLY = Pattern.compile("^[ -~｡-ﾟ]*$");
	private static final Pattern INTEGER = Pattern.compile("^[+,-]?\\d+$");
	private static final Pattern IP_ADDR = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
	private static final Pattern EMAIL = Pattern.compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9\\-]{2,})+$");
	private static final Pattern NUMBER_ONLY = Pattern.compile("^\\d*$");
	private static final Pattern POSTCODE_JP = Pattern.compile("^(\\d{3}-\\d{4}|\\d{7})$");
	private static final Pattern TEL_JP = Pattern.compile("^[0-9][0-9-]{1,19}$");
	private static final Pattern YM = Pattern.compile("^\\d{4}/\\d{2}$");
	private static final Pattern YMD = Pattern.compile("^\\d{4}/\\d{2}/\\d{2}$");
	private static final Pattern TIME = Pattern.compile("^\\d{2}:\\d{2}$");
	private static final Pattern FILENAME = Pattern.compile(
			"[\\x00-\\x1f<>:\"/\\\\|?*]" +
        	"|^(CON|PRN|AUX|NUL|COM[0-9]|LPT[0-9]|CLOCK\\$)(\\.|$)" +
        	"|^[\\. ]$");


	/** 必須 */
	public static boolean required(Map<String, String> values) {
		long count = values.values().stream().filter(v -> isNotEmpty(v)).count();
		return count > 0;
	}

	/** 最低桁数 */
	public static boolean minlength(String val, int minLength) {
		if (isEmpty(val))
			return true;

		final int len = val.codePointCount(0, val.length());
		return (len >= minLength);
	}

	/** 最大桁数 */
	public static boolean maxlength(String val, int maxLength) {
		if (isEmpty(val))
			return true;

		final int len = val.codePointCount(0, val.length());
		return (len <= maxLength);
	}

	/** Shift-JISの最大バイト数 */
	public static boolean maxbyteSJIS(String val, int maxByte) {
		if (isEmpty(val))
			return true;

		int len = val.getBytes(MS932).length;
		return len <= maxByte;
	}

	/** UTF-8の最大バイト数 */
	public static boolean maxbyteUTF8(String val, int maxByte) {
		if (isEmpty(val))
			return true;

		int len = val.getBytes(UTF8).length;
		return len <= maxByte;
	}

	/** 数値 */
	public static boolean isNumeric(String val) {
		if (isEmpty(val))
			return true;

		if (NUMERIC.matcher(val).matches()) {
			try {
				new BigDecimal(val);
				return true;
			}
			catch (NumberFormatException e) {
				return false;
			}
		}
		return false;
	}

	/** 整数 */
	public static boolean isInteger(String val) {
		if (isEmpty(val))
			return true;

		if (INTEGER.matcher(val).matches()) {
			try {
				new BigDecimal(val);
				return true;
			}
			catch (NumberFormatException e) {
				return false;
			}
		}
		return false;
	}

	/** 最小値 */
	public static boolean isGTE(BigDecimal v, BigDecimal minimum) {
		if (v == null)
			return true;

		final BigDecimal min = defaults(minimum, MIN);
		return MiscUtils.compareTo(v, min) >= 0;
	}

	/** 最大値 */
	public static boolean isLTE(BigDecimal v, BigDecimal maximum) {
		if (v == null)
			return true;

		final BigDecimal max = defaults(maximum, MAX);
		return MiscUtils.compareTo(v, max) <= 0;
	}

	/** 範囲 */
	public static boolean isBetween(BigDecimal v, BigDecimal from, BigDecimal to) {
		if (v == null)
			return true;

		final BigDecimal min = defaults(from, MIN);
		final BigDecimal max = defaults(to, MAX);
		return MiscUtils.between(v, min, max);
	}

	/** アルファベットのみ */
	public static boolean isAlphabetOnly(String val) {
		if (isEmpty(val))
			return true;

		return ALPHABET_ONLY.matcher(val).matches();
	}

	/** 英数字のみ */
	public static boolean isAlphaNumber(String val) {
		if (isEmpty(val))
			return true;

		return ALPHA_NUMBER.matcher(val).matches();
	}

	/** 英数字アンダースコアのみ */
	public static boolean isAlphaNumberUnderscore(String val) {
		if (isEmpty(val))
			return true;

		return ALPHA_NUMBER_UNDERSCORE.matcher(val).matches();
	}

	/** 英数字記号のみ */
	public static boolean isAlphaNumberSymbol(String val) {
		if (isEmpty(val))
			return true;

		return ALPHA_NUMBER_SYMBOL.matcher(val).matches();
	}

	/** 英字記号のみ */
	public static boolean isAlphaSymbol(String val) {
		if (isEmpty(val))
			return true;

		return ALPHA_SYMBOL.matcher(val).matches();
	}

	/** 英数字記号＋改行 */
	public static boolean isAlphaNumberSymbolLf(String val) {
		if (isEmpty(val))
			return true;

		return ALPHA_NUMBER_SYMBOL_LF.matcher(val).matches();
	}

	/** 全角カナのみ */
	public static boolean isFullKanaOnly(String val) {
		if (isEmpty(val))
			return true;

		return FULL_KANA_ONLY.matcher(val).matches();
	}

	/** 全角のみ */
	public static boolean isFullWidthOnly(String val) {
		if (isEmpty(val))
			return true;

		return FULL_WIDTH_ONLY.matcher(val).matches();
	}

	/** 半角カナのみ */
	public static boolean isHalfKanaOnly(String val) {
		if (isEmpty(val))
			return true;

		return HALF_KANA_ONLY.matcher(val).matches();
	}

	/** 半角のみ */
	public static boolean isHalfWidthOnly(String val) {
		if (isEmpty(val))
			return true;

		return HALF_WIDTH_ONLY.matcher(val).matches();
	}

	/** IPアドレス */
	public static boolean isIpAddr(String val) {
		if (isEmpty(val))
			return true;

		return IP_ADDR.matcher(val).matches();
	}

	/** メールアドレス */
	public static boolean isMail(String val) {
		if (isEmpty(val))
			return true;

		return EMAIL.matcher(val).matches();
	}

	/** 数字のみ */
	public static boolean isNumberOnly(String val) {
		if (isEmpty(val))
			return true;

		return NUMBER_ONLY.matcher(val).matches();
	}

	/** 郵便番号(日本) */
	public static boolean isPostcodeJP(String val) {
		if (isEmpty(val))
			return true;

		return POSTCODE_JP.matcher(val).matches();
	}

	/** 電話番号（日本） */
	public static boolean isTelJP(String val) {
		if (isEmpty(val))
			return true;

		return TEL_JP.matcher(val).matches();
	}

	/** 年月 */
	public static boolean isYM(String val) {
		if (isEmpty(val))
			return true;

		if (YM.matcher(val).matches()) {
			try {
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
				sdf.setLenient(false);
				sdf.parse(val);
				return true;
			}
			catch (ParseException e) {
				return false;
			}
		}
		return false;
	}

	/** 年月日(yyyy/MM/dd) */
	public static boolean isYMD(String val) {
		if (isEmpty(val))
			return true;

		if (YMD.matcher(val).matches()) {
			try {
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				sdf.setLenient(false);
				sdf.parse(val);
				return true;
			}
			catch (ParseException e) {
				return false;
			}
		}
		return false;
	}

	/** 時刻(HH:mm) */
	public static boolean isTime(String val) {
		if (isEmpty(val))
			return true;

		if (TIME.matcher(val).matches()) {
			try {
				final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				sdf.setLenient(false);
				sdf.parse(val);
				return true;
			}
			catch (ParseException e) {
				return false;
			}
		}
		return false;
	}

	/** 小数点桁数の超過 */
	public static boolean isMaxDecimalPoint(BigDecimal v, Integer decimalPlaces) {
		if (v == null || decimalPlaces == null || decimalPlaces.intValue() < 0)
			return true;

		// 1.00など、小数部が0なら小数部が何桁あろうとも小数部が無いものとして扱う
		if (v.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0)
			return true;

		return v.scale() <= decimalPlaces;
	}

	/** URLのフォーマット */
	public static boolean isValidURL(String val) {
		if (isEmpty(val))
			return true;
		try {
			new URL(val);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

	/** 正しいファイル名か */
	public static boolean isFilename(String val) {
		if (isEmpty(val))
			return true;

		// 不正な文字が使用されていれば正しくない
		return !FILENAME.matcher(val).matches();
	}
}
