package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

/**
 * The persistent class for the MWT_DOC_META_INFO database table.
 *
 */
@Entity
public class MwtDocMetaInfoEx extends MwmBaseJpaEntity {

	@Id
	@Column(name="DOC_META_ID")
	private long docMetaId;

	@Column(name="DOC_ID")
	private Long docId;

	@Column(name="META_TEMPLATE_DETAIL_ID")
	private Long metaTemplateDetailId;

	@Column(name="META_TEMPLATE_ID")
	private Long metaTemplateId;

	@Column(name="META_VALUE1")
	private String metaValue1;

	@Column(name="META_VALUE2")
	private String metaValue2;

	@Column(name="META_VALUE3")
	private String metaValue3;

	@Column(name="META_VALUE4")
	private String metaValue4;

	@Column(name="META_VALUE5")
	private String metaValue5;

	@Column(name="META_Code")
	private String metaCode;

	@Column(name="META_NAME")
	private String metaName;

	@Column(name="INPUT_TYPE")
	private String inputType;

	@Column(name="REQUIRED_FLAG")
	private String requiredFlag;

	@Column(name="MAX_LENGTHS")
	private Integer maxLengths;

	@Column(name="OPTION_ID")
	private Long optionId;

	public long getDocMetaId() {
		return docMetaId;
	}

	public void setDocMetaId(long docMetaId) {
		this.docMetaId = docMetaId;
	}

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public Long getMetaTemplateDetailId() {
		return metaTemplateDetailId;
	}

	public void setMetaTemplateDetailId(Long metaTemplateDetailId) {
		this.metaTemplateDetailId = metaTemplateDetailId;
	}

	public Long getMetaTemplateId() {
		return metaTemplateId;
	}

	public void setMetaTemplateId(Long metaTemplateId) {
		this.metaTemplateId = metaTemplateId;
	}

	public String getMetaValue1() {
		return metaValue1;
	}

	public void setMetaValue1(String metaValue1) {
		this.metaValue1 = metaValue1;
	}

	public String getMetaValue2() {
		return metaValue2;
	}

	public void setMetaValue2(String metaValue2) {
		this.metaValue2 = metaValue2;
	}

	public String getMetaValue3() {
		return metaValue3;
	}

	public void setMetaValue3(String metaValue3) {
		this.metaValue3 = metaValue3;
	}

	public String getMetaValue4() {
		return metaValue4;
	}

	public void setMetaValue4(String metaValue4) {
		this.metaValue4 = metaValue4;
	}

	public String getMetaValue5() {
		return metaValue5;
	}

	public void setMetaValue5(String metaValue5) {
		this.metaValue5 = metaValue5;
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

	public String getRequiredFlag() {
		return requiredFlag;
	}

	public void setRequiredFlag(String requiredFlag) {
		this.requiredFlag = requiredFlag;
	}

	public Integer getMaxLengths() {
		return maxLengths;
	}

	public void setMaxLengths(Integer maxLengths) {
		this.maxLengths = maxLengths;
	}

	public Long getOptionId() {
		return optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

}
