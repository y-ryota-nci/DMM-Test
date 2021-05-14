package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_ATTACH_FILE_WF database table.
 *
 */
@Entity
@Table(name="MWT_ATTACH_FILE_WF")
@NamedQuery(name="MwtAttachFileWf.findAll", query="SELECT m FROM MwtAttachFileWf m")
public class MwtAttachFileWf extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ATTACH_FILE_WF_ID")
	private long attachFileWfId;

	private String comments;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Lob
	@Column(name="FILE_DATA")
	private byte[] fileData;

	@Column(name="FILE_NAME")
	private String fileName;

	@Column(name="FILE_SIZE")
	private Integer fileSize;

	@Column(name="PROCESS_ID")
	private Long processId;

	public MwtAttachFileWf() {
	}

	public long getAttachFileWfId() {
		return this.attachFileWfId;
	}

	public void setAttachFileWfId(long attachFileWfId) {
		this.attachFileWfId = attachFileWfId;
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

	public Long getProcessId() {
		return this.processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

}