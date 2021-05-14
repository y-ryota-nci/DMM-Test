package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_CALC_EC database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_CALC_EC", uniqueConstraints=@UniqueConstraint(columnNames={"PARTS_CALC_ID", "IDENTIFY_KEY"}))
@NamedQuery(name="MwmPartsCalcEc.findAll", query="SELECT m FROM MwmPartsCalcEc m")
public class MwmPartsCalcEc extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_CALC_EC_ID")
	private long partsCalcEcId;

	@Column(name="EC_CLASS")
	private String ecClass;

	@Column(name="EC_OPERATOR")
	private String ecOperator;

	@Column(name="EC_TYPE")
	private String ecType;

	@Column(name="IDENTIFY_KEY")
	private String identifyKey;

	@Column(name="NUMERIC_FLAG")
	private String numericFlag;

	@Column(name="PARTS_CALC_ID")
	private Long partsCalcId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="TARGET_LITERAL_VAL")
	private String targetLiteralVal;

	@Column(name="TARGET_PARTS_ID")
	private Long targetPartsId;

	public MwmPartsCalcEc() {
	}

	public long getPartsCalcEcId() {
		return this.partsCalcEcId;
	}

	public void setPartsCalcEcId(long partsCalcEcId) {
		this.partsCalcEcId = partsCalcEcId;
	}

	public String getEcClass() {
		return this.ecClass;
	}

	public void setEcClass(String ecClass) {
		this.ecClass = ecClass;
	}

	public String getEcOperator() {
		return this.ecOperator;
	}

	public void setEcOperator(String ecOperator) {
		this.ecOperator = ecOperator;
	}

	public String getEcType() {
		return this.ecType;
	}

	public void setEcType(String ecType) {
		this.ecType = ecType;
	}

	public String getIdentifyKey() {
		return identifyKey;
	}

	public void setIdentifyKey(String identifyKey) {
		this.identifyKey = identifyKey;
	}

	public String getNumericFlag() {
		return this.numericFlag;
	}

	public void setNumericFlag(String numericFlag) {
		this.numericFlag = numericFlag;
	}

	public Long getPartsCalcId() {
		return this.partsCalcId;
	}

	public void setPartsCalcId(Long partsCalcId) {
		this.partsCalcId = partsCalcId;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getTargetLiteralVal() {
		return this.targetLiteralVal;
	}

	public void setTargetLiteralVal(String targetLiteralVal) {
		this.targetLiteralVal = targetLiteralVal;
	}

	public Long getTargetPartsId() {
		return this.targetPartsId;
	}

	public void setTargetPartsId(Long targetPartsId) {
		this.targetPartsId = targetPartsId;
	}

}