package jp.co.nci.iwf.util;

import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import jp.co.nci.integrated_workflow.common.StringTruncator;
import jp.co.nci.iwf.component.accesslog.ScreenInfo;
import jp.co.nci.iwf.jersey.provider.JacksonConfig;

/**
 * 雑多なユーティリティ
 */
public abstract class MiscUtils {
	/** 日付書式 */
	public static final String FORMAT_DATE = "yyyy/MM/dd";
	/** 日付時刻書式 */
	public static final String FORMAT_DATETIME = "yyyy/MM/dd HH:mm:ss";

	/** SimpleDateFormatをスレッドセーフにするための仕掛け */
	public static final ThreadLocal<DateFormat> FORMATTER_DATE = new ThreadLocal<DateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(FORMAT_DATE);
		}
	};

	/** SimpleDateFormatをスレッドセーフにするための仕掛け */
	public static final ThreadLocal<DateFormat> FORMATTER_DATETIME = new ThreadLocal<DateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(FORMAT_DATETIME);
		}
	};

	static {
		ConvertUtils.register(new SqlTimestampConverter(null), Timestamp.class);
	}

	/** 有効期間の最大値 */
	public static final java.sql.Date ENDDATE = toDate("2099/12/31", "yyyy/MM/dd");
	/** 有効期間の最少値 */
	public static final java.sql.Date STARTDATE = toDate("1900/01/01", "yyyy/MM/dd");


	/**  URI＋QueryStringを返す */
	public static String getURL(HttpServletRequest req) {
		final StringBuilder url = new StringBuilder(req.getRequestURI());
		final String queryString = req.getQueryString();
		if (StringUtils.isNotEmpty(queryString)) {
			url.append("?").append(queryString);
		}
		return url.toString();
	}

	/**
	 * URIを 「contextPath + /page/」を起点とした相対パスで返す（UriInfo().getPath()に等しい）
	 * @return "up/up0020.html" など /page/を起点とした相対パス
	 **/
	public static String getRelativePageURL(HttpServletRequest req) {
		String url = getURL(req);
		String root = req.getContextPath();
		String baseURL;
		if (isEmpty(root) || "/".equals(root))
			baseURL = "/page/";
		else
			baseURL = root + "/page/";

		if (url.contains(baseURL))
			return url.substring(baseURL.length());
		if (url.equals(root) || url.equals(root + "/"))	// URLにコンテキストルートが指定されている
			return "";
		throw new IllegalArgumentException("相対パスが解決できません。/page/以下にないコンテンツです。" + url);
	}

