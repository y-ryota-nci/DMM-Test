package jp.co.nci.iwf.jpa.entity.wm;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the WFV_SELECT_START_USER database table.
 *
 */
@Entity
@Table(name="WFV_SELECT_START_USER")
@NamedQuery(name="WfvSelectStartUser.findAll", query="SELECT w FROM WfvSelectStartUser w")
public class WfvSelectStartUser extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="AUTH_TRANSFER_TYPE")
	private String authTransferType;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="CORPORATION_CODE_P")
	private String corporationCodeP;

	@Column(name="CORPORATION_NAME")
	private String corporationName;

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

	@Column(name="JOB_TYPE")
	private String jobType;

	@Column(name="JOB_TYPE_NAME")
	private String jobTypeName;

	@Id
	@Column(name="ID")
	private long id;

	@Column(name="LOCALE_CODE")
	private String localeCode;

	@Column(name="MAIL_ADDRESS")
	private String mailAddress;

	@Column(name="ORGANIZATION_ADDED_INFO")
	private String organizationAddedInfo;

	@Column(name="ORGANIZATION_CODE")
	private String organizationCode;

	@Column(name="ORGANIZATION_CODE_5")
	private String organizationCode5;

	@Column(name="ORGANIZATION_CODE_UP_3")
	private String organizationCodeUp3;

	@Column(name="ORGANIZATION_NAME")
	private String organizationName;

	@Column(name="ORGANIZATION_NAME_5")
	private String organizationName5;

	@Column(name="ORGANIZATION_TREE_NAME")
	private String organizationTreeName;

	@Column(name="PAY_APPL_CD")
	private String payApplCd;

	@Column(name="POST_ADDED_INFO")
	private String postAddedInfo;

	@Column(name="POST_CODE")
	private String postCode;

	@Column(name="POST_NAME")
	private String postName;

	@Column(name="PROCESS_DEF_CODE")
	private String processDefCode;

	@Column(name="PROCESS_DEF_DETAIL_CODE")
	private String processDefDetailCode;

	@Column(name="SEQ_NO_AUTH_TRANSFER")
	private BigDecimal seqNoAuthTransfer;

	@Column(name="SBMTR_ADDR")
	private String sbmtrAddr;

	@Column(name="TEL_NUM")
	private String telNum;

	@Column(name="TEL_NUM_CEL")
	private String telNumCel;

	@Column(name="USER_ADDED_INFO")
	private String userAddedInfo;

	@Column(name="USER_CODE")
	private String userCode;

	@Column(name="USER_CODE_TRANSFER")
	private String userCodeTransfer;

	@Column(name="USER_NAME")
	private String userName;

	@Column(name="USER_NAME_ABBR")
	private String userNameAbbr;

	public WfvSelectStartUser() {
	}

	public String getAuthTransferType() {
		return this.authTransferType;
	}

	public void setAuthTransferType(String authTransferType) {
		this.authTransferType = authTransferType;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getCorporationCodeP() {
		return this.corporationCodeP;
	}

	public void setCorporationCodeP(String corporationCodeP) {
		this.corporationCodeP = corporationCodeP;
	}

	public String getCorporationName() {
		return this.corporationName;
	}

	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
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

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobTypeName() {
		return jobTypeName;
	}

	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocaleCode() {
		return this.localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getMailAddress() {
		return this.mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getOrganizationAddedInfo() {
		return this.organizationAddedInfo;
	}

	public void setOrganizationAddedInfo(String organizationAddedInfo) {
		this.organizationAddedInfo = organizationAddedInfo;
	}

	public String getOrganizationCode() {
		return this.organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getOrganizationName() {
		return this.organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationTreeName() {
		return this.organizationTreeName;
	}

	public void setOrganizationTreeName(String organizationTreeName) {
		this.organizationTreeName = organizationTreeName;
	}

	public String getPayApplCd() {
		return payApplCd;
	}

	public void setPayApplCd(String payApplCd) {
		this.payApplCd = payApplCd;
	}

	public String getPostAddedInfo() {
		return this.postAddedInfo;
	}

	public void setPostAddedInfo(String postAddedInfo) {
		this.postAddedInfo = postAddedInfo;
	}

	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPostName() {
		return this.postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getProcessDefCode() {
		return this.processDefCode;
	}

	public void setProcessDefCode(String processDefCode) {
		this.processDefCode = processDefCode;
	}

	public String getProcessDefDetailCode() {
		return this.processDefDetailCode;
	}

	public void setProcessDefDetailCode(String processDefDetailCode) {
		this.processDefDetailCode = processDefDetailCode;
	}

	public BigDecimal getSeqNoAuthTransfer() {
		return this.seqNoAuthTransfer;
	}

	public void setSeqNoAuthTransfer(BigDecimal seqNoAuthTransfer) {
		this.seqNoAuthTransfer = seqNoAuthTransfer;
	}

	public String getTelNum() {
		return this.telNum;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	public String getTelNumCel() {
		return this.telNumCel;
	}

	public void setTelNumCel(String telNumCel) {
		this.telNumCel = telNumCel;
	}

	public String getUserAddedInfo() {
		return this.userAddedInfo;
	}

	public void setUserAddedInfo(String userAddedInfo) {
		this.userAddedInfo = userAddedInfo;
	}

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserCodeTransfer() {
		return this.userCodeTransfer;
	}

	public void setUserCodeTransfer(String userCodeTransfer) {
		this.userCodeTransfer = userCodeTransfer;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNameAbbr() {
		return this.userNameAbbr;
	}

	public void setUserNameAbbr(String userNameAbbr) {
		this.userNameAbbr = userNameAbbr;
	}

	public String getOrganizationCodeUp3() {
		return organizationCodeUp3;
	}

	public void setOrganizationCodeUp3(String organizationCodeUp3) {
		this.organizationCodeUp3 = organizationCodeUp3;
	}

	public String getOrganizationName5() {
		return organizationName5;
	}

	public void setOrganizationName5(String organizationName5) {
		this.organizationName5 = organizationName5;
	}

	public String getSbmtrAddr() {
		return sbmtrAddr;
	}

	public void setSbmtrAddr(String sbmtrAddr) {
		this.sbmtrAddr = sbmtrAddr;
	}

	public String getOrganizationCode5() {
		return organizationCode5;
	}

	public void setOrganizationCode5(String organizationCode5) {
		this.organizationCode5 = organizationCode5;
	}

}