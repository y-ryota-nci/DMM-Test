package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * The persistent class for the MWM_DOC_FOLDER database table.
 *
 */
@Entity
public class MwmMetaItemEx extends BaseJpaEntity {

	@Id
	@Column(name="META_ID")
	private long metaId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DELETE_FLAG")
	private String deleteFlag;

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

	@Column(name="REQUIRED_FLAG_NAME")
	private String requiredFlagName;

	@Version
	@Column(name="version")
	private Long version;

	@Column(name="INPUT_TYPE_NAME")
	private String inputTypeName;

	@Column(name="OPTION_NAME")
	private String optionName;


	public long getMetaId() {
		return metaId;
	}

	public void setMetaId(long metaId) {
		this.metaId = metaId;
	}

	public String getCorporationCode() {
		return corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getInitialValue1() {
		return initialValue1;
	}

	public void setInitialValue1(String initialValue1) {
		this.initialValue1 = initialValue1;
	}

	public String getInitialValue2() {
		return initialValue2;
	}

	public void setInitialValue2(String initialValue2) {
		this.initialValue2 = initialValue2;
	}

	public String getInitialValue3() {
		return initialValue3;
	}

	public void setInitialValue3(String initialValue3) {
		this.initialValue3 = initialValue3;
	}

	public String getInitialValue4() {
		return initialValue4;
	}

	public void setInitialValue4(String initialValue4) {
		this.initialValue4 = initialValue4;
	}

	public String getInitialValue5() {
		return initialValue5;
	}

	public void setInitialValue5(String initialValue5) {
		this.initialValue5 = initialValue5;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public Integer getMaxLengths() {
		return maxLengths;
	}

	public void setMaxLengths(Integer maxLengths) {
		this.maxLengths = maxLengths;
	}

	public String getMetaCode() {
		return metaCode;
	}

	public void setMetaCode(String metaCode) {
		this.metaCode = metaCode;
	}

	public String getMetaName() {
		return metaName;
	}

	public void setMetaName(String metaName) {
		this.metaName = metaName;
	}

	public Long getOptionId() {
		return optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public String getRequiredFlag() {
		return requiredFlag;
	}

	public void setRequiredFlag(String requiredFlag) {
		this.requiredFlag = requiredFlag;
	}

	public String getRequiredFlagName() {
		return requiredFlagName;
	}

	public void setRequiredFlagName(String requiredFlagName) {
		this.requiredFlagName = requiredFlagName;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getInputTypeName() {
		return inputTypeName;
	}

	public void setInputTypeName(String inputTypeName) {
		this.inputTypeName = inputTypeName;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

}
