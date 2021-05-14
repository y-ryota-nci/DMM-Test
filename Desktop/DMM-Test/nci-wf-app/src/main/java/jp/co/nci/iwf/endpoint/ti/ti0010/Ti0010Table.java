package jp.co.nci.iwf.endpoint.ti.ti0010;

import java.util.Map;

import jp.co.nci.iwf.designer.service.tableInfo.TableMetaData;
import jp.co.nci.iwf.jpa.entity.mw.MwmTable;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * マスタ取込設定のテーブル情報
 */
public class Ti0010Table {
	public String tableName;
	public String comment;
	public String entityType;
	public Long tableId;
	public boolean selected;

	public Ti0010Table() {
	}

	public Ti0010Table(TableMetaData meta, Map<String, MwmTable> imports) {
		tableName = meta.tableName;
		comment = meta.comment;
		entityType = meta.entityType;

		MwmTable t = imports.get(tableName);
		if (t != null) {
			selected = true;
			tableId = t.getTableId();
			comment = t.getLogicalTableName();
			entityType = t.getEntityType();
		}
	}

	public String toString() {
		if (MiscUtils.isEmpty(comment))
			return tableName;
		else
			return String.format("%s (%s)", tableName, comment);
	}
}
