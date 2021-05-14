package jp.co.nci.iwf.jpa.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_RESET_PASSWORD database table.
 *
 */
@Entity
@Table(name="MWT_RESET_PASSWORD")
@NamedQuery(name="MwtResetPassword.findAll", query="SELECT m FROM MwtResetPassword m")
public class MwtResetPassword extends jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="RESET_PASSWORD_ID")
	private long resetPasswordId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="ISSUE_DATETIME")
	private Timestamp issueDatetime;

	private String password;

	@Column(name="RESET_DATETIME")
	private Timestamp resetDatetime;

	@Column(name="USER_CODE")
	private String userCode;

	public MwtResetPassword() {
	}

	public long getResetPasswordId() {
		return this.resetPasswordId;
	}

	public void setResetPasswordId(long resetPasswordId) {
		this.resetPasswordId = resetPasswordId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Timestamp getIssueDatetime() {
		return this.issueDatetime;
	}

	public void setIssueDatetime(Timestamp issueDatetime) {
		this.issueDatetime = issueDatetime;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getResetDatetime() {
		return this.resetDatetime;
	}

	public void setResetDatetime(Timestamp resetDatetime) {
		this.resetDatetime = resetDatetime;
	}

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

}