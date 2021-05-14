package jp.co.nci.iwf.component.tableSearch;

import java.util.ArrayList;
import java.util.List;

import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchColumnEx;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 汎用テーブル：検索結果定義
 */
public class TableSearchResultDef {
	/** 汎用テーブル検索条件カラムID */
	public long tableSearchColumnId;
	/** カラム名 */
	public String columnName;
	/** ソートカラム名 */
	public String sortColumnName;
	/** カラム型 */
	public String columnType;
	/** 検索結果：表示位置揃え区分 */
	public String alignType;
	/** 検索結果：デフォルトソート方向 */
	public String orderByDirection;
	/** 検索結果：デフォルトソート順 */
	public Integer orderByPosition;
	/** 検索結果：表示順 */
	public Integer displayPosition;
	/** 検索結果：表示区分 */
	public String displayType;
	/** 検索結果：列幅 */
	public Integer displayWidth;
	/** 検索結果：列表示名 */
	public String displayName;

	/** コンストラクタ */
	public TableSearchResultDef() {
	}

	/** コンストラクタ */
	public TableSearchResultDef(List<String> primaryKeys, MwmTableSearchColumnEx src) {
		tableSearchColumnId = src.tableSearchColumnId;
		columnName = src.columnName;
		columnType = src.searchColumnType;
		alignType = src.resultAlignType;
		orderByDirection = src.resultOrderByDirection;
		orderByPosition = src.resultOrderByPosition;
		displayPosition = src.resultDisplayPosition;
		displayType = src.resultDisplayType;
		displayWidth = src.resultDisplayWidth;
		displayName = MiscUtils.defaults(src.logicaColumnName, src.columnName);

		List<String> sorts = new ArrayList<>();
		if (MiscUtils.isNotEmpty(this.columnName))
			sorts.add(this.columnName);
		for (String pk : primaryKeys) {
			if (!sorts.contains(pk))
				sorts.add(pk);
		}
		sortColumnName = String.join(", ", sorts);
	}
}
