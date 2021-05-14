package jp.co.nci.iwf.jpa.entity.wm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;


/**
 * The persistent class for the WFV_ORGANIZATION database table.
 *
 */
@Entity
@Table(name="WFV_ORGANIZATION")
@NamedQuery(name="WfvOrganization.findAll", query="SELECT w FROM WfvOrganization w")
public class WfvOrganization extends BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="ORGANIZATION_CODE")
	private String organizationCode;

	@Column(name="LOCALE_CODE")
	private String localeCode;

	private String address;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DELETE_FLAG")
	private String deleteFlag;

	@Column(name="DELETE_FLAG_NAME")
	private String deleteFlagName;

	@Column(name="EXTENDED_INFO_01")
	private String extendedInfo01;

	@Column(name="EXTENDED_INFO_02")
	private String extendedInfo02;

	@Column(name="EXTENDED_INFO_03")
	private String extendedInfo03;

	@Column(name="EXTENDED_INFO_04")
	private String extendedInfo04;

	@Column(name="EXTENDED_INFO_05")
	private String extendedInfo05;

	@Column(name="EXTENDED_INFO_06")
	private String extendedInfo06;

	@Column(name="EXTENDED_INFO_07")
	private String extendedInfo07;

	@Column(name="EXTENDED_INFO_08")
	private String extendedInfo08;

	@Column(name="EXTENDED_INFO_09")
	private String extendedInfo09;

	@Column(name="EXTENDED_INFO_10")
	private String extendedInfo10;

	@Column(name="FAX_NUM")
	private String faxNum;

	@Id
	private BigDecimal id;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="ORGANIZATION_ADDED_INFO")
	private String organizationAddedInfo;

	@Column(name="ORGANIZATION_ADDED_INFO_UP")
	private String organizationAddedInfoUp;

	@Column(name="ORGANIZATION_CODE_UP")
	private String organizationCodeUp;

	@Column(name="ORGANIZATION_LEVEL")
	private BigDecimal organizationLevel;

	@Column(name="ORGANIZATION_NAME")
	private String organizationName;

	@Column(name="ORGANIZATION_NAME_ABBR")
	private String organizationNameAbbr;

	@Column(name="ORGANIZATION_NAME_UP")
	private String organizationNameUp;

	@Column(name="ORGANIZATION_TREE_NAME")
	private String organizationTreeName;

	@Column(name="POST_NUM")
	private String postNum;

	@Column(name="SORT_ORDER")
	private BigDecimal sortOrder;

	@Column(name="TEL_NUM")
	private String telNum;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Version
	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	private Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	private Date validStartDate;

	public WfvOrganization() {
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCorporationCodeCreated() {
		return this.corporationCodeCreated;
	}

	public void setCorporationCodeCreated(String corporationCodeCreated) {
		this.corporationCodeCreated = corporationCodeCreated;
	}

	public String getCorporationCodeUpdated() {
		return this.corporationCodeUpdated;
	}

	public void setCorporationCodeUpdated(String corporationCodeUpdated) {
		this.corporationCodeUpdated = corporationCodeUpdated;
	}

	public String getDeleteFlag() {
		return this.deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getDeleteFlagName() {
		return this.deleteFlagName;
	}

	public void setDeleteFlagName(String deleteFlagName) {
		this.deleteFlagName = deleteFlagName;
	}

	public String getExtendedInfo01() {
		return this.extendedInfo01;
	}

	public void setExtendedInfo01(String extendedInfo01) {
		this.extendedInfo01 = extendedInfo01;
	}

	public String getExtendedInfo02() {
		return this.extendedInfo02;
	}

	public void setExtendedInfo02(String extendedInfo02) {
		this.extendedInfo02 = extendedInfo02;
	}

	public String getExtendedInfo03() {
		return this.extendedInfo03;
	}

	public void setExtendedInfo03(String extendedInfo03) {
		this.extendedInfo03 = extendedInfo03;
	}

	public String getExtendedInfo04() {
		return this.extendedInfo04;
	}

	public void setExtendedInfo04(String extendedInfo04) {
		this.extendedInfo04 = extendedInfo04;
	}

	public String getExtendedInfo05() {
		return this.extendedInfo05;
	}

	public void setExtendedInfo05(String extendedInfo05) {
		this.extendedInfo05 = extendedInfo05;
	}

	public String getExtendedInfo06() {
		return this.extendedInfo06;
	}

	public void setExtendedInfo06(String extendedInfo06) {
		this.extendedInfo06 = extendedInfo06;
	}

	public String getExtendedInfo07() {
		return this.extendedInfo07;
	}

	public void setExtendedInfo07(String extendedInfo07) {
		this.extendedInfo07 = extendedInfo07;
	}

	public String getExtendedInfo08() {
		return this.extendedInfo08;
	}

	public void setExtendedInfo08(String extendedInfo08) {
		this.extendedInfo08 = extendedInfo08;
	}

	public String getExtendedInfo09() {
		return this.extendedInfo09;
	}

	public void setExtendedInfo09(String extendedInfo09) {
		this.extendedInfo09 = extendedInfo09;
	}

	public String getExtendedInfo10() {
		return this.extendedInfo10;
	}

	public void setExtendedInfo10(String extendedInfo10) {
		this.extendedInfo10 = extendedInfo10;
	}

	public String getFaxNum() {
		return this.faxNum;
	}

	public void setFaxNum(String faxNum) {
		this.faxNum = faxNum;
	}

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getIpCreated() {
		return this.ipCreated;
	}

	public void setIpCreated(String ipCreated) {
		this.ipCreated = ipCreated;
	}

	public String getIpUpdated() {
		return this.ipUpdated;
	}

	public void setIpUpdated(String ipUpdated) {
		this.ipUpdated = ipUpdated;
	}

	public String getOrganizationAddedInfo() {
		return this.organizationAddedInfo;
	}

	public void setOrganizationAddedInfo(String organizationAddedInfo) {
		this.organizationAddedInfo = organizationAddedInfo;
	}

	public String getOrganizationAddedInfoUp() {
		return this.organizationAddedInfoUp;
	}

	public void setOrganizationAddedInfoUp(String organizationAddedInfoUp) {
		this.organizationAddedInfoUp = organizationAddedInfoUp;
	}

	public String getOrganizationCodeUp() {
		return this.organizationCodeUp;
	}

	public void setOrganizationCodeUp(String organizationCodeUp) {
		this.organizationCodeUp = organizationCodeUp;
	}

	public BigDecimal getOrganizationLevel() {
		return this.organizationLevel;
	}

	public void setOrganizationLevel(BigDecimal organizationLevel) {
		this.organizationLevel = organizationLevel;
	}

	public String getOrganizationName() {
		return this.organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationNameAbbr() {
		return this.organizationNameAbbr;
	}

	public void setOrganizationNameAbbr(String organizationNameAbbr) {
		this.organizationNameAbbr = organizationNameAbbr;
	}

	public String getOrganizationNameUp() {
		return this.organizationNameUp;
	}

	public void setOrganizationNameUp(String organizationNameUp) {
		this.organizationNameUp = organizationNameUp;
	}

	public String getOrganizationTreeName() {
		return this.organizationTreeName;
	}

	public void setOrganizationTreeName(String organizationTreeName) {
		this.organizationTreeName = organizationTreeName;
	}

	public String getPostNum() {
		return this.postNum;
	}

	public void setPostNum(String postNum) {
		this.postNum = postNum;
	}

	public BigDecimal getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(BigDecimal sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getTelNum() {
		return this.telNum;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	public Timestamp getTimestampCreated() {
		return this.timestampCreated;
	}

	public void setTimestampCreated(Timestamp timestampCreated) {
		this.timestampCreated = timestampCreated;
	}

	public Timestamp getTimestampUpdated() {
		return this.timestampUpdated;
	}

	public void setTimestampUpdated(Timestamp timestampUpdated) {
		this.timestampUpdated = timestampUpdated;
	}

	public String getUserCodeCreated() {
		return this.userCodeCreated;
	}

	public void setUserCodeCreated(String userCodeCreated) {
		this.userCodeCreated = userCodeCreated;
	}

	public String getUserCodeUpdated() {
		return this.userCodeUpdated;
	}

	public void setUserCodeUpdated(String userCodeUpdated) {
		this.userCodeUpdated = userCodeUpdated;
	}

	public Date getValidEndDate() {
		return this.validEndDate;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	public Date getValidStartDate() {
		return this.validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

	public String getCorporationCode() {
		return corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

}