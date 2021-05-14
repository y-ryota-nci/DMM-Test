package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_PROCESS_BBS database table.
 *
 */
@Entity
@Table(name="MWT_PROCESS_BBS")
@NamedQuery(name="MwtProcessBbs.findAll", query="SELECT m FROM MwtProcessBbs m")
public class MwtProcessBbs extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PROCESS_BBS_ID")
	private long processBbsId;

	@Column(name="\"CONTENTS\"")
	private String contents;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="CORPORATION_CODE_SUBMIT")
	private String corporationCodeSubmit;

	@Column(name="ORGANIZATION_CODE_SUBMIT")
	private String organizationCodeSubmit;

	@Column(name="POST_CODE_SUBMIT")
	private String postCodeSubmit;

	@Column(name="PROCESS_BBS_ID_UP")
	private Long processBbsIdUp;

	@Column(name="PROCESS_BBS_MAIL_TYPE")
	private String processBbsMailType;

	@Column(name="PROCESS_ID")
	private Long processId;

	@Column(name="TIMESTAMP_SUBMIT")
	private Timestamp timestampSubmit;

	@Column(name="USER_CODE_SUBMIT")
	private String userCodeSubmit;

	public MwtProcessBbs() {
	}

	public long getProcessBbsId() {
		return this.processBbsId;
	}

	public void setProcessBbsId(long processBbsId) {
		this.processBbsId = processBbsId;
	}

	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getCorporationCodeSubmit() {
		return this.corporationCodeSubmit;
	}

	public void setCorporationCodeSubmit(String corporationCodeSubmit) {
		this.corporationCodeSubmit = corporationCodeSubmit;
	}

	public String getOrganizationCodeSubmit() {
		return this.organizationCodeSubmit;
	}

	public void setOrganizationCodeSubmit(String organizationCodeSubmit) {
		this.organizationCodeSubmit = organizationCodeSubmit;
	}

	public String getPostCodeSubmit() {
		return this.postCodeSubmit;
	}

	public void setPostCodeSubmit(String postCodeSubmit) {
		this.postCodeSubmit = postCodeSubmit;
	}

	public Long getProcessBbsIdUp() {
		return this.processBbsIdUp;
	}

	public void setProcessBbsIdUp(Long processBbsIdUp) {
		this.processBbsIdUp = processBbsIdUp;
	}

	public String getProcessBbsMailType() {
		return this.processBbsMailType;
	}

	public void setProcessBbsMailType(String processBbsMailType) {
		this.processBbsMailType = processBbsMailType;
	}

	public Long getProcessId() {
		return this.processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public Timestamp getTimestampSubmit() {
		return this.timestampSubmit;
	}

	public void setTimestampSubmit(Timestamp timestampSubmit) {
		this.timestampSubmit = timestampSubmit;
	}

	public String getUserCodeSubmit() {
		return this.userCodeSubmit;
	}

	public void setUserCodeSubmit(String userCodeSubmit) {
		this.userCodeSubmit = userCodeSubmit;
	}

}