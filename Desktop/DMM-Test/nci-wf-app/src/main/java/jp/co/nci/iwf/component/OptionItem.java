package jp.co.nci.iwf.component;

import java.io.Serializable;
import java.util.Objects;

import jp.co.nci.iwf.util.MiscUtils;

/**
 * HTMLのOPTIONタグをJSON化するためのBean
 */
public class OptionItem implements Serializable {
	/** 値 */
	private String value;
	/** ラベル */
	private String label;
	/** title(ツールチップ) */
	private String title;


	public static final OptionItem EMPTY = new OptionItem("", "--");


	/** コンストラクタ */
	public OptionItem() {
	}

	/**
	 * コンストラクタ
	 * @param value 値
	 * @param label ラベル
	 */
	public OptionItem(String value, String label) {
		this.value = (value == null ? "" : value);
		this.label = label;
	}

	/**
	 * コンストラクタ
	 * @param value 値
	 * @param label ラベル
	 */
	public OptionItem(Number value, String label) {
		this.value = (value == null ? "" : value.toString());
		this.label = label;
	}

	/**
	 * コンストラクタ
	 * @param value 値
	 * @param label ラベル
	 * @param title タイトル(HTML上のツールチップ）
	 */
	public OptionItem(String value, String label, String title) {
		this.value = (value == null ? "" : value);
		this.label = label;
		this.title = title;
	}

	/**
	 * コンストラクタ
	 * @param value 値
	 * @param label ラベル
	 * @param title タイトル(HTML上のツールチップ）
	 */
	public OptionItem(Number value, String label, String title) {
		this.value = (value == null ? "" : value.toString());
		this.label = label;
		this.title = title;
	}


	/** 値 */
	public String getValue() {
		return value;
	}
	/** 値 */
	public void setValue(String value) {
		this.value = value;
	}

	/** ラベル */
	public String getLabel() {
		return label;
	}
	/** ラベル */
	public void setLabel(String label) {
		this.label = label;
	}

	/** title(ツールチップ) */
	public String getTitle() {
		return title;
	}
	/** title(ツールチップ) */
	public void setTitle(String title) {
		this.title = title;
	}

	/** 文字列化 */
	@Override
	public String toString() {
		return "[" + value + "] " + label;
	}

	/** ハッシュ値計算 */
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	/** 等価判定 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OptionItem) {
			return equals((OptionItem)obj);
		}
		return super.equals(obj);
	}

	/** 等価判定 */
	public boolean equals(OptionItem other) {
		if (other == null)
			return false;
		return MiscUtils.eq(value, other.value);
	}
}
