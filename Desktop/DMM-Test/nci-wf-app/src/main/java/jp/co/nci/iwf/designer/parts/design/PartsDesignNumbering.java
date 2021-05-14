package jp.co.nci.iwf.designer.parts.design;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.designer.DesignerCodeBook.ColumnType;
import jp.co.nci.iwf.designer.DesignerCodeBook.RoleNumbering;
import jp.co.nci.iwf.designer.parts.DesignerContext;
import jp.co.nci.iwf.designer.parts.PartsColumn;
import jp.co.nci.iwf.designer.parts.runtime.PartsBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsContainerBase;
import jp.co.nci.iwf.designer.parts.runtime.PartsNumbering;

/**
 * 【デザイン時】採番パーツ
 */
public class PartsDesignNumbering extends PartsDesign implements RoleNumbering {
	/** (アクティビティと連動した)採番コード */
	public String numberingCode;
	/** パーツ採番形式ID */
	public Long partsNumberingFormatId;
	/** 申請/承認時のみ発番する */
	public boolean fireIfNormalAction;

	/** 採番パーツ固有のフィールド名の定義 */
	private static final String[] extFieldNames = {
			"numberingCode",
			"partsNumberingFormatId",
			"fireIfNormalAction",
	};

	/**
	 * 【デザイン時】新規パーツ配置用の初期値を付与する
	 */
	@Override
	public void setInitValue() {
		super.setInitValue();
		copyTargetFlag = false;
	}

	/**
	 * 新規パーツ配置用の新しいインスタンスを返す
	 */
	@Override
	public PartsBase<? extends PartsDesign> newParts(PartsContainerBase<?> container, Integer rowId,
			DesignerContext ctx) {
		final PartsNumbering parts = new PartsNumbering();
		parts.defaultRoleCode = NUMBERING;
		setPartsCommonValue(parts, container, rowId, ctx);
		return parts;
	}

	/**
	 * パーツ固有の拡張情報のフィールド名の定義
	 */
	@Override
	public String[] extFieldNames() {
		return extFieldNames;
	}

	/**
	 * 申請時のパーツデータを格納するためのテーブルカラム定義を新たに生成
	 */
	@Override
	public List<PartsColumn> newColumns() {
		final List<PartsColumn> list = new ArrayList<>();
		PartsColumn col = new PartsColumn();
		col.partsId = partsId;
		col.columnName = partsCode;
		col.columnType = ColumnType.VARCHAR;
		col.columnSize = 60;
		col.comments = labelText;
		col.sortOrder = sortOrder;
		col.roleCode = NUMBERING;
		list.add(col);
		return list;
	}

	/** パーツ更新の前処理 */
	@Override
	public void beforeSave() {}

	/** パーツ読込後の最終調整処理 */
	public void afterLoad() {}
}
