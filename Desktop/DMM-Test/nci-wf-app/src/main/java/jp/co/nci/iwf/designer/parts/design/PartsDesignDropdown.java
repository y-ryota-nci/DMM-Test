package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.ColumnType;
import jp.co.nci.iwf.designer.DesignerCodeBook.EmptyLineType;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleDropdown;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsDropdown;

/**
 * 【デザイン時】Dropdownパーツ
 */
public class PartsDesignDropdown extends PartsDesignOption implements RoleDropdown {
	/** 読取専用か */
	public boolean readonly;
	/** 空行区分 */
	public int emptyLineType;

	/** Dropdown固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"emptyLineType",
			"readonly",
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		defaultValue = "";
		emptyLineType = EmptyLineType.USE_ALWAYS;
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsDropdown newParts(PartsContainerBase<?> container, Integer rowId, DesignerContext ctx) {
		final PartsDropdown parts = new PartsDropdown();
		setPartsCommonValue(parts, container, rowId, ctx);
		parts.defaultRoleCode = DROPDOWN_CODE;
		parts.clearAndSetDefaultValue(this);
		return parts;
	}

	/**
	 * パーツ固有の拡張情報のフィールド名の定義
	 * @return
	 */
	@Override
	public String[] extFieldNames() {
		return extFieldNames;
	}

	/**
	 * 申請時のパーツデータを格納するためのテーブルカラム定義を新たに生成
	 * @return
	 */
	@Override
	public List<PartsColumn> newColumns() {
		final List<PartsColumn> list = new ArrayList<>();
		{
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_CODE";
			col.columnType = ColumnType.VARCHAR;
			col.columnSize = 30;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.roleCode = DROPDOWN_CODE;

			list.add(col);
		}
		{
			PartsColumn col = new PartsColumn();
			col.partsId = partsId;
			col.columnName = partsCode + "_LABEL";
			col.columnType = ColumnType.VARCHAR;
			col.columnSize = 90;
			col.comments = labelText;
			col.sortOrder = sortOrder;
			col.roleCode = DROPDOWN_LABEL;
			list.add(col);
		}
		return list;
	}

	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {}

	/** パーツ読込後の最終調整処理 */
	public void afterLoad() {}
}
