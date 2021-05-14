package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.ColumnType;
import jp.co.nci.iwf.designer.DesignerCodeBook.DisplayMethod;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleRadio;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsRadio;

/**
 * 【デザイン時】Radioパーツ
 */
public class PartsDesignRadio extends PartsDesignOption implements RoleRadio {

	/** 表示方法 */
	public int displayMethod;
	/** 読取専用か */
	public boolean readonly;

	/** Radio固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"displayMethod",
			"readonly",
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		defaultValue = "";
		displayMethod = DisplayMethod.HORIZONTAL;
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsRadio newParts(PartsContainerBase<?> container, Integer rowId, DesignerContext ctx) {
		final PartsRadio parts = new PartsRadio();
		setPartsCommonValue(parts, container, rowId, ctx);
		parts.defaultRoleCode = RADIO_CODE;
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
			col.roleCode = RADIO_CODE;

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
			col.roleCode = RADIO_LABEL;
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
