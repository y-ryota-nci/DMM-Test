package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWM_META_ITEM database table.
 *
 */
@Entity
@Table(name="MWM_META_ITEM")
@NamedQuery(name="MwmMetaItem.findAll", query="SELECT m FROM MwmMetaItem m")
public class MwmMetaItem extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="META_ID")
	private long metaId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="INITIAL_VALUE1")
	private String initialValue1;

	@Column(name="INITIAL_VALUE2")
	private String initialValue2;

	@Column(name="INITIAL_VALUE3")
	private String initialValue3;

	@Column(name="INITIAL_VALUE4")
	private String initialValue4;

	@Column(name="INITIAL_VALUE5")
	private String initialValue5;

	@Column(name="INPUT_TYPE")
	private String inputType;

	@Column(name="MAX_LENGTHS")
	private Integer maxLengths;

	@Column(name="META_CODE")
	private String metaCode;

	@Column(name="META_NAME")
	private String metaName;

	@Column(name="OPTION_ID")
	private Long optionId;

	@Column(name="REQUIRED_FLAG")
	private String requiredFlag;

	public MwmMetaItem() {
	}

	public long getMetaId() {
		return this.metaId;
	}

	public void setMetaId(long metaId) {
		this.metaId = metaId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getInitialValue1() {
		return this.initialValue1;
	}

	public void setInitialValue1(String initialValue1) {
		this.initialValue1 = initialValue1;
	}

	public String getInitialValue2() {
		return this.initialValue2;
	}

	public void setInitialValue2(String initialValue2) {
		this.initialValue2 = initialValue2;
	}

	public String getInitialValue3() {
		return this.initialValue3;
	}

	public void setInitialValue3(String initialValue3) {
		this.initialValue3 = initialValue3;
	}

	public String getInitialValue4() {
		return this.initialValue4;
	}

	public void setInitialValue4(String initialValue4) {
		this.initialValue4 = initialValue4;
	}

	public String getInitialValue5() {
		return this.initialValue5;
	}

	public void setInitialValue5(String initialValue5) {
		this.initialValue5 = initialValue5;
	}

	public String getInputType() {
		return this.inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public Integer getMaxLengths() {
		return this.maxLengths;
	}

	public void setMaxLengths(Integer maxLengths) {
		this.maxLengths = maxLengths;
	}

	public String getMetaCode() {
		return this.metaCode;
	}

	public void setMetaCode(String metaCode) {
		this.metaCode = metaCode;
	}

	public String getMetaName() {
		return this.metaName;
	}

	public void setMetaName(String metaName) {
		this.metaName = metaName;
	}

	public Long getOptionId() {
		return this.optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public String getRequiredFlag() {
		return this.requiredFlag;
	}

	public void setRequiredFlag(String requiredFlag) {
		this.requiredFlag = requiredFlag;
	}

}