//	/**  URI＋QueryStringを返す */
//	public static String getURL(UriInfo uriInfo) {
//		final StringBuilder url = new StringBuilder(uriInfo.getRequestUri().toString());
//		final MultivaluedMap<String, String> params = uriInfo.getQueryParameters(true);
//		if (!params.isEmpty()) {
//			int i = 0;
//			for (String key : params.keySet()) {
//				url.append(i++ == 0 ? "?" : ":");
//				url.append(key).append("=").append(params.get(key));
//			}
//		}
//		return uriInfo.getRequestUri().toString();
//	}

	/** 空か（文字列のみ、空文字列も空として扱う） */
	public static boolean isEmpty(Object s) {
		if (s instanceof CharSequence)
			return StringUtils.isEmpty(s.toString());
		if (s instanceof Collection)
			return ((Collection<?>)s).isEmpty();
		if (s instanceof Map)
			return ((Map<?, ?>)s).isEmpty();
		if (s != null && s.getClass().isArray()) {
			return Array.getLength(s) == 0;
		}
		return (s == null);
	}

	/** 空でないか（文字列のみ、空文字列も空として扱う） */
	public static boolean isNotEmpty(Object s) {
		return !isEmpty(s);
	}

	/** 等価か */
	public static <T> boolean eq(T t1, T t2) {
		if (t1 == null)
			return (t2 == null);
		return t1.equals(t2);
	}

	/** 等価か（NULLを空文字とみなして比較） */
	public static boolean same(String s1, String s2) {
		return eq(defaults(s1, ""), defaults(s2, ""));
	}

	/** 比較 */
	public static <T extends Comparable<T>> int compareTo(T t1, T t2) {
		// compareTo()は、実装によっては引数側が nullだと落ちるので、事前にNull判定が必要だ
		if (t1 == null)
			return (t2 == null ? 0 : -1);
		if (t2 == null)
			return 1;
		return t1.compareTo(t2);
	}

	/**
	 * 範囲１と範囲２が重複しているか（範囲１(from1,to1) ∩ 範囲２(from2,to2)）
	 * @param from1 範囲１の開始
	 * @param to1 範囲１の終了
	 * @param to2 範囲２の開始
	 * @param from2 範囲２の終了
	 * @return 範囲が重複していればtrue
	 */
	public static <T extends Comparable<T>> boolean overlap(T from1, T to1, T from2, T to2) {
		if (from1 == null || to1 == null || from2 == null || to2 == null)
			return false;
		final Range<T> r1 = new Range<T>(from1, to1);
		final Range<T> r2 = new Range<T>(from2, to2);
		return overlap(r1, r2);
	}

	/**
	 * 範囲１と範囲２が重複しているか（範囲１ ∩ 範囲２）
	 * @param r1 範囲１
	 * @param r2 範囲２
	 * @return 範囲が重複していればtrue
	 */
	public static <T extends Comparable<T>> boolean overlap(Range<T> r1, Range<T> r2) {
		return compareTo(r1.from, r2.to) <= 0
				&& compareTo(r1.to, r2.from) >= 0;
	}

	/**
	 * 範囲１が範囲２を包含するか（範囲１ ⊇ 範囲２）
	 * @param from1 範囲１の開始
	 * @param to1 範囲１の終了
	 * @param to2 範囲２の開始
	 * @param from2 範囲２の終了
	 * @return 範囲１ ⊇ 範囲２ ならtrue
	 */
	public static <T extends Comparable<T>> boolean include(T from1, T to1, T from2, T to2) {
		if (from1 == null || to1 == null || from2 == null || to2 == null)
			return false;
		final Range<T> r1 = new Range<T>(from1, to1);
		final Range<T> r2 = new Range<T>(from2, to2);
		return include(r1, r2);
	}

	/**
	 * 範囲１が範囲２を包含するか（範囲１ ⊇ 範囲２）
	 * @param r1 範囲１
	 * @param r2 範囲２
	 * @return 範囲１ ⊇ 範囲２ ならtrue
	 */
	public static <T extends Comparable<T>> boolean include(Range<T> r1, Range<T> r2) {
		return compareTo(r1.from, r2.from) <= 0
				&& compareTo(r1.to, r2.to) >= 0;
	}

	/** 範囲内であるか */
	public static <T extends Comparable<T>> boolean between(T val, T from, T to) {
		if (val == null || from == null || to == null)
			return false;
		return from.compareTo(val) <= 0
				&& val.compareTo(to) <= 0;
	}

	/** 値 t は valuesの要素と一致するものが１つでもあれば true。（SQLのIN句に相当） */
	@SuppressWarnings("unchecked")
	public static <T> boolean in(T t, T... values) {
		for (T v : values) {
			if (eq(t, v))
				return true;
		}
		return false;
	}

	/** コレクション targets に valuesの要素と一致するものが１つでもあれば true。（SQLのIN句に相当） */
	@SuppressWarnings("unchecked")
	public static <T> boolean in(Collection<T> targets, T...values) {
		if (targets == null || values.length == 0)
			return false;

		for (T v : values) {
			if (targets.contains(v))
				return true;
		}
		return false;
	}

	/** 値 t は valuesの要素と一致するものが１つでもあれば true。（SQLのIN句に相当） */
	public static <T> boolean in(T t, Collection<T> values) {
		for (T v : values) {
			if (eq(t, v))
				return true;
		}
		return false;
	}

	/** 本日00:00:00.000を返す（操作者のタイムゾーンで） */
	public static java.sql.Date today() {
		return today(null);
	}

	/** 本日00:00:00.000を返す（指定タイムゾーンで） */
	public static java.sql.Date today(TimeZone tz) {
		Calendar cal = Calendar.getInstance();
		if (tz != null) cal.setTimeZone(tz);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	/** 現時刻を返す */
	public static java.sql.Date now() {
		return new java.sql.Date(System.currentTimeMillis());
	}

	/** 現時刻を返す */
	public static Timestamp timestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/** 日付の加算 */
	public static java.sql.Date addDay(Date d, int day) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, day);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	/** 月の加算 */
	public static java.sql.Date addMonth(Date d, int month) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MONTH, month);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	/** 年の加算 */
	public static java.sql.Date addYear(Date d, int year) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.YEAR, year);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	/** 時間の加算 */
	public static java.sql.Date addHour(Date d, int hours) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.HOUR, hours);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	/** 分の加算 */
	public static java.sql.Date addMinutes(Date d, int minutes) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MINUTE, minutes);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	/** 強制文字列化 */
	public static String toStr(Object o) {
		if (o == null) return null;
		if (o instanceof String)
			return (String)o;
		return o.toString();
	}

	/** 日付 -> 文字(個別に書式指定) */
	public static String toStr(Date d, String format) {
		// ミリ秒やナノ秒の精度が必要なら、MiscUtils.formatAsTimestamp()を使うべし
		if (d == null) return null;
		return new SimpleDateFormat(format).format(d);
	}

	/** 日付 -> 文字(yyyy/MM/dd) */
	public static String toStr(Date d) {
		// ミリ秒やナノ秒の精度が必要なら、MiscUtils.formatAsTimestamp()を使うべし
		if (d == null) return null;
		return FORMATTER_DATE.get().format(d);
	}

	/** タイムスタンプ -> 文字("yyyy/MM/dd HH:mm:ss") */
	public static String toStr(Timestamp d) {
		// ミリ秒やナノ秒の精度が必要なら、MiscUtils.formatAsTimestamp()を使うべし
		return toStrTimestamp(d);
	}

	/** 日付 -> 文字("yyyy/MM/dd HH:mm:ss") */
	public static String toStrTimestamp(Date d) {
		// ミリ秒やナノ秒の精度が必要なら、MiscUtils.formatAsTimestamp()を使うべし
		if (d == null) return null;
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(d);
	}

	/** 数値 -> 文字(個別に書式指定) */
	public static String toStr(Number n, String format) {
		if (n == null) return null;
		return new DecimalFormat(format).format(n);
	}

	/** 数値 -> 文字(汎用数値書式) */
	public static String toStr(Number n) {
		if (n == null) return null;
		return NumberFormat.getNumberInstance().format(n);
	}

	/** 真偽値 -> 文字(真なら"1"、偽なら"0") */
	public static String toStr(Boolean b) {
		return b != null && b.booleanValue() ? "1" : "0";
	}

	/** コレクションや配列 -> 文字列 */
	public static <T> String toStr(Iterable<T> values) {
		StringBuilder sb = new StringBuilder(32);
		for (T value : values) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(value);
		}
		return sb.toString();
	}

	/** 文字 -> 日付 */
	public static java.sql.Date toDate(String s, String format) {
		if (StringUtils.isEmpty(s)) return null;
		try {
			final Date d = new SimpleDateFormat(format).parse(s);
			return new java.sql.Date(d.getTime());
		}
		catch (ParseException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 文字 -> タイムスタンプ */
	public static Timestamp toTimestamp(String s, String format) {
		if (StringUtils.isEmpty(s)) return null;
		try {
			final Date d = new SimpleDateFormat(format).parse(s);
			return new Timestamp(d.getTime());
		}
		catch (ParseException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 文字 -> Double */
	public static Double toDouble(String s) {
		if (StringUtils.isEmpty(s)) return null;
		try {
			return NumberFormat.getNumberInstance().parse(s).doubleValue();
		} catch (ParseException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 文字 -> Long */
	public static Long toLong(String s) {
		if (StringUtils.isEmpty(s)) return null;
		try {
			return NumberFormat.getIntegerInstance().parse(s).longValue();
		} catch (ParseException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** Object -> Long */
	public static Long toLong(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Number)
			return ((Number)obj).longValue();
		if (obj instanceof String)
			return toLong((String)obj);

		return toLong(obj.toString());
	}

	/** 文字 -> Integer */
	public static Integer toInt(String s) {
		if (StringUtils.isEmpty(s)) return null;
		try {
			return NumberFormat.getIntegerInstance().parse(s).intValue();
		} catch (ParseException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** Object -> Integer */
	public static Integer toInt(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Number)
			return ((Number)obj).intValue();
		if (obj instanceof String)
			return toInt((String)obj);

		return toInt(obj.toString());
	}

	/** 文字 -> BigDecimal */
	public static BigDecimal toBD(String s) {
		if (StringUtils.isEmpty(s)) return null;
		try {
			return new BigDecimal(s);
		} catch (NumberFormatException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** Object -> BigDecimal */
	public static BigDecimal toBD(Object o) {
		if (o == null)
			return null;
		if (o instanceof BigDecimal)
			return (BigDecimal)o;
		return new BigDecimal(o.toString());
	}

	/** 文字 -> Boolean */
	public static Boolean toBool(String s) {
		if (StringUtils.isEmpty(s)) return null;
		return "true".equalsIgnoreCase(s)
				|| "1".equals(s)
				|| "yes".equalsIgnoreCase(s)
				|| "on".equalsIgnoreCase(s)
				|| "y".equalsIgnoreCase(s);
	}

	/** java.util.Date -> java.sql.Date */
	public static java.sql.Date toSqlDate(java.util.Date d) {
		return (d == null ? null : new java.sql.Date(d.getTime()));
	}

	/**
	 * Timestampをナノ秒まで含めて文字列化（yyyy/MM/dd HH:mm:ss.SSSSSS） 。
	 * Timestamp型の内部保持しているのはナノ秒、SimpleDateFormatはミリ秒までの精度なので、桁が足りない。
	 * これを回避するためのカスタム実装である
	 * @param value
	 * @return
	 */
	public static String formatAsTimestamp(Timestamp value) {
		if (value == null)
			return "";
		final String d = new StringBuilder(32)
				.append(FORMATTER_DATETIME.get().format(value))
				.append(".")
				.append(value.getNanos())
				.toString();
		return d;
	}

	/**
	 * ナノ秒まで含んだ文字列をTimestamp化（yyyy/MM/dd HH:mm:ss.SSSSSS）
	 * Timestamp型の内部保持しているのはナノ秒、SimpleDateFormatはミリ秒までの精度なので、桁が足りない。
	 * これを回避するためのカスタム実装である
	 * @param value
	 * @return
	 */
	public static Timestamp parseAsTimestamp(String value) {
		try {
			if (StringUtils.isNotEmpty(value)) {
				final String[] values = value.split("\\.");
				if (values.length > 0) {
					final Date d = FORMATTER_DATETIME.get().parse(values[0]);
					final Timestamp tm = new Timestamp(d.getTime());
					if (values.length > 1) {
						final Integer nano = Integer.valueOf(values[1]);
						tm.setNanos(nano);
					}
					return tm;
				}
			}
			return null;
		}
		catch (ParseException e) {
			throw new InternalServerErrorException(e.getMessage(), e);
		}
	}

	/** Object間でプロパティ値をコピー（シャローコピー） */
	public static void copyProperties(Object src, Object dest) {
		try {
			BeanUtils.copyProperties(dest, src);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** Object間でpublicフィールド値をコピー（シャローコピー） */
	public static void copyFields(Object src, Object dest) {
		if (src == null || dest == null) return;
		for (Field f : src.getClass().getFields()) {
			String name = f.getName();
			try {
				Object value = f.get(src);
				Field f2 = dest.getClass().getField(name);
				if (f2 != null)
					f2.set(dest, value);
			}
			catch (Exception e) {
				;	// 握りつぶす
			}
		}
	}

	/** Object間でフィールドまたはプロパティの値を読み込み、それを他方のフィールドまたはプロパティ値としてコピー*/
	public static void copyFieldsAndProperties(Object src, Object dest) {
		if (src == null || dest == null) return;
		// フィールドから値を取得し、フィールド／プロパティへセット
		for (Field f : src.getClass().getFields()) {
			String name = f.getName();
			if ("class".equals(name)) continue;	// for security

			try {
				final Object value = f.get(src);
				// フィールドへ
				try { dest.getClass().getField(name).set(dest, value); }
				catch (Exception e) {/* 握りつぶす */}
				// プロパティへ
				try { propUtil.setProperty(dest, name, value); }
				catch (Exception e) {/* 握りつぶす */}
			}
			catch (Exception e) {/* 握りつぶす */}
		}
		for (PropertyDescriptor pd : propUtil.getPropertyDescriptors(src.getClass())) {
			if (pd.getReadMethod() == null || pd.getWriteMethod() == null) continue;
			String name = pd.getName();
			if ("class".equals(name)) continue;		// for security

			try {
				Object value = propUtil.getProperty(src, name);
				// フィールドへ
				try { dest.getClass().getField(name).set(dest, value); }
				catch (Exception e) {/* 握りつぶす */}
				// プロパティへ
				try { propUtil.setProperty(dest, name, value); }
				catch (Exception e) {/* 握りつぶす */}
			}
			catch (Exception e) {/* 握りつぶす */}
		}
	}

	/** Objectにプロパティ値を設定 */
	public static void setProperty(Object obj, String name, Object val) {
		try {
			BeanUtils.setProperty(obj, name, val);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** Objectにフィールド値を設定 */
	public static boolean setFieldValue(Object obj, Field field, Object value) {
		if (obj == null || field == null)
			return true;

		try {
			field.set(obj, value);
			return true;
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

	/**
	 * キャメルケース化（アンダースコア(_)付文字列からBeanのプロパティ名を取得する）
	 * 　例)
	 * 　PROCESS_DEF_CODE ⇒ processDefCode
	 * 　A_BB_CCC  ⇒ aBbCcc
	 * 　abc ⇒ aBC
	 */
	public static String toCamelCase(String name) {
		if (name == null)
			return null;

		StringBuilder sb = new StringBuilder(name.length() * 2);
		final char[] chars = name.toLowerCase().toCharArray();
		boolean found = false;
		for (int i = 0 ; i < chars.length; i++) {
			final char c = chars[i];
			if (found) {
				sb.append(Character.toUpperCase(c));
				found = false;
			}
			else if (i != 0 && c == '_') {
				found = true;
			}
			else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/** トリミング、null値は nullのまま返す */
	public static String trim(String s) {
		return StringUtils.trim(s);
	}

	/** トリミング、null値は空文字にして返す */
	public static String trimToEmpty(String s) {
		return StringUtils.trimToEmpty(s);
	}

	/** 部分一致によるLIKE検索用にエスケープ ( like '%aaa%')   */
	public static String escapeLikeBoth(String value) {
		return "%" + escapeLike(value) + "%";
	}

	/** 前方一致によるLIKE検索用にエスケープ ( like 'aaa%')   */
	public static String escapeLikeFront(String value) {
		return escapeLike(value) + "%";
	}

	/** 後方一致によるLIKE検索用にエスケープ ( like '%aaa')  */
	public static String escapeLikeBack(String value) {
		return "%" + escapeLike(value);
	}

	/**
	 * DBのワイルドカード文字列をエスケープして返す（ワイルドカードは含まない。ワイルドカードが必要なら escapeLikeFront / escapeLikeBack / escapeLikeBoth を使用すること）
	 * @param str like条件に渡す文字列
	 * @return エスケープ後の文字列
	 * @see escapeLiteral()
	 */
	private static String escapeLike(String str) {
		if (isEmpty(str))
			return "";

//		// 全角％＿もエスケープする（Oracle11G R2.2まで）
//		return str.replaceAll("~", "~~").replaceAll("%", "~%").replaceAll("％", "~％").replaceAll("_", "~_").replaceAll("＿", "~＿");

		// 全角％＿はエスケープせず（Oracle11G R2.3以降）
		return str.replaceAll("~", "~~").replaceAll("%", "~%").replaceAll("_", "~_");
	}

	/**
	 * Endpointに対するリクエストから画面IDを求める。HTML
	 * @param uriInfo
	 * @return
	 */
	public static ScreenInfo toScreenInfo(HttpServletRequest req) {
		ScreenInfo inf = new ScreenInfo();
		if (req.getPathInfo() != null) {
			// Endpointに対するAjaxリクエスト
			String[] paths = req.getPathInfo().split("/");
			if (paths != null) {
				if (paths.length > 1)
					inf.setScreenId(paths[1].toUpperCase());
				if (paths.length > 2)
					inf.setActionName(paths[2]);
			}
		}
		else {
			String uri = req.getRequestURI();
			int end = uri.indexOf(".html");
			if (end > -1) {
				// 要求したのがHTMLである
				int start = uri.lastIndexOf("/");
				inf.setScreenId( uri.substring(start + 1, end).toUpperCase());
				inf.setActionName(null);
			}
		}
		return inf;
	}

	/** 正規表現のパターン文字列(検索文字列)をエスケープ  */
	public static String quotePattern(String s) {
		if (StringUtils.isEmpty(s)) return "";
		return Pattern.quote(s);
	}

	/** 正規表現の置換文字列をエスケープ  */
	public static String quoteReplacement(String s) {
		if (StringUtils.isEmpty(s)) return "";
		return Matcher.quoteReplacement(s);
	}

	/**
	 * 対象文字列を先頭から順に判定していき、
	 * nullでないものがあればその値を返す。
	 * すべてがnullならnull値を返す
	 * @param astr 対象値(可変長配列)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T defaults(T...array) {
		for (T obj : array) {
			if (obj != null)
				return obj;
		}
		return null;
	}

	/**
	 * ISO8859-1でエンコードされた文字列をUTF-8でエンコードしなおす。
	 * @param src
	 * @return
	 */
	public static String iso8859ToUtf8(String src) {
		if (src == null || src.length() == 0) return src;
		return new String(src.getBytes(StandardCharsets.ISO_8859_1),
				StandardCharsets.UTF_8);
	}

	/**
	 * UTF-8のバイト数を上限に、文字化けが発生しないよう文字をカット
	 * @param str 対象文字列
	 * @param byteCount UTF-8のバイト数
	 * @return
	 */
	public static String trunc(String str, int byteCount) {
		return StringTruncator.trunc(str, byteCount, StringTruncator.UTF8);
	}

	/** 日付時刻から時分秒を切り捨てる */
	public static java.util.Date trunc(java.util.Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(c.getTimeInMillis());
	}

	protected static final PropertyUtilsBean propUtil = new PropertyUtilsBean();

	/**
	 * Beanの属性をMapに変換
	 * @param bean
	 * @return
	 */
	public static Map<String, Object> toParams(Serializable bean) {
		return BeanToMapConveter.toMap(bean);
	}

	/**
	 * インスタンス化
	 * @param clazz
	 * @return
	 */
	public static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		}
		catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e
		) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * インスタンス化
	 * @param className
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className) {
		try {
			Class<T> clazz = (Class<T>)Class.forName(className);
			return newInstance(clazz);
		}
		catch (ClassNotFoundException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * 完全修飾クラス名からクラスを生成
	 * @param className 完全修飾クラス名
	 * @return
	 */
	public static Class<?> forName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** JSON文字列からオブジェクトへ変換 */
	public static <T> T toObjFromJson(String json, Class<T> clazz) {
		try {
			return JacksonConfig.getObjectMapper().readValue(json, clazz);
		}
		catch (IOException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** JSONデータのバイト配列をオブジェクトへ変換 */
	public static <E> E toObjFromJson(byte[] json, Class<E> clazz) throws IOException {
		try {
			return JacksonConfig.getObjectMapper().readValue(json, clazz);
		}
		catch (IOException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** オブジェクトからJSON文字列変換 */
	public static String toJsonFromObj(Object obj) {
		try {
			return JacksonConfig.getObjectMapper().writeValueAsString(obj);
		}
		catch (JsonProcessingException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** HTMLエスケープ（「"&<>」の4つに加え、OWASPに従って「'」もエスケープ） */
	public static String escapeHtml(CharSequence s) {
		if (s == null || s.length() == 0)
			return "";
		final StringBuilder out = new StringBuilder(Math.max(16, s.length() * 2));
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\'') out.append("&#39;");
			else if (c == '"') out.append("&quot;");
			else if (c == '&') out.append("&amp;");
			else if (c == '<') out.append("&lt;");
			else if (c == '>') out.append("&gt;");
			else out.append(c);
		}
		return out.toString();
	}

	/** HTMLエスケープしたうえで改行コードを BRタグへ変換 */
	public static String escapeHtmlBR(CharSequence s) {
		return escapeHtml(s).replaceAll("\n", "<br />");
	}

	/** 対象文字を指定数だけ繰り返し、その結果をすべて文字連結して返す */
	public static String repeat(String s, int repeat) {
		return repeat(s, null, repeat);
	}

	/** 対象文字を指定数だけ繰り返し、その結果をすべて文字連結して返す */
	public static String repeat(String s, String separator, int repeat) {
		return StringUtils.repeat(s, separator, repeat);
	}

	/**
	 * 属性の読取がフィールド直読みか、getter/setter経由かを判定
	 * @param bean
	 * @return フィールド直読みならtrue、getter/setter経由ならfalse
	 */
	public static boolean isUseField(Object bean) {
		if (bean == null)
			return true;

		// JPAエンティティでフィールドアクセスでアノテーションされているか
		final Access c = bean.getClass().getAnnotation(Access.class);
		if (c != null && c.value() != null && c.value() == AccessType.FIELD)
			return true;

		// PropertyDescriptorに読み書き両方のできる属性があれば getter/setterありとみなす
		final PropertyDescriptor descriptors[] = propUtil.getPropertyDescriptors(bean);
		for (PropertyDescriptor d : descriptors) {
			final String name = d.getName();
			if ("class".equals(name))	// getClass()は無視
				continue;
			if (propUtil.isReadable(bean, name) && propUtil.isWriteable(bean, name)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 対象のPropertyDescriptorを取得する
	 * @param bean
	 * @return
	 */
	public static PropertyDescriptor[] getPropertyDescriptors(Object bean) {
		return propUtil.getPropertyDescriptors(bean);
	}

	/**
	 * 対象のPropertyDescriptorを取得する
	 * @param clazz
	 * @return
	 */
	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
		return propUtil.getPropertyDescriptors(clazz);
	}

	/**
	 * 対象のPropertyDescriptorを取得する
	 * @param bean
	 * @param name
	 * @return
	 */
	public static PropertyDescriptor getPropertyDescriptor(Object bean, String name) {
		try {
			return propUtil.getPropertyDescriptor(bean, name);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * 対象から指定されたプロパティ値（getterメソッドの値）を取得
	 * @param bean 対象オブジェクト
	 * @param name プロパティ名
	 * @return プロパティの値
	 */
	public static <V> V getPropertyValue(Object bean, String name) {
		final PropertyDescriptor pd = getPropertyDescriptor(bean, name);
		return getPropertyValue(bean, pd);
	}

	/**
	 * 対象から指定されたプロパティ値（getterメソッドの値）を取得
	 * @param bean 対象オブジェクト
	 * @param name プロパティ名
	 * @return プロパティの値
	 */
	@SuppressWarnings("unchecked")
	public static <V> V getPropertyValue(Object bean, PropertyDescriptor pd) {
		try {
			return (V)pd.getReadMethod().invoke(bean);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * 対象のプロパティへ値をセット（setterメソッドをコール）
	 * @param bean 対象オブジェクト
	 * @param name プロパティ名
	 * @param value 値
	 */
	public static void setPropertyValue(Object bean, String name, Object value) {
		final PropertyDescriptor pd = getPropertyDescriptor(bean, name);
		if (pd == null)
			throw new NullPointerException(bean.getClass().getSimpleName() + "の PropertyDescriptor '" + name + "'は存在しません");
		setPropertyValue(bean, pd, value);
	}

	/**
	 * 対象のプロパティへ値をセット（setterメソッドをコール）
	 * @param bean 対象オブジェクト
	 * @param pd プロパティディクスリプター
	 * @param value 値
	 */
	public static void setPropertyValue(Object bean, PropertyDescriptor pd, Object value) {
		try {
			pd.getWriteMethod().invoke(bean, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * 対象のフィールド値へ値をセット
	 * @param bean
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <V> V getFieldValue(Object bean, String name) {
		if (bean == null)
			throw new NullPointerException();
		if (name == null)
			throw new NullPointerException();
		Field f;
		try {
			f = bean.getClass().getDeclaredField(name);
			V value = (V)f.get(bean);
			return value;
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/**
	 * 配列に該当する値があるか
	 * @param v 値
	 * @param array 配列リスト
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean contains(T v, T...array) {
		final Set<T> values = new HashSet<>(Arrays.asList(array));
		return values.contains(v);
	}

	/**
	 * オブジェクトをバイト配列へシリアライズ
	 * @param entity
	 * @return
	 */
	public static byte[] toBytes(Serializable entity) {
		if (entity == null)
			return null;

		// エンティティをバイト配列へシリアライズ
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)){
				oos.writeObject(entity);
				oos.flush();
				baos.flush();
				return baos.toByteArray();
			}
		}
		catch (IOException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** ストリームをバイト配列へ */
	public static byte[] toBytes(InputStream is) {
		if (is == null)
			return null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			byte[] buf = new byte[8192];
			try (BufferedInputStream bis = new BufferedInputStream(is)) {
				int len = 0;
				while ((len = bis.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
				return baos.toByteArray();
			}
		}
		catch (IOException e) {
			throw new InternalServerErrorException(e);
		}
	}

	/** 可変長配列を単純配列へ変換 */
	public static String[] asArray(String...values) {
		if (values == null)
			return new String[0];
		return values;
	}
	/** コレクションを単純配列へ変換 */
	public static String[] asArray(Collection<String> values) {
		if (values == null)
			return new String[0];
		return values.toArray(new String[values.size()]);
	}

	/** 可変長配列をリストへ変換 */
	@SuppressWarnings("unchecked")
	public static <E> List<E> asList(E...values) {
		final int size = (values == null) ? 16 : Math.max(16, values.length);
		final List<E> list = new ArrayList<>(size);
		if (values != null) {
			for (E v : values)
				list.add(v);
		}
		return list;
	}

	/** 可変長配列をSetへ変換 */
	@SuppressWarnings("unchecked")
	public static <E> Set<E> asSet(E...values) {
		final int size = (values == null) ? 16 : Math.max(16, values.length);
		final Set<E> sets = new HashSet<>(size);
		if (values != null) {
			for (E v : values)
				sets.add(v);
		}
		return sets;
	}

	/** 最小値を返す */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<T>> T min(T...targets) {
		T v = null;
		for (T t : targets) {
			if (t == null)
				continue;
			if (v == null)
				v = t;
			else if (compareTo(v, t) > 0)
				v = t;
		}
		return v;
	}

	/** 最大値を返す */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<T>> T max(T...targets) {
		T v = null;
		for (T t : targets) {
			if (t == null)
				continue;
			if (v == null)
				v = t;
			else if (compareTo(v, t) < 0)
				v = t;
		}
		return v;
	}

	/** ディープコピーの作成 */
	public static <T extends Serializable> T deepCopy(T src) {
		try {
			// Objectをバイト配列化
			final byte[] binary = toBytesFromObj(src);
			// バイト配列をObject化
			return toObjFromBytes(binary);
		}
		catch (IOException | ClassNotFoundException e) {
			throw new InternalServerErrorException("deepCopyの作成が出来ませんでした。", e);
		}
	}

	/** Objectをバイト配列に変換 */
	public static byte[] toBytesFromObj(Serializable src) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
				out.writeObject(src);
				return baos.toByteArray();
			}
		}
	}

	/** バイト配列をObjectに変換 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T toObjFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
			try (ObjectInputStream ois = new ObjectInputStream(bais)) {
				return (T)ois.readObject();
			}
		}
	}

	/** 全引数を文字列として連結 */
	public static String join(Object...objects) {
		StringBuilder sb = new StringBuilder(64);
		if (objects != null) {
			for (Object obj : objects) {
				sb.append(toStr(obj));
			}
		}
		return sb.toString();
	}

	/**
	 * フィールド型の型引数を返す。List＜String＞なら Stringが型引数。
	 * @param f フィールド
	 * @return 型引数
	 */
	@SuppressWarnings("unchecked")
	public static <C extends Type> C getParameterizedType(Field f) {
		final ParameterizedType pt = (ParameterizedType)f.getGenericType();
		final Type[] types = pt.getActualTypeArguments();
		if (types != null && types.length > 0) {
			final Type type = types[0];
			return (C)type;
		}
		return null;
	}
}
