package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWM_ANNOUNCEMENT database table.
 *
 */
@Entity
@Table(name="MWM_ANNOUNCEMENT")
@NamedQuery(name="MwmAnnouncement.findAll", query="SELECT m FROM MwmAnnouncement m")
public class MwmAnnouncement extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ANNOUNCEMENT_ID")
	private long announcementId;

	@Column(name="\"CONTENTS\"")
	private String contents;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="LINK_TITLE")
	private String linkTitle;

	@Column(name="LINK_URL")
	private String linkUrl;

	private String remarks;

	private String subject;

	@Column(name="TIMESTAMP_END")
	private Timestamp timestampEnd;

	@Column(name="TIMESTAMP_START")
	private Timestamp timestampStart;

	public MwmAnnouncement() {
	}

	public long getAnnouncementId() {
		return this.announcementId;
	}

	public void setAnnouncementId(long announcementId) {
		this.announcementId = announcementId;
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

	public String getLinkTitle() {
		return this.linkTitle;
	}

	public void setLinkTitle(String linkTitle) {
		this.linkTitle = linkTitle;
	}

	public String getLinkUrl() {
		return this.linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Timestamp getTimestampEnd() {
		return this.timestampEnd;
	}

	public void setTimestampEnd(Timestamp timestampEnd) {
		this.timestampEnd = timestampEnd;
	}

	public Timestamp getTimestampStart() {
		return this.timestampStart;
	}

	public void setTimestampStart(Timestamp timestampStart) {
		this.timestampStart = timestampStart;
	}

}