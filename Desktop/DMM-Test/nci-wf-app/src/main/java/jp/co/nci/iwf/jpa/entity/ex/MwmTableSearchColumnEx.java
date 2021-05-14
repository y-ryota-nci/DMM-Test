package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 汎用テーブル検索条件カラムエンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class MwmTableSearchColumnEx extends BaseJpaEntity {
	@Id
	@Column(name="TABLE_SEARCH_COLUMN_ID")
	public long tableSearchColumnId;

	@Column(name="COLUMN_NAME")
	public String columnName;

	@Column(name="CONDITION_BLANK_TYPE")
	public String conditionBlankType;

	@Column(name="CONDITION_BLANK_TYPE_NAME")
	public String conditionBlankTypeName;

	@Column(name="CONDITION_DISPLAY_POSITION")
	public Integer conditionDisplayPosition;

	@Column(name="CONDITION_DISPLAY_TYPE")
	public String conditionDisplayType;

	@Column(name="CONDITION_DISPLAY_TYPE_NAME")
	public String conditionDisplayTypeName;

	@Column(name="CONDITION_INIT_TYPE_1")
	public String conditionInitType1;

	@Column(name="CONDITION_INIT_TYPE_NAME_1")
	public String conditionInitTypeName1;

	@Column(name="CONDITION_INIT_TYPE_2")
	public String conditionInitType2;

	@Column(name="CONDITION_INIT_TYPE_NAME_2")
	public String conditionInitTypeName2;

	@Column(name="CONDITION_INIT_VALUE_1")
	public String conditionInitValue1;

	@Column(name="CONDITION_INIT_VALUE_2")
	public String conditionInitValue2;

	@Column(name="CONDITION_MATCH_TYPE")
	public String conditionMatchType;

	@Column(name="CONDITION_MATCH_TYPE_NAME")
	public String conditionMatchTypeName;

	@Column(name="CONDITION_OPTION_ID")
	public Long conditionOptionId;

	@Column(name="CONDITION_TRIM_FLAG")
	public String conditionTrimFlag;

	@Column(name="CONDITION_TRIM_FLAG_NAME")
	public String conditionTrimFlagName;

	@Column(name="CONDITION_KANA_CONVERT_TYPE")
	public String conditionKanaConvertType;

	@Column(name="CONDITION_KANA_CONVERT_TYPE_NM")
	public String conditionKanaConvertTypeName;

	@Column(name="LOGICA_COLUMN_NAME")
	public String logicaColumnName;

	@Column(name="RESULT_ALIGN_TYPE")
	public String resultAlignType;

	@Column(name="RESULT_ALIGN_TYPE_NAME")
	public String resultAlignTypeName;

	@Column(name="RESULT_ORDER_BY_DIRECTION")
	public String resultOrderByDirection;

	@Column(name="RESULT_ORDER_BY_DIRECTION_NAME")
	public String resultOrderByDirectionName;

	@Column(name="RESULT_ORDER_BY_POSITION")
	public Integer resultOrderByPosition;

	@Column(name="RESULT_DISPLAY_POSITION")
	public Integer resultDisplayPosition;

	@Column(name="RESULT_DISPLAY_TYPE")
	public String resultDisplayType;

	@Column(name="RESULT_DISPLAY_TYPE_NAME")
	public String resultDisplayTypeName;

	@Column(name="RESULT_DISPLAY_WIDTH")
	public Integer resultDisplayWidth;

	@Column(name="SEARCH_COLUMN_TYPE")
	public String searchColumnType;

	@Column(name="SEARCH_COLUMN_TYPE_NAME")
	public String searchColumnTypeName;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Column(name="TABLE_SEARCH_ID")
	public Long tableSearchId;

	@Column(name="VERSION")
	public Long version;

	@Column(name="DELETE_FLAG")
	public String deleteFlag;
}
