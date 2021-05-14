package jp.co.nci.iwf.jpa.entity.ex;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;


/**
 * The persistent class for the MWM_SCREEN_PARTS_COND database table.
 *
 */
@Entity
public class MwmScreenPartsCondEx extends MwmBaseJpaEntity implements Serializable {

	@Id
	@Column(name="SCREEN_PARTS_COND_ID")
	private long screenPartsCondId;

	@Column(name="PARTS_CONDITION_TYPE")
	private String partsConditionType;

	@Column(name="PARTS_CONDITION_TYPE_NAME")
	private String partsConditionTypeName;

	@Column(name="PARTS_ID")
	private Long partsId;

	@Column(name="SCREEN_ID")
	private Long screenId;

	@Column(name="CALLBACK_FUNCTION")
	private String callbackFunction;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmScreenPartsCondEx() {
	}

	public long getScreenPartsCondId() {
		return this.screenPartsCondId;
	}

	public void setScreenPartsCondId(long screenPartsCondId) {
		this.screenPartsCondId = screenPartsCondId;
	}

	public String getPartsConditionType() {
		return this.partsConditionType;
	}

	public String getPartsConditionTypeName() {
		return this.partsConditionTypeName;
	}

	public void setPartsConditionTypeName(String partsConditionTypeName) {
		this.partsConditionTypeName = partsConditionTypeName;
	}

	public void setPartsConditionType(String partsCondtionType) {
		this.partsConditionType = partsCondtionType;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

	public Long getScreenId() {
		return this.screenId;
	}

	public void setScreenId(Long screenId) {
		this.screenId = screenId;
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