package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;


/**
 * The persistent class for the MWT_DOC_FILE_DATA database table.
 *
 */
@Entity
public class MwtDocFileDataEx extends MwmBaseJpaEntity {

	@Id
	@Column(name="DOC_FILE_DATA_ID")
	private long docFileDataId;

	@Column(name="FILE_NAME")
	private String fileName;

	@Column(name="FILE_SIZE")
	private Integer fileSize;

	@Column(name="FILE_TYPE")
	private String fileType;

	@Column(name="USER_NAME_CREATED")
	private String userNameCreated;

	@Column(name="USER_NAME_UPDATED")
	private String userNameUpdated;

	public long getDocFileDataId() {
		return this.docFileDataId;
	}

	public void setDocFileDataId(long docFileDataId) {
		this.docFileDataId = docFileDataId;
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

	public String getUserNameCreated() {
		return this.userNameCreated;
	}

	public void setUserNameCreated(String userNameCreated) {
		this.userNameCreated = userNameCreated;
	}

	public String getUserNameUpdated() {
		return this.userNameUpdated;
	}

	public void setUserNameUpdated(String userNameUpdated) {
		this.userNameUpdated = userNameUpdated;
	}
}