package jp.co.nci.iwf.designer.parts;

import java.io.Serializable;

import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツの選択肢項目
 */
public class PartsOptionItem implements Serializable {

	/** 値 */
	public String value;
	/** ラベル */
	public String label;

	/** コンボボックス用初期値 */
	public static final PartsOptionItem EMPTY = new PartsOptionItem("", "--");
	/** ラジオボタン用初期値 */
	public static final PartsOptionItem SAMPLE_1 = new PartsOptionItem("1", "Sample-1");
	public static final PartsOptionItem SAMPLE_2 = new PartsOptionItem("2", "Sample-2");
	public static final PartsOptionItem SAMPLE_3 = new PartsOptionItem("3", "Sample-3");

	/** デフォルトコンストラクタ */
	public PartsOptionItem() {}
	/** コンストラクタ */
	public PartsOptionItem(String value, String label) {
		this.value = MiscUtils.defaults(value, "");
		this.label = MiscUtils.defaults(label, "");
	}

}
