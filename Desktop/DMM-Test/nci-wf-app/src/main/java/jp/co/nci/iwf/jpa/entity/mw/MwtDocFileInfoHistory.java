package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_DOC_FILE_INFO_HISTORY database table.
 *
 */
@Entity
@Table(name="MWT_DOC_FILE_INFO_HISTORY")
@NamedQuery(name="MwtDocFileInfoHistory.findAll", query="SELECT m FROM MwtDocFileInfoHistory m")
public class MwtDocFileInfoHistory extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_FILE_HISTORY_ID")
	private long docFileHistoryId;

	private String comments;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DOC_FILE_DATA_ID")
	private Long docFileDataId;

	@Column(name="DOC_FILE_ID")
	private Long docFileId;

	@Column(name="DOC_FILE_NUM")
	private String docFileNum;

	@Column(name="DOC_FILE_WF_ID")
	private Long docFileWfId;

	@Column(name="DOC_ID")
	private Long docId;

	@Column(name="HISTORY_SEQ_NO")
	private Integer historySeqNo;

	@Column(name="LOCK_CORPORATION_CODE")
	private String lockCorporationCode;

	@Column(name="LOCK_FLAG")
	private String lockFlag;

	@Column(name="LOCK_TIMESTAMP")
	private Timestamp lockTimestamp;

	@Column(name="LOCK_USER_CODE")
	private String lockUserCode;

	@Column(name="LOCK_USER_NAME")
	private String lockUserName;

	@Column(name="MAJOR_VERSION")
	private Integer majorVersion;

	@Column(name="MINOR_VERSION")
	private Integer minorVersion;

	@Column(name="USER_NAME_CREATED")
	private String userNameCreated;

	@Column(name="USER_NAME_UPDATED")
	private String userNameUpdated;

	public MwtDocFileInfoHistory() {
	}

	public long getDocFileHistoryId() {
		return this.docFileHistoryId;
	}

	public void setDocFileHistoryId(long docFileHistoryId) {
		this.docFileHistoryId = docFileHistoryId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCorporationCode() {
		return corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Long getDocFileDataId() {
		return this.docFileDataId;
	}

	public void setDocFileDataId(Long docFileDataId) {
		this.docFileDataId = docFileDataId;
	}

	public Long getDocFileId() {
		return this.docFileId;
	}

	public void setDocFileId(Long docFileId) {
		this.docFileId = docFileId;
	}

	public String getDocFileNum() {
		return docFileNum;
	}

	public void setDocFileNum(String docFileNum) {
		this.docFileNum = docFileNum;
	}

	public Long getDocFileWfId() {
		return docFileWfId;
	}

	public void setDocFileWfId(Long docFileWfId) {
		this.docFileWfId = docFileWfId;
	}

	public Long getDocId() {
		return this.docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public Integer getHistorySeqNo() {
		return this.historySeqNo;
	}

	public void setHistorySeqNo(Integer historySeqNo) {
		this.historySeqNo = historySeqNo;
	}

	public String getLockCorporationCode() {
		return this.lockCorporationCode;
	}

	public void setLockCorporationCode(String lockCorporationCode) {
		this.lockCorporationCode = lockCorporationCode;
	}

	public String getLockFlag() {
		return this.lockFlag;
	}

	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}

	public Timestamp getLockTimestamp() {
		return this.lockTimestamp;
	}

	public void setLockTimestamp(Timestamp lockTimestamp) {
		this.lockTimestamp = lockTimestamp;
	}

	public String getLockUserCode() {
		return this.lockUserCode;
	}

	public void setLockUserCode(String lockUserCode) {
		this.lockUserCode = lockUserCode;
	}

	public String getLockUserName() {
		return this.lockUserName;
	}

	public void setLockUserName(String lockUserName) {
		this.lockUserName = lockUserName;
	}

	public Integer getMajorVersion() {
		return this.majorVersion;
	}

	public void setMajorVersion(Integer majorVersion) {
		this.majorVersion = majorVersion;
	}

	public Integer getMinorVersion() {
		return this.minorVersion;
	}

	public void setMinorVersion(Integer minorVersion) {
		this.minorVersion = minorVersion;
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