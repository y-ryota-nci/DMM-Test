package jp.co.nci.iwf.jpa.entity.ex;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;


/**
 * The persistent class for the MWM_META_TEMPLATE_DETAIL database table.
 *
 */
@Entity
public class MwmMetaTemplateDetailEx extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="META_TEMPLATE_DETAIL_ID")
	private long metaTemplateDetailId;

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

	@Column(name="MAX_LENGTHS")
	private Integer maxLengths;

	@Column(name="META_ID")
	private Long metaId;

	@Column(name="META_TEMPLATE_ID")
	private Long metaTemplateId;

	@Column(name="REQUIRED_FLAG")
	private String requiredFlag;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="REQUIRED_FLAG_NAME")
	private String requiredFlagName;

	@Column(name="DELETE_FLAG_NAME")
	private String deleteFlagName;

	@Column(name="META_CODE")
	private String metaCode;

	@Column(name="META_NAME")
	private String metaName;

	@Column(name="INPUT_TYPE")
	private String inputType;

	@Column(name="INPUT_TYPE_NAME")
	private String inputTypeName;

	@Column(name="OPTION_ID")
	private Long optionId;

	@Column(name="OPTION_Name")
	private String optionName;

	public MwmMetaTemplateDetailEx() {
	}

	public long getMetaTemplateDetailId() {
		return this.metaTemplateDetailId;
	}

	public void setMetaTemplateDetailId(long metaTemplateDetailId) {
		this.metaTemplateDetailId = metaTemplateDetailId;
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

	public Integer getMaxLengths() {
		return this.maxLengths;
	}

	public void setMaxLengths(Integer maxLengths) {
		this.maxLengths = maxLengths;
	}

	public Long getMetaId() {
		return this.metaId;
	}

	public void setMetaId(Long metaId) {
		this.metaId = metaId;
	}

	public Long getMetaTemplateId() {
		return this.metaTemplateId;
	}

	public void setMetaTemplateId(Long metaTemplateId) {
		this.metaTemplateId = metaTemplateId;
	}

	public String getRequiredFlag() {
		return this.requiredFlag;
	}

	public void setRequiredFlag(String requiredFlag) {
		this.requiredFlag = requiredFlag;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getRequiredFlagName() {
		return requiredFlagName;
	}

	public void setRequiredFlagName(String requiredFlagName) {
		this.requiredFlagName = requiredFlagName;
	}

	public String getDeleteFlagName() {
		return deleteFlagName;
	}

	public void setDeleteFlagName(String deleteFlagName) {
		this.deleteFlagName = deleteFlagName;
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

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getInputTypeName() {
		return inputTypeName;
	}

	public void setInputTypeName(String inputTypeName) {
		this.inputTypeName = inputTypeName;
	}

	public Long getOptionId() {
		return this.optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

}