package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_MAIL_TEMPLATE_HEADER database table.
 *
 */
@Entity
@Table(name="MWM_MAIL_TEMPLATE_HEADER", uniqueConstraints=@UniqueConstraint(columnNames={"MAIL_TEMPLATE_FILE_ID", "CORPORATION_CODE"}))
@NamedQuery(name="MwmMailTemplateHeader.findAll", query="SELECT m FROM MwmMailTemplateHeader m")
public class MwmMailTemplateHeader extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MAIL_TEMPLATE_HEADER_ID")
	private long mailTemplateHeaderId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="MAIL_TEMPLATE_FILE_ID")
	private Long mailTemplateFileId;

	@Column(name="RETURN_TO")
	private String returnTo;

	@Column(name="SEND_BCC")
	private String sendBcc;

	@Column(name="SEND_CC")
	private String sendCc;

	@Column(name="SEND_FROM")
	private String sendFrom;

	@Column(name="SEND_FROM_PERSONAL")
	private String sendFromPersonal;

	@Column(name="SEND_TO")
	private String sendTo;

	public MwmMailTemplateHeader() {
	}

	public long getMailTemplateHeaderId() {
		return this.mailTemplateHeaderId;
	}

	public void setMailTemplateHeaderId(long mailTemplateHeaderId) {
		this.mailTemplateHeaderId = mailTemplateHeaderId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Long getMailTemplateFileId() {
		return this.mailTemplateFileId;
	}

	public void setMailTemplateFileId(Long mailTemplateFileId) {
		this.mailTemplateFileId = mailTemplateFileId;
	}

	public String getReturnTo() {
		return this.returnTo;
	}

	public void setReturnTo(String returnTo) {
		this.returnTo = returnTo;
	}

	public String getSendBcc() {
		return this.sendBcc;
	}

	public void setSendBcc(String sendBcc) {
		this.sendBcc = sendBcc;
	}

	public String getSendCc() {
		return this.sendCc;
	}

	public void setSendCc(String sendCc) {
		this.sendCc = sendCc;
	}

	public String getSendFrom() {
		return this.sendFrom;
	}

	public void setSendFrom(String sendFrom) {
		this.sendFrom = sendFrom;
	}

	public String getSendFromPersonal() {
		return sendFromPersonal;
	}

	public void setSendFromPersonal(String sendFromPersonal) {
		this.sendFromPersonal = sendFromPersonal;
	}

	public String getSendTo() {
		return this.sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

}