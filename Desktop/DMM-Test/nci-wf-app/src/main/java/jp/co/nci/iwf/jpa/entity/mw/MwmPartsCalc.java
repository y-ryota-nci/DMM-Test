package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The persistent class for the MWM_PARTS_CALC database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_CALC", uniqueConstraints=@UniqueConstraint(columnNames={"PARTS_ID", "IDENTIFY_KEY"}))
@NamedQuery(name="MwmPartsCalc.findAll", query="SELECT m FROM MwmPartsCalc m")
public class MwmPartsCalc extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_CALC_ID")
	private long partsCalcId;

	@Column(name="PARTS_CALC_NAME")
	private String partsCalcName;

	@Column(name="PARTS_ID")
	private Long partsId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="DEFAULT_FLAG")
	private String defaultFlag;

	@Column(name="IDENTIFY_KEY")
	private String identifyKey;
	
	@Column(name="CALLBACK_FUNCTION")
	private String callbackFunction;

	public MwmPartsCalc() {
	}

	public long getPartsCalcId() {
		return this.partsCalcId;
	}

	public void setPartsCalcId(long partsCalcId) {
		this.partsCalcId = partsCalcId;
	}

	public String getPartsCalcName() {
		return this.partsCalcName;
	}

	public void setPartsCalcName(String partsCalcName) {
		this.partsCalcName = partsCalcName;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getDefaultFlag() {
		return this.defaultFlag;
	}

	public void setDefaultFlag(final String val) {
		this.defaultFlag = val;
	}

	public String getIdentifyKey() {
		return identifyKey;
	}

	public void setIdentifyKey(String identifyKey) {
		this.identifyKey = identifyKey;
	}

	public String getCallbackFunction() {
		return this.callbackFunction;
	}

	public void setCallbackFunction(String callbackFunction) {
		this.callbackFunction = callbackFunction;
	}

}