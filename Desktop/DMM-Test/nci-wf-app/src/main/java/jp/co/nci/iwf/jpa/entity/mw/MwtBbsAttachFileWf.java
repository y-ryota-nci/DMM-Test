package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_BBS_ATTACH_FILE_WF database table.
 *
 */
@Entity
@Table(name="MWT_BBS_ATTACH_FILE_WF")
@NamedQuery(name="MwtBbsAttachFileWf.findAll", query="SELECT m FROM MwtBbsAttachFileWf m")
public class MwtBbsAttachFileWf extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BBS_ATTACH_FILE_WF_ID")
	private long bbsAttachFileWfId;

	@Lob
	@Column(name="FILE_DATA")
	private byte[] fileData;

	@Column(name="FILE_NAME")
	private String fileName;

	@Column(name="FILE_SIZE")
	private Integer fileSize;

	@Column(name="FILE_TYPE")
	private String fileType;

	@Column(name="PROCESS_BBS_ID")
	private Long processBbsId;

	public MwtBbsAttachFileWf() {
	}

	public long getBbsAttachFileWfId() {
		return this.bbsAttachFileWfId;
	}

	public void setBbsAttachFileWfId(long bbsAttachFileWfId) {
		this.bbsAttachFileWfId = bbsAttachFileWfId;
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

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getProcessBbsId() {
		return this.processBbsId;
	}

	public void setProcessBbsId(Long processBbsId) {
		this.processBbsId = processBbsId;
	}

}