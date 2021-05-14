package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_MAIL_TEMPLATE_BODY database table.
 *
 */
@Entity
@Table(name="MWM_MAIL_TEMPLATE_BODY", uniqueConstraints=@UniqueConstraint(columnNames={"MAIL_TEMPLATE_HEADER_ID", "LOCALE_CODE"}))
@NamedQuery(name="MwmMailTemplateBody.findAll", query="SELECT m FROM MwmMailTemplateBody m")
public class MwmMailTemplateBody extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MAIL_TEMPLATE_BODY_ID")
	private long mailTemplateBodyId;

	@Column(name="LOCALE_CODE")
	private String localeCode;

	@Lob
	@Column(name="MAIL_BODY")
	private String mailBody;

	@Column(name="MAIL_SUBJECT")
	private String mailSubject;

	@Column(name="MAIL_TEMPLATE_HEADER_ID")
	private Long mailTemplateHeaderId;

	public MwmMailTemplateBody() {
	}

	public long getMailTemplateBodyId() {
		return this.mailTemplateBodyId;
	}

	public void setMailTemplateBodyId(long mailTemplateBodyId) {
		this.mailTemplateBodyId = mailTemplateBodyId;
	}

	public String getLocaleCode() {
		return this.localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getMailBody() {
		return this.mailBody;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public String getMailSubject() {
		return this.mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public Long getMailTemplateHeaderId() {
		return this.mailTemplateHeaderId;
	}

	public void setMailTemplateHeaderId(Long mailTemplateHeaderId) {
		this.mailTemplateHeaderId = mailTemplateHeaderId;
	}

}