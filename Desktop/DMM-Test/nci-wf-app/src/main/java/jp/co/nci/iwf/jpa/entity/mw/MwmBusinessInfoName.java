package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The persistent class for the MWM_BUSINESS_INFO_NAME database table.
 *
 */
@Entity
@Table(name="MWM_BUSINESS_INFO_NAME", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE","BUSINESS_INFO_CODE"}))
@NamedQuery(name="MwmBusinessInfoName.findAll", query="SELECT m FROM MwmBusinessInfoName m")
public class MwmBusinessInfoName extends MwmBaseJpaEntity implements Serializable {

	@Id
	@Column(name="BUSINESS_INFO_NAME_ID")
	private Long businessInfoNameId;
	@Column(name="CORPORATION_CODE")
	private String corporationCode;
	@Column(name="BUSINESS_INFO_CODE")
	private String businessInfoCode;
	@Column(name="BUSINESS_INFO_NAME")
	private String businessInfoName;
	@Column(name="BUSINESS_INFO_TYPE")
	private String businessInfoType;
	@Column(name="VALID_FLAG")
	private String validFlag;
	@Column(name="SCREEN_PARTS_INPUT_FLAG")
	private String screenPartsInputFlag;
	@Column(name="DATA_TYPE")
	private String dataType;
	@Column(name="SORT_ORDER")
	private Long sortOrder;

	public Long getBusinessInfoNameId(){
		return this.businessInfoNameId;
	}

	public void setBusinessInfoNameId(final Long val){
		this.businessInfoNameId = val;
	}

	public String getCorporationCode(){
		return this.corporationCode;
	}

	public void setCorporationCode(final String val){
		this.corporationCode = val;
	}

	public String getBusinessInfoCode(){
		return this.businessInfoCode;
	}

	public void setBusinessInfoCode(final String val){
		this.businessInfoCode = val;
	}

	public String getBusinessInfoName(){
		return this.businessInfoName;
	}

	public void setBusinessInfoName(final String val){
		this.businessInfoName = val;
	}

	public String getBusinessInfoType(){
		return this.businessInfoType;
	}

	public void setBusinessInfoType(final String val){
		this.businessInfoType = val;
	}

	public String getValidFlag(){
		return this.validFlag;
	}

	public void setValidFlag(final String val){
		this.validFlag = val;
	}

	public String getScreenPartsInputFlag() {
		return screenPartsInputFlag;
	}

	public void setScreenPartsInputFlag(final String val) {
		this.screenPartsInputFlag = val;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(final String val) {
		this.dataType = val;
	}

	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(final Long val) {
		this.sortOrder = val;
	}

}
