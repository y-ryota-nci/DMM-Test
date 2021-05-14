package jp.co.nci.iwf.jpa.entity.ex;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

/**
 * The persistent class for the MWM_SEQUENCE database table.
 *
 */
@Entity
public class MwmPartsSequenceSpecEx extends MwmBaseJpaEntity implements Serializable {

	@Id
	@Column(name="PARTS_SEQUENCE_SPEC_ID")
	private long partsSequenceSpecId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="PARTS_SEQUENCE_SPEC_CODE")
	private String partsSequenceSpecCode;

	@Column(name="PARTS_SEQUENCE_SPEC_NAME")
	private String partsSequenceSpecName;

	@Column(name="RESET_TYPE")
	private String resetType;

	@Column(name="SEQUENCE_LENGTH")
	private Integer sequenceLength;

	@Column(name="DELETE_FLAG_NAME")
	private String deleteFlagName;

	@Column(name="RESET_TYPE_NAME")
	private String resetTypeName;

	@Column(name="START_VALUE")
	private Long startValue;

	public long getPartsSequenceSpecId() {
		return this.partsSequenceSpecId;
	}

	public void setPartsSequenceSpecId(long partsSequenceSpecId) {
		this.partsSequenceSpecId = partsSequenceSpecId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getPartsSequenceSpecCode() {
		return this.partsSequenceSpecCode;
	}

	public void setPartsSequenceSpecCode(String partsSequenceSpecCode) {
		this.partsSequenceSpecCode = partsSequenceSpecCode;
	}

	public String getPartsSequenceSpecName() {
		return this.partsSequenceSpecName;
	}

	public void setPartsSequenceSpecName(String partsSequenceSpecName) {
		this.partsSequenceSpecName = partsSequenceSpecName;
	}

	public String getResetType() {
		return this.resetType;
	}

	public void setResetType(String resetType) {
		this.resetType = resetType;
	}

	public Integer getSequenceLength() {
		return this.sequenceLength;
	}

	public void setSequenceLength(Integer sequenceLength) {
		this.sequenceLength = sequenceLength;
	}

	/**
	 * @return deleteFlagName
	 */
	public String getDeleteFlagName() {
		return deleteFlagName;
	}

	/**
	 * @param deleteFlagName セットする deleteFlagName
	 */
	public void setDeleteFlagName(String deleteFlagName) {
		this.deleteFlagName = deleteFlagName;
	}

	/**
	 * @return resetTypeName
	 */
	public String getResetTypeName() {
		return resetTypeName;
	}

	/**
	 * @param resetTypeName セットする resetTypeName
	 */
	public void setResetTypeName(String resetTypeName) {
		this.resetTypeName = resetTypeName;
	}

	public Long getStartValue() {
		return this.startValue;
	}

	public void setStartValue(Long startValue) {
		this.startValue = startValue;
	}
}
