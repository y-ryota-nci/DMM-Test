package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_SEQUENCE_SPEC database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_SEQUENCE_SPEC", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "PARTS_SEQUENCE_SPEC_CODE"}))
@NamedQuery(name="MwmPartsSequenceSpec.findAll", query="SELECT m FROM MwmPartsSequenceSpec m")
public class MwmPartsSequenceSpec extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

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

	@Column(name="START_VALUE")
	private Long startValue;

	public MwmPartsSequenceSpec() {
	}

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

	public Long getStartValue() {
		return this.startValue;
	}

	public void setStartValue(Long startValue) {
		this.startValue = startValue;
	}

}