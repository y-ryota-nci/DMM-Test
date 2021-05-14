package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_UPLOAD_REGISTERED database table.
 *
 */
@Entity
@Table(name="MWT_UPLOAD_REGISTERED")
@NamedQuery(name="MwtUploadRegistered.findAll", query="SELECT m FROM MwtUploadRegistered m")
public class MwtUploadRegistered extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="UPLOAD_FILE_REGISTERED_ID")
	private long uploadFileRegisteredId;

	@Column(name="LATEST_FLAG")
	private String latestFlag;

	@Lob
	@Column(name="REGISTERED_CONFIG")
	private String registeredConfig;

	@Column(name="REGISTERED_CORPORATION_CODE")
	private String registeredCorporationCode;

	@Column(name="REGISTERED_DATETIME")
	private Timestamp registeredDatetime;

	@Column(name="REGISTERED_USER_CODE")
	private String registeredUserCode;

	@Column(name="UPLOAD_FILE_ID")
	private Long uploadFileId;

	public MwtUploadRegistered() {
	}

	public long getUploadFileRegisteredId() {
		return this.uploadFileRegisteredId;
	}

	public void setUploadFileRegisteredId(long uploadFileRegisteredId) {
		this.uploadFileRegisteredId = uploadFileRegisteredId;
	}

	public String getLatestFlag() {
		return this.latestFlag;
	}

	public void setLatestFlag(String latestFlag) {
		this.latestFlag = latestFlag;
	}

	public String getRegisteredConfig() {
		return this.registeredConfig;
	}

	public void setRegisteredConfig(String registeredConfig) {
		this.registeredConfig = registeredConfig;
	}

	public String getRegisteredCorporationCode() {
		return this.registeredCorporationCode;
	}

	public void setRegisteredCorporationCode(String registeredCorporationCode) {
		this.registeredCorporationCode = registeredCorporationCode;
	}

	public Timestamp getRegisteredDatetime() {
		return this.registeredDatetime;
	}

	public void setRegisteredDatetime(Timestamp registeredDatetime) {
		this.registeredDatetime = registeredDatetime;
	}

	public String getRegisteredUserCode() {
		return this.registeredUserCode;
	}

	public void setRegisteredUserCode(String registeredUserCode) {
		this.registeredUserCode = registeredUserCode;
	}

	public Long getUploadFileId() {
		return this.uploadFileId;
	}

	public void setUploadFileId(Long uploadFileId) {
		this.uploadFileId = uploadFileId;
	}

}