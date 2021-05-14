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
 * The persistent class for the MWM_PARTS_ATTACH_FILE database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_ATTACH_FILE", uniqueConstraints=@UniqueConstraint(columnNames={"PARTS_ID", "GROUP_KEY"}))
@NamedQuery(name="MwmPartsAttachFile.findAll", query="SELECT m FROM MwmPartsAttachFile m")
public class MwmPartsAttachFile extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_ATTACH_FILE_ID")
	private long partsAttachFileId;

	private String comments;

	@Lob
	@Column(name="FILE_DATA")
	private byte[] fileData;

	@Column(name="FILE_NAME")
	private String fileName;

	@Column(name="FILE_SIZE")
	private Integer fileSize;

	@Column(name="GROUP_KEY")
	private String groupKey;

	@Column(name="PARTS_ID")
	private Long partsId;

	public MwmPartsAttachFile() {
	}

	public long getPartsAttachFileId() {
		return this.partsAttachFileId;
	}

	public void setPartsAttachFileId(long partsAttachFileId) {
		this.partsAttachFileId = partsAttachFileId;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public String getGroupKey() {
		return this.groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

}