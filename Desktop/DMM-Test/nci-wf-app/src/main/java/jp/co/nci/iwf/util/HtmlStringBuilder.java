package jp.co.nci.iwf.util;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * HTML生成用のStringBuilder
 */
public class HtmlStringBuilder implements CharSequence {

	private StringBuilder sb;

	public int length() {
		return sb.length();
	}

	public int hashCode() {
		return sb.hashCode();
	}

	public IntStream chars() {
		return sb.chars();
	}

	public HtmlStringBuilder append(Object obj) {
		sb.append(obj);
		return this;
	}

	public HtmlStringBuilder append(CharSequence s) {
		sb.append(s);
		return this;
	}

	public char charAt(int index) {
		return sb.charAt(index);
	}

	public HtmlStringBuilder delete(int start, int end) {
		sb.delete(start, end);
		return this;
	}

	public HtmlStringBuilder deleteCharAt(int index) {
		sb.deleteCharAt(index);
		return this;
	}

	public int indexOf(String str) {
		return sb.indexOf(str);
	}

	public String toString() {
		return sb.toString();
	}

	public String substring(int start) {
		return sb.substring(start);
	}

	public CharSequence subSequence(int start, int end) {
		return sb.subSequence(start, end);
	}

	public String substring(int start, int end) {
		return sb.substring(start, end);
	}

	/** コンストラクタ */
	public HtmlStringBuilder() {
		sb = new StringBuilder();
	}

	/**
	 * コンストラクタ
	 * @param size 初期容量
	 */
	public HtmlStringBuilder(int capacity) {
		sb = new StringBuilder(capacity);
	}

	/**
	 * コンストラクタ
	 * @param str 初期値
	 */
	public HtmlStringBuilder(CharSequence str) {
		sb = new StringBuilder(str == null ? null : str.toString());
	}

	/**
	 * 属性を追加
	 * @param propName 属性名
	 * @param format 書式設定(String.format()の書式を参照）
	 * @param args 書式設定のパラメータ
	 * @return
	 */
	public HtmlStringBuilder appendProperty(String propName) {
		sb.append(" ").append(propName);
		return this;
	}

	/**
	 * 属性を追加
	 * @param propName 属性名
	 * @param value 値
	 * @return
	 */
	public HtmlStringBuilder appendProperty(String propName, Number value) {
		sb.append(" ").append(propName).append("='").append(value).append("'");
		return this;
	}

	/**
	 * 属性を追加
	 * @param propName 属性名
	 * @param value 値
	 * @return
	 */
	public HtmlStringBuilder appendProperty(String propName, CharSequence value) {
		sb.append(" ").append(propName).append("='").append(MiscUtils.escapeHtml(value)).append("'");
		return this;
	}

	/**
	 * 属性を追加
	 * @param propName 属性名
	 * @param value 値
	 * @return
	 */
	public HtmlStringBuilder appendProperty(String propName, Boolean value) {
		if (value != null) {
			sb.append(" ").append(propName).append("='").append(MiscUtils.escapeHtml(value.toString())).append("'");
		}
		return this;
	}

	/**
	 * 属性を追加
	 * @param propName 属性名
	 * @param value 値
	 * @return
	 */
	public HtmlStringBuilder appendProperty(String propName, Date value) {
		sb.append(" ").append(propName).append("='").append(value).append("'");
		return this;
	}

	/**
	 * 属性を追加
	 * @param propName 属性名
	 * @param separator コレクションを連結するときのセパレータ。
	 * @param values 値のコレクション。指定されたセパレータで連結
	 * @return
	 */
	public HtmlStringBuilder appendProperty(String propName, char separator, Collection<String> values) {
		appendProperty(propName, separator, values.toArray(new String[values.size()]));
		return this;
	}

	/**
	 * 属性を追加
	 * @param propName 属性名
	 * @param format 書式設定(String.format()の書式を参照）
	 * @param args 書式設定のパラメータ
	 * @return
	 */
	public HtmlStringBuilder appendProperty(String propName, char separator, String... args) {
		if (args != null && args.length > 0) {
			String value = Stream.of(args)
					.filter(StringUtils::isNotEmpty)
					.collect(Collectors.joining(String.valueOf(separator)));
			sb.append(" ").append(propName).append("='");
			sb.append(MiscUtils.escapeHtml(value));
			sb.append("'");
		}
		return this;
	}

	/**
	 * 値をHTML Escapeして追加
	 * @param s
	 * @return
	 */
	public HtmlStringBuilder appendEscape(String s) {
		if (s != null && s.length() > 0)
			sb.append(MiscUtils.escapeHtml(s));
		return this;
	}

	/**
	 * 値をHTML Escapeして追加
	 * @param s
	 * @return
	 */
	public HtmlStringBuilder appendEscape(Number s) {
		if (s != null)
			sb.append(MiscUtils.escapeHtml(String.valueOf(s)));
		return this;
	}

	/**
	 * 置換
	 * @param start 置換する開始INDEX
	 * @param end 置換する終了位置
	 * @param str 置換文字
	 */
	public HtmlStringBuilder replace(int start, int end, String str) {
		if (str != null && str.length() > 0)
			sb.replace(start, end, str);
		return this;
	}

	/** 値をフォーマットして追加 */
	public HtmlStringBuilder appendFormat(String format, Object...args) {
		if (format != null && format.length() > 0) {
			sb.append(String.format(format, args));
		}
		return this;
	}
}
