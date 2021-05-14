package jp.co.nci.iwf.designer.parts.design;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.designer.DesignerCodeBook.ColumnType;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleCheckbox;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.runtime.PartsCheckbox;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 【デザイン時】Checkboxパーツ
 */
public class PartsDesignCheckbox extends PartsDesign implements RoleCheckbox {

	/** チェックありの時の値 */
	public String checkedValue;
	/** チェックなしの時の値 */
	public String uncheckedValue;
	/** HIDDENとしてレンダリングするか */
	public boolean renderAsHidden;
	/** 読取専用か */
	public boolean readonly;
	/** チェックボックス用の表示ラベル */
	public String checkboxLabel;


	/** Checkbox固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"checkedValue",
			"uncheckedValue",
			"renderAsHidden",
			"readonly",
			"checkboxLabel"
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		defaultValue = CommonFlag.OFF;
		checkedValue = CommonFlag.ON;
		uncheckedValue = CommonFlag.OFF;
		checkboxLabel = partsCode;
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsCheckbox newParts(PartsContainerBase<?> container, Integer rowId, DesignerContext ctx) {
		final PartsCheckbox parts = new PartsCheckbox();
		setPartsCommonValue(parts, container, rowId, ctx);
		parts.defaultRoleCode = CHECK;
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
		PartsColumn col = new PartsColumn();
		col.partsId = partsId;
		col.columnName = partsCode;
		col.columnType = ColumnType.VARCHAR;
		final int checkedBytes = MiscUtils.isEmpty(checkedValue) ? 1 : checkedValue.getBytes(StandardCharsets.UTF_8).length;
		final int uncheckedBytes = MiscUtils.isEmpty(uncheckedValue) ? 1 : uncheckedValue.getBytes(StandardCharsets.UTF_8).length;
		col.columnSize = Math.max(checkedBytes, uncheckedBytes);
		col.comments = labelText;
		col.sortOrder = sortOrder;
		col.roleCode = CHECK;
		list.add(col);
		return list;
	}

	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {}

	/** パーツ読込後の最終調整処理 */
	public void afterLoad() {}
}
