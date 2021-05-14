package jp.co.nci.iwf.jpa.entity.mw;

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

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;


/**
 * The persistent class for the WFV_USER_PASSWORD database table.
 *
 */
@Entity
@Table(name="WFV_USER_PASSWORD")
@NamedQuery(name="WfvUserPassword.findAll", query="SELECT w FROM WfvUserPassword w")
public class WfvUserPassword extends BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CHANGE_REQUEST_FLAG")
	private String changeRequestFlag;

	@Column(name="CHANGE_REQUEST_FLAG_NAME")
	private String changeRequestFlagName;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CORPORATION_NAME")
	private String corporationName;

	@Column(name="DELETE_FLAG")
	private String deleteFlag;

	@Column(name="EXIST_PASSWORD_FLAG")
	private String existPasswordFlag;

	@Column(name="EXIST_PASSWORD_FLAG_NAME")
	private String existPasswordFlagName;

	@Column(name="LOCALE_CODE")
	private String localeCode;

	@Column(name="LOCK_FLAG")
	private String lockFlag;

	@Column(name="LOCK_FLAG_NAME")
	private String lockFlagName;

	@Column(name="LOGIN_NG_COUNT")
	private Long loginNgCount;

	@Column(name="LOGIN_TIMESTAMP")
	private Timestamp loginTimestamp;

	@Column(name="LOGIN_TIMESTAMP_PREV")
	private Timestamp loginTimestampPrev;

	private String password;

	@Column(name="PASSWORD_EXPIRED_FLAG")
	private String passwordExpiredFlag;

	@Column(name="PASSWORD_VALIDITY_TERM")
	private BigDecimal passwordValidityTerm;

	@Id
	@Column(name="ROW_NUMBER")
	private Long rowNumber;

	@Column(name="SEQ_NO_USER_PASSWORD")
	private Integer seqNoUserPassword;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_ADDED_INFO")
	private String userAddedInfo;

	@Column(name="USER_CODE")
	private String userCode;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	@Column(name="USER_NAME")
	private String userName;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	private Date validStartDate;

	public WfvUserPassword() {
	}

	public String getChangeRequestFlag() {
		return this.changeRequestFlag;
	}

	public void setChangeRequestFlag(String changeRequestFlag) {
		this.changeRequestFlag = changeRequestFlag;
	}

	public String getChangeRequestFlagName() {
		return this.changeRequestFlagName;
	}

	public void setChangeRequestFlagName(String changeRequestFlagName) {
		this.changeRequestFlagName = changeRequestFlagName;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
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

	public String getCorporationName() {
		return this.corporationName;
	}

	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}

	public String getDeleteFlag() {
		return this.deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getExistPasswordFlag() {
		return this.existPasswordFlag;
	}

	public void setExistPasswordFlag(String existPasswordFlag) {
		this.existPasswordFlag = existPasswordFlag;
	}

	public String getExistPasswordFlagName() {
		return this.existPasswordFlagName;
	}

	public void setExistPasswordFlagName(String existPasswordFlagName) {
		this.existPasswordFlagName = existPasswordFlagName;
	}

	public String getLocaleCode() {
		return this.localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getLockFlag() {
		return this.lockFlag;
	}

	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}

	public String getLockFlagName() {
		return this.lockFlagName;
	}

	public void setLockFlagName(String lockFlagName) {
		this.lockFlagName = lockFlagName;
	}

	public Long getLoginNgCount() {
		return this.loginNgCount;
	}

	public void setLoginNgCount(Long loginNgCount) {
		this.loginNgCount = loginNgCount;
	}

	public Timestamp getLoginTimestamp() {
		return this.loginTimestamp;
	}

	public void setLoginTimestamp(Timestamp loginTimestamp) {
		this.loginTimestamp = loginTimestamp;
	}

	public Timestamp getLoginTimestampPrev() {
		return this.loginTimestampPrev;
	}

	public void setLoginTimestampPrev(Timestamp loginTimestampPrev) {
		this.loginTimestampPrev = loginTimestampPrev;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordExpiredFlag() {
		return this.passwordExpiredFlag;
	}

	public void setPasswordExpiredFlag(String passwordExpiredFlag) {
		this.passwordExpiredFlag = passwordExpiredFlag;
	}

	public BigDecimal getPasswordValidityTerm() {
		return this.passwordValidityTerm;
	}

	public void setPasswordValidityTerm(BigDecimal passwordValidityTerm) {
		this.passwordValidityTerm = passwordValidityTerm;
	}

	public Long getRowNumber() {
		return this.rowNumber;
	}

	public void setRowNumber(Long rowNumber) {
		this.rowNumber = rowNumber;
	}

	public Integer getSeqNoUserPassword() {
		return this.seqNoUserPassword;
	}

	public void setSeqNoUserPassword(Integer seqNoUserPassword) {
		this.seqNoUserPassword = seqNoUserPassword;
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

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getValidStartDate() {
		return this.validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

}