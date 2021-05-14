package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_UPLOAD_FILE database table.
 *
 */
@Entity
@Table(name="MWT_UPLOAD_FILE")
@NamedQuery(name="MwtUploadFile.findAll", query="SELECT m FROM MwtUploadFile m")
public class MwtUploadFile extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="UPLOAD_FILE_ID")
	private long uploadFileId;

	@Column(name="FILE_APP_VERSION")
	private String fileAppVersion;

	@Column(name="FILE_CORPORATION_CODE")
	private String fileCorporationCode;

	@Lob
	@Column(name="FILE_DATA")
	private byte[] fileData;

	@Column(name="FILE_DB_STRING")
	private String fileDbString;

	@Column(name="FILE_HOST_IP_ADDR")
	private String fileHostIpAddr;

	@Column(name="FILE_HOST_NAME")
	private String fileHostName;

	@Column(name="FILE_NAME")
	private String fileName;

	@Column(name="FILE_SIZE")
	private Integer fileSize;

	@Column(name="FILE_TIMESTAMP")
	private Timestamp fileTimestamp;

	@Column(name="REGISTERED_FLAG")
	private String registeredFlag;

	@Column(name="UPLOAD_CORPORATION_CODE")
	private String uploadCorporationCode;

	@Column(name="UPLOAD_DATETIME")
	private Timestamp uploadDatetime;

	@Column(name="UPLOAD_KIND")
	private String uploadKind;

	public MwtUploadFile() {
	}

	public long getUploadFileId() {
		return this.uploadFileId;
	}

	public void setUploadFileId(long uploadFileId) {
		this.uploadFileId = uploadFileId;
	}

	public String getFileAppVersion() {
		return this.fileAppVersion;
	}

	public void setFileAppVersion(String fileAppVersion) {
		this.fileAppVersion = fileAppVersion;
	}

	public String getFileCorporationCode() {
		return this.fileCorporationCode;
	}

	public void setFileCorporationCode(String fileCorporationCode) {
		this.fileCorporationCode = fileCorporationCode;
	}

	public byte[] getFileData() {
		return this.fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getFileDbString() {
		return this.fileDbString;
	}

	public void setFileDbString(String fileDbString) {
		this.fileDbString = fileDbString;
	}

	public String getFileHostIpAddr() {
		return fileHostIpAddr;
	}

	public void setFileHostIpAddr(String fileHostIpAddr) {
		this.fileHostIpAddr = fileHostIpAddr;
	}

	public String getFileHostName() {
		return this.fileHostName;
	}

	public void setFileHostName(String fileHostName) {
		this.fileHostName = fileHostName;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public Timestamp getFileTimestamp() {
		return this.fileTimestamp;
	}

	public void setFileTimestamp(Timestamp fileTimestamp) {
		this.fileTimestamp = fileTimestamp;
	}

	public String getRegisteredFlag() {
		return this.registeredFlag;
	}

	public void setRegisteredFlag(String registeredFlag) {
		this.registeredFlag = registeredFlag;
	}

	public String getUploadCorporationCode() {
		return this.uploadCorporationCode;
	}

	public void setUploadCorporationCode(String uploadCorporationCode) {
		this.uploadCorporationCode = uploadCorporationCode;
	}

	public Timestamp getUploadDatetime() {
		return this.uploadDatetime;
	}

	public void setUploadDatetime(Timestamp uploadDatetime) {
		this.uploadDatetime = uploadDatetime;
	}

	public String getUploadKind() {
		return this.uploadKind;
	}

	public void setUploadKind(String uploadKind) {
		this.uploadKind = uploadKind;
	}

}