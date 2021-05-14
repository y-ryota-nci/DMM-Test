package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_DOC_FILE_DATA database table.
 *
 */
@Entity
@Table(name="MWT_DOC_FILE_DATA")
@NamedQuery(name="MwtDocFileData.findAll", query="SELECT m FROM MwtDocFileData m")
public class MwtDocFileData extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_FILE_DATA_ID")
	private long docFileDataId;

	@Column(name="CSET")
	private String cset;

	@Lob
	@Column(name="FILE_DATA")
	private byte[] fileData;

	@Column(name="FILE_NAME")
	private String fileName;

	@Column(name="FILE_SIZE")
	private Integer fileSize;

	@Column(name="FILE_TYPE")
	private String fileType;

	@Column(name="FMT")
	private String fmt;

	public MwtDocFileData() {
	}

	public long getDocFileDataId() {
		return this.docFileDataId;
	}

	public void setDocFileDataId(long docFileDataId) {
		this.docFileDataId = docFileDataId;
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

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFmt() {
		return fmt;
	}

	public void setFmt(String fmt) {
		this.fmt = fmt;
	}

}