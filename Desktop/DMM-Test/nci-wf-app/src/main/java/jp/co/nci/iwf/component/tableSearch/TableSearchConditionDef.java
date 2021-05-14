package jp.co.nci.iwf.component.tableSearch;

import java.util.List;

import jp.co.nci.integrated_workflow.common.CodeMaster.CommonFlag;
import jp.co.nci.iwf.component.CodeBook.ConditionBlankType;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableSearchColumnEx;
import jp.co.nci.iwf.util.MiscUtils;

/**
 * 汎用テーブル：検索条件定義
 */
public class TableSearchConditionDef {
	/** 汎用テーブル検索条件カラムID */
	public long tableSearchColumnId;
	/** カラム名 */
	public String columnName;
	/** カラム型 */
	public String columnType;
	/** 検索条件：表示位置 */
	public Integer displayPosition;
	/** 検索条件：表示区分 */
	public String displayType;
	/** 検索条件：初期化区分１ */
	public String initType1;
	/** 検索条件：初期化区分２ */
	public String initType2;
	/** 検索条件：初期化１ */
	public String initValue1;
	/** 検索条件：初期化２ */
	public String initValue2;
	/** 検索条件：一致区分 */
	public String matchType;
	/** 検索条件：表示名 */
	public String displayName;
	/** 検索条件：選択肢 */
	public List<OptionItem> options;
	/** 検索条件：必須入力か */
	public boolean required;
	/** 検索条件：ブランクをブランクとして検索 */
	public boolean searchAsBlank;
	/** ブランク区分 */
	public String blankType;
	/** 検索条件：トリムして検索 */
	public boolean conditionTrimFlag;
	/** カナ変換区分 */
	public String conditionKanaConvertType;


	/** コンストラクタ */
	public TableSearchConditionDef() {
	}

	/** コンストラクタ */
	public TableSearchConditionDef(MwmTableSearchColumnEx src) {
		MiscUtils.copyFields(src, this);
		displayPosition = src.conditionDisplayPosition;
		displayType = src.conditionDisplayType;
		columnType = src.searchColumnType;
		initType1 = src.conditionInitType1;
		initType2 = src.conditionInitType2;
		initValue1 = src.conditionInitValue1;
		initValue2 = src.conditionInitValue2;
		matchType = src.conditionMatchType;
		blankType = src.conditionBlankType;
		required = ConditionBlankType.REQUIRED.equals(blankType);
		searchAsBlank = ConditionBlankType.SEARCH.equals(blankType);
		displayName = MiscUtils.defaults(src.logicaColumnName, src.columnName);
		conditionTrimFlag = MiscUtils.eq(CommonFlag.ON, src.conditionTrimFlag);
		conditionKanaConvertType = src.conditionKanaConvertType;
	}
}
