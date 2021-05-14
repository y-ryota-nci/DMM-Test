package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_DOC_BUSINESS_INFO_NAME database table.
 *
 */
@Entity
@Table(name="MWM_DOC_BUSINESS_INFO_NAME", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "DOC_BUSINESS_INFO_CODE"}))
@NamedQuery(name="MwmDocBusinessInfoName.findAll", query="SELECT m FROM MwmDocBusinessInfoName m")
public class MwmDocBusinessInfoName extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_BUSINESS_INFO_NAME_ID")
	private long docBusinessInfoNameId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DATA_TYPE")
	private String dataType;

	@Column(name="DOC_BUSINESS_INFO_CODE")
	private String docBusinessInfoCode;

	@Column(name="DOC_BUSINESS_INFO_NAME")
	private String docBusinessInfoName;

	@Column(name="SCREEN_PARTS_INPUT_FLAG")
	private String screenPartsInputFlag;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="VALID_FLAG")
	private String validFlag;

	public MwmDocBusinessInfoName() {
	}

	public long getDocBusinessInfoNameId() {
		return this.docBusinessInfoNameId;
	}

	public void setDocBusinessInfoNameId(long docBusinessInfoNameId) {
		this.docBusinessInfoNameId = docBusinessInfoNameId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDocBusinessInfoCode() {
		return this.docBusinessInfoCode;
	}

	public void setDocBusinessInfoCode(String docBusinessInfoCode) {
		this.docBusinessInfoCode = docBusinessInfoCode;
	}

	public String getDocBusinessInfoName() {
		return this.docBusinessInfoName;
	}

	public void setDocBusinessInfoName(String docBusinessInfoName) {
		this.docBusinessInfoName = docBusinessInfoName;
	}

	public String getScreenPartsInputFlag() {
		return this.screenPartsInputFlag;
	}

	public void setScreenPartsInputFlag(String screenPartsInputFlag) {
		this.screenPartsInputFlag = screenPartsInputFlag;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}