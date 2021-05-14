package jp.co.dmm.customize.component.parts;

import java.io.Serializable;
import java.util.List;

/**
 * 行データ
 */
public class DmmEntityRow implements Serializable {
	/** 行のフィールド情報（つまり行内の子パーツの値） */
	public List<DmmEntityField> fields;
}
