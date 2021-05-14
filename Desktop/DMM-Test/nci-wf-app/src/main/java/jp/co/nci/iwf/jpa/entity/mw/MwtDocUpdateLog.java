package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_DOC_UPDATE_LOG database table.
 *
 */
@Entity
@Table(name="MWT_DOC_UPDATE_LOG")
@NamedQuery(name="MwtDocUpdateLog.findAll", query="SELECT m FROM MwtDocUpdateLog m")
public class MwtDocUpdateLog extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_UPDATE_LOG_ID")
	private long docUpdateLogId;

	@Column(name="CONTENTS_TYPE")
	private String contentsType;

	@Column(name="DOC_FILE_HISTORY_ID")
	private Long docFileHistoryId;

	@Column(name="DOC_HISTORY_ID")
	private Long docHistoryId;

	@Column(name="DOC_ID")
	private Long docId;

	@Column(name="DOC_UPDATE_LOG")
	private String docUpdateLog;

	@Column(name="DOC_UPDATE_TIMESTAMP")
	private Timestamp docUpdateTimestamp;

	@Column(name="DOC_UPDATE_USER_NAME")
	private String docUpdateUserName;

	@Column(name="SEQ_NO")
	private Integer seqNo;

	public MwtDocUpdateLog() {
	}

	public long getDocUpdateLogId() {
		return this.docUpdateLogId;
	}

	public void setDocUpdateLogId(long docUpdateLogId) {
		this.docUpdateLogId = docUpdateLogId;
	}

	public String getContentsType() {
		return this.contentsType;
	}

	public void setContentsType(String contentsType) {
		this.contentsType = contentsType;
	}

	public Long getDocFileHistoryId() {
		return this.docFileHistoryId;
	}

	public void setDocFileHistoryId(Long docFileHistoryId) {
		this.docFileHistoryId = docFileHistoryId;
	}

	public Long getDocHistoryId() {
		return this.docHistoryId;
	}

	public void setDocHistoryId(Long docHistoryId) {
		this.docHistoryId = docHistoryId;
	}

	public Long getDocId() {
		return this.docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public String getDocUpdateLog() {
		return this.docUpdateLog;
	}

	public void setDocUpdateLog(String docUpdateLog) {
		this.docUpdateLog = docUpdateLog;
	}

	public Timestamp getDocUpdateTimestamp() {
		return this.docUpdateTimestamp;
	}

	public void setDocUpdateTimestamp(Timestamp docUpdateTimestamp) {
		this.docUpdateTimestamp = docUpdateTimestamp;
	}

	public String getDocUpdateUserName() {
		return this.docUpdateUserName;
	}

	public void setDocUpdateUserName(String docUpdateUserName) {
		this.docUpdateUserName = docUpdateUserName;
	}

	public Integer getSeqNo() {
		return this.seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

}