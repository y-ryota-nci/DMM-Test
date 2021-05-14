package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_DOC_FILE_WF database table.
 *
 */
@Entity
@Table(name="MWT_DOC_FILE_WF")
@NamedQuery(name="MwtDocFileWf.findAll", query="SELECT m FROM MwtDocFileWf m")
public class MwtDocFileWf extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_FILE_WF_ID")
	private long docFileWfId;

	private String comments;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="CSET")
	private String cset;

	@Lob
	@Column(name="FILE_DATA")
	private byte[] fileData;

	@Column(name="FILE_NAME")
	private String fileName;

	@Column(name="FILE_SIZE")
	private Integer fileSize;

	@Column(name="FMT")
	private String fmt;

	@Column(name="PROCESS_ID")
	private Long processId;

	public MwtDocFileWf() {
	}

	public long getDocFileWfId() {
		return this.docFileWfId;
	}

	public void setDocFileWfId(long docFileWfId) {
		this.docFileWfId = docFileWfId;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getCset() {
		return cset;
	}

	public void setCset(String cset) {
		this.cset = cset;
	}

	public byte[] getFileData() {
		return this.fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public String getFmt() {
		return fmt;
	}

	public void setFmt(String fmt) {
		this.fmt = fmt;
	}

	public Long getProcessId() {
		return this.processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

}