package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

@Entity
public class MwmUserDisplayConditionEx extends MwmBaseJpaEntity {

	@Id
	@Column(name="USER_DISPLAY_CONDITION_ID")
	private Long userDisplayConditionId;
	@Column(name="USER_DISPLAY_ID")
	private Long userDisplayId;
	@Column(name="COLUMN_NAME")
	private String columnName;
	@Column(name="CONDITION_TYPE")
	private String conditionType;
	@Column(name="CONDITION_INDEX")
	private Long conditionIndex;
	@Column(name="DISPLAY_NAME")
	private String displayName;
	private String attrName;

	public Long getUserDisplayConditionId(){
		return this.userDisplayConditionId;
	}

	public void setUserDisplayConditionId(final Long val){
		this.userDisplayConditionId = val;
	}

	public Long getUserDisplayId(){
		return this.userDisplayId;
	}

	public void setUserDisplayId(final Long val){
		this.userDisplayId = val;
	}

	public String getColumnName(){
		return this.columnName;
	}

	public void setColumnName(final String val){
		this.columnName = val;
	}

	public String getConditionType(){
		return this.conditionType;
	}

	public void setConditionType(final String val){
		this.conditionType = val;
	}

	public Long getConditionIndex(){
		return this.conditionIndex;
	}

	public void setConditionIndex(final Long val){
		this.conditionIndex = val;
	}

	/**
	 * @return displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName セットする displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return attrName
	 */
	public String getAttrName() {
		return attrName;
	}

	/**
	 * @param attrName セットする attrName
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
}
