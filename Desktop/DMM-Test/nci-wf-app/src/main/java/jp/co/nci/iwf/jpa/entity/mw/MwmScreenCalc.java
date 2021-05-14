package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_SCREEN_CALC database table.
 *
 */
@Entity
@Table(name="MWM_SCREEN_CALC", uniqueConstraints=@UniqueConstraint(columnNames={"SCREEN_ID", "PARTS_ID", "IDENTIFY_KEY"}))
@NamedQuery(name="MwmScreenCalc.findAll", query="SELECT m FROM MwmScreenCalc m")
public class MwmScreenCalc extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_CALC_ID")
	private long screenCalcId;
	
	@Column(name="PARTS_CALC_NAME")
	private String partsCalcName;

	@Column(name="PARTS_ID")
	private Long partsId;

	@Column(name="SCREEN_ID")
	private Long screenId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="DEFAULT_FLAG")
	private String defaultFlag;

	@Column(name="IDENTIFY_KEY")
	private String identifyKey;
	
	@Column(name="CALLBACK_FUNCTION")
	private String callbackFunction;

	public MwmScreenCalc() {
	}

	public long getScreenCalcId() {
		return this.screenCalcId;
	}

	public void setScreenCalcId(long screenCalcId) {
		this.screenCalcId = screenCalcId;
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

	public Long getScreenId() {
		return this.screenId;
	}

	public void setScreenId(Long screenId) {
		this.screenId = screenId;
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