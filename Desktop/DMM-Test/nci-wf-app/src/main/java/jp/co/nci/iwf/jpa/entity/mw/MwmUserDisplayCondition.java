package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the MWM_USER_DISPLAY_CONDITION database table.
 *
 */
@Entity
@Table(name="MWM_USER_DISPLAY_CONDITION")
@NamedQuery(name="MwmUserDisplayCondition.findAll", query="SELECT m FROM MwmUserDisplayCondition m")
public class MwmUserDisplayCondition extends MwmBaseJpaEntity implements Serializable {

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

}
