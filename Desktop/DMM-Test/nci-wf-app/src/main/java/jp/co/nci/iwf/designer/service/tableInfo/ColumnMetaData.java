package jp.co.nci.iwf.designer.service.tableInfo;

import java.io.Serializable;

/**
 * カラムのメタ情報
 */
public class ColumnMetaData implements Serializable {
	/** カラム名 */
	public String columnName;
	/** カラム型 */
	public String columnType;
	/** 桁数 */
	public Integer columnSize;
	/** 小数点桁数 */
	public Integer decimalPoint;
	/** NULL可 */
	public boolean isNotNull;
	/** デフォルト値 */
	public String defaultValue;
	/** プライマリキーか */
	public boolean isPrimaryKey;
	/** コメント */
	public String comment;
	/** カラムの並び順 */
	public Integer sortOrder;
	/** スキーマ */
	public String schema;
}
