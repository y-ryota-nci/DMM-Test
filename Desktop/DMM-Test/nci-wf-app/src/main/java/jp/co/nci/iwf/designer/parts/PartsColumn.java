package jp.co.nci.iwf.designer.parts;

import jp.co.nci.iwf.designer.DesignerCodeBook.ColumnType;
import jp.co.nci.iwf.jpa.entity.mw.MwmPartsColumn;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * パーツカラム
 */
public class PartsColumn extends MiscUtils implements Comparable<PartsColumn>{
	/** デフォルトコンストラクタ */
	public PartsColumn() {
	}

	/** コンストラクタ */
	public PartsColumn(MwmPartsColumn c) {
		partsId = c.getPartsId();
		partsColumnId = c.getPartsColumnId();
		columnName = c.getColumnName();
		columnSize = c.getColumnSize();
		columnType = c.getColumnType();
		comments = c.getComments();
		decimalPoint = c.getDecimalPoint();
		roleCode = c.getRoleCode();
		sortOrder = c.getSortOrder();
		columnTypeLabel = toString();
	}

	@Override
	public int compareTo(PartsColumn o) {
		int compare = compareTo(sortOrder, o.sortOrder);
		if (compare != 0)
			return compare;
		return compareTo(columnName, o.columnName);
	}


	/** パーツID */
	public Long partsId;
	/** パーツカラムID(PK) */
	public Long partsColumnId;
	/** カラム名 */
	public String columnName;
	/** カラム長 */
	public Integer columnSize;
	/** カラム型（1:VARCHAR, 2:NUMBER, 3:DATE, 4:TIMESTAMP, 5:CLOB, 6:BLOB） */
	public Integer columnType;
	/** コメント */
	public String comments;
	/** 小数点桁数 */
	public Integer decimalPoint;
	/** 役割コード */
	public String roleCode;
	/** 並び順 */
	public Integer sortOrder;
	/** バージョン */
	public Long version;
	/** カラム型ラベル */
	public String columnTypeLabel;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (ColumnType.VARCHAR == columnType) {
			sb.append("VARCHAR");
			if (columnSize != null)
				sb.append("(").append(columnSize).append(")");
		}
		else if (ColumnType.NUMBER == columnType) {
			sb.append("NUMERIC");
			if (columnSize != null) {
				sb.append("(").append(columnSize);
				if (decimalPoint == null)
					sb.append(", 0");
				else
					sb.append(", ").append(decimalPoint);
				sb.append(")");
			}
		}
		else if (ColumnType.DATE == columnType) {
			sb.append("DATE");
		}
		else if (ColumnType.TIMESTAMP == columnType) {
			sb.append("TIMESTAMP");
		}
		else if (ColumnType.CLOB == columnType) {
			sb.append("CLOB");
		}
		else if (ColumnType.BLOB == columnType) {
			sb.append("BLOB");
		}
		else {
			sb.append("UNKNOWN");
		}
		return sb.toString();
	}
}
