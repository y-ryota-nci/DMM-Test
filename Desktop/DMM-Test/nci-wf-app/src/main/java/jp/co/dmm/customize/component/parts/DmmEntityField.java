package jp.co.dmm.customize.component.parts;

import java.io.Serializable;

/**
 * 行に対するフィールド(列)データ
 */
public class DmmEntityField implements Serializable {
	/** カラム名 */
	public String columnName;
	/** 対応するパーツコード */
	public String partsCode;
	/** 値 */
	public String value;
}
