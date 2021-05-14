package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_MAIL_TEMPLATE_FILE database table.
 *
 */
@Entity
@Table(name="MWM_MAIL_TEMPLATE_FILE", uniqueConstraints=@UniqueConstraint(columnNames={"MAIL_TEMPLATE_FILENAME"}))
@NamedQuery(name="MwmMailTemplateFile.findAll", query="SELECT m FROM MwmMailTemplateFile m")
public class MwmMailTemplateFile extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MAIL_TEMPLATE_FILE_ID")
	private long mailTemplateFileId;

	@Column(name="MAIL_TEMPLATE_FILENAME")
	private String mailTemplateFilename;

	private String remarks;

	public MwmMailTemplateFile() {
	}

	public long getMailTemplateFileId() {
		return this.mailTemplateFileId;
	}

	public void setMailTemplateFileId(long mailTemplateFileId) {
		this.mailTemplateFileId = mailTemplateFileId;
	}

	public String getMailTemplateFilename() {
		return this.mailTemplateFilename;
	}

	public void setMailTemplateFilename(String mailTemplateFilename) {
		this.mailTemplateFilename = mailTemplateFilename;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}