package jp.co.nci.iwf.jpa.entity.ex;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;


/**
 * The persistent class for the MWM_PARTS_COND database table.
 *
 */
@Entity
public class MwmPartsCondEx extends MwmBaseJpaEntity implements Serializable {

	@Id
	@Column(name="PARTS_COND_ID")
	private long partsCondId;

	@Column(name="PARTS_CONDITION_TYPE")
	private String partsConditionType;

	@Column(name="PARTS_CONDITION_TYPE_NAME")
	private String partsConditionTypeName;

	@Column(name="PARTS_ID")
	private Long partsId;

	@Column(name="CALLBACK_FUNCTION")
	private String callbackFunction;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmPartsCondEx() {
	}

	public long getPartsCondId() {
		return this.partsCondId;
	}

	public void setPartsCondId(long partsCondId) {
		this.partsCondId = partsCondId;
	}

	public String getPartsConditionType() {
		return this.partsConditionType;
	}

	public void setPartsConditionType(String partsConditionType) {
		this.partsConditionType = partsConditionType;
	}

	public String getPartsConditionTypeName() {
		return this.partsConditionTypeName;
	}

	public void setPartsConditionTypeName(String partsConditionTypeName) {
		this.partsConditionTypeName = partsConditionTypeName;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

	public String getCallbackFunction() {
		return callbackFunction;
	}

	public void setCallbackFunction(String callbackFunction) {
		this.callbackFunction = callbackFunction;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}