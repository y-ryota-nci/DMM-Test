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
 * The persistent class for the MWT_PARTS_ATTACH_FILE_WF database table.
 *
 */
@Entity
@Table(name="MWT_PARTS_ATTACH_FILE_WF", uniqueConstraints=@UniqueConstraint(columnNames={"RUNTIME_ID", "PARTS_ID", "SORT_ORDER"}))
@NamedQuery(name="MwtPartsAttachFileWf.findAll", query="SELECT m FROM MwtPartsAttachFileWf m")
public class MwtPartsAttachFileWf extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_ATTACH_FILE_WF_ID")
	private long partsAttachFileWfId;

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

	@Column(name="PARTS_ID")
	private Long partsId;

	@Column(name="PROCESS_ID")
	private Long processId;

	@Column(name="RUNTIME_ID")
	private Long runtimeId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwtPartsAttachFileWf() {
	}

	public long getPartsAttachFileWfId() {
		return this.partsAttachFileWfId;
	}

	public void setPartsAttachFileWfId(long partsAttachFileWfId) {
		this.partsAttachFileWfId = partsAttachFileWfId;
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

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

	public Long getProcessId() {
		return this.processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public Long getRuntimeId() {
		return this.runtimeId;
	}

	public void setRuntimeId(Long runtimeId) {
		this.runtimeId = runtimeId;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}