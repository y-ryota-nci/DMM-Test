package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_TABLE_SEARCH_COLUMN database table.
 *
 */
@Entity
@Table(name="MWM_TABLE_SEARCH_COLUMN", uniqueConstraints=@UniqueConstraint(columnNames={"TABLE_SEARCH_ID", "COLUMN_NAME"}))
@NamedQuery(name="MwmTableSearchColumn.findAll", query="SELECT m FROM MwmTableSearchColumn m")
public class MwmTableSearchColumn extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TABLE_SEARCH_COLUMN_ID")
	private long tableSearchColumnId;

	@Column(name="COLUMN_NAME")
	private String columnName;

	@Column(name="CONDITION_BLANK_TYPE")
	private String conditionBlankType;

	@Column(name="CONDITION_DISPLAY_POSITION")
	private Integer conditionDisplayPosition;

	@Column(name="CONDITION_DISPLAY_TYPE")
	private String conditionDisplayType;

	@Column(name="CONDITION_INIT_TYPE_1")
	private String conditionInitType1;

	@Column(name="CONDITION_INIT_TYPE_2")
	private String conditionInitType2;

	@Column(name="CONDITION_INIT_VALUE_1")
	private String conditionInitValue1;

	@Column(name="CONDITION_INIT_VALUE_2")
	private String conditionInitValue2;

	@Column(name="CONDITION_MATCH_TYPE")
	private String conditionMatchType;

	@Column(name="CONDITION_OPTION_ID")
	private Long conditionOptionId;

	@Column(name="CONDITION_TRIM_FLAG")
	private String conditionTrimFlag;

	@Column(name="CONDITION_KANA_CONVERT_TYPE")
	private String conditionKanaConvertType;

	@Column(name="LOGICA_COLUMN_NAME")
	private String logicaColumnName;

	@Column(name="RESULT_ALIGN_TYPE")
	private String resultAlignType;

	@Column(name="RESULT_ORDER_BY_DIRECTION")
	private String resultOrderByDirection;

	@Column(name="RESULT_ORDER_BY_POSITION")
	private Integer resultOrderByPosition;

	@Column(name="RESULT_DISPLAY_POSITION")
	private Integer resultDisplayPosition;

	@Column(name="RESULT_DISPLAY_TYPE")
	private String resultDisplayType;

	@Column(name="RESULT_DISPLAY_WIDTH")
	private Integer resultDisplayWidth;

	@Column(name="SEARCH_COLUMN_TYPE")
	private String searchColumnType;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="TABLE_SEARCH_ID")
	private Long tableSearchId;

	public MwmTableSearchColumn() {
	}

	public long getTableSearchColumnId() {
		return this.tableSearchColumnId;
	}

	public void setTableSearchColumnId(long tableSearchColumnId) {
		this.tableSearchColumnId = tableSearchColumnId;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getConditionBlankType() {
		return conditionBlankType;
	}

	public void setConditionBlankType(String conditionBlankType) {
		this.conditionBlankType = conditionBlankType;
	}

	public Integer getConditionDisplayPosition() {
		return this.conditionDisplayPosition;
	}

	public void setConditionDisplayPosition(Integer conditionDisplayPosition) {
		this.conditionDisplayPosition = conditionDisplayPosition;
	}

	public String getConditionDisplayType() {
		return this.conditionDisplayType;
	}

	public void setConditionDisplayType(String conditionDisplayType) {
		this.conditionDisplayType = conditionDisplayType;
	}

	public String getConditionInitType1() {
		return this.conditionInitType1;
	}

	public void setConditionInitType1(String conditionInitType1) {
		this.conditionInitType1 = conditionInitType1;
	}

	public String getConditionInitType2() {
		return this.conditionInitType2;
	}

	public void setConditionInitType2(String conditionInitType2) {
		this.conditionInitType2 = conditionInitType2;
	}

	public String getConditionInitValue1() {
		return this.conditionInitValue1;
	}

	public void setConditionInitValue1(String conditionInitValue1) {
		this.conditionInitValue1 = conditionInitValue1;
	}

	public String getConditionInitValue2() {
		return this.conditionInitValue2;
	}

	public void setConditionInitValue2(String conditionInitValue2) {
		this.conditionInitValue2 = conditionInitValue2;
	}

	public String getConditionMatchType() {
		return this.conditionMatchType;
	}

	public void setConditionMatchType(String conditionMatchType) {
		this.conditionMatchType = conditionMatchType;
	}

	public Long getConditionOptionId() {
		return this.conditionOptionId;
	}

	public String getConditionTrimFlag() {
		return conditionTrimFlag;
	}

	public void setConditionTrimFlag(String conditionTrimFlag) {
		this.conditionTrimFlag = conditionTrimFlag;
	}

	public String getConditionKanaConvertType() {
		return conditionKanaConvertType;
	}

	public void setConditionKanaConvertType(String conditionKanaConvertType) {
		this.conditionKanaConvertType = conditionKanaConvertType;
	}

	public void setConditionOptionId(Long conditionOptionId) {
		this.conditionOptionId = conditionOptionId;
	}

	public String getLogicaColumnName() {
		return this.logicaColumnName;
	}

	public void setLogicaColumnName(String logicaColumnName) {
		this.logicaColumnName = logicaColumnName;
	}

	public String getResultAlignType() {
		return this.resultAlignType;
	}

	public void setResultAlignType(String resultAlignType) {
		this.resultAlignType = resultAlignType;
	}

	public String getResultOrderByDirection() {
		return this.resultOrderByDirection;
	}

	public void setResultOrderByDirection(String resultOrderByDirection) {
		this.resultOrderByDirection = resultOrderByDirection;
	}

	public Integer getResultOrderByPosition() {
		return this.resultOrderByPosition;
	}

	public void setResultOrderByPosition(Integer resultOrderByPosition) {
		this.resultOrderByPosition = resultOrderByPosition;
	}

	public Integer getResultDisplayPosition() {
		return this.resultDisplayPosition;
	}

	public void setResultDisplayPosition(Integer resultDisplayPosition) {
		this.resultDisplayPosition = resultDisplayPosition;
	}

	public String getResultDisplayType() {
		return this.resultDisplayType;
	}

	public void setResultDisplayType(String resultDisplayType) {
		this.resultDisplayType = resultDisplayType;
	}

	public Integer getResultDisplayWidth() {
		return this.resultDisplayWidth;
	}

	public void setResultDisplayWidth(Integer resultDisplayWidth) {
		this.resultDisplayWidth = resultDisplayWidth;
	}

	public String getSearchColumnType() {
		return searchColumnType;
	}

	public void setSearchColumnType(String searchColumnType) {
		this.searchColumnType = searchColumnType;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Long getTableSearchId() {
		return this.tableSearchId;
	}

	public void setTableSearchId(Long tableSearchId) {
		this.tableSearchId = tableSearchId;
	}

}