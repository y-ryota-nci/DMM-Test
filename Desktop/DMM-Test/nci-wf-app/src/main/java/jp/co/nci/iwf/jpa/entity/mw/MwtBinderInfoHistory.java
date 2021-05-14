package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_BINDER_INFO_HISTORY database table.
 *
 */
@Entity
@Table(name="MWT_BINDER_INFO_HISTORY")
@NamedQuery(name="MwtBinderInfoHistory.findAll", query="SELECT m FROM MwtBinderInfoHistory m")
public class MwtBinderInfoHistory extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BINDER_HISTORY_ID")
	private long binderHistoryId;

	@Column(name="BINDER_ID")
	private Long binderId;

	@Column(name="DOC_HISTORY_ID")
	private Long docHistoryId;

	@Column(name="DOC_ID")
	private Long docId;

	@Column(name="HISTORY_SEQ_NO")
	private Integer historySeqNo;

	public MwtBinderInfoHistory() {
	}

	public long getBinderHistoryId() {
		return this.binderHistoryId;
	}

	public void setBinderHistoryId(long binderHistoryId) {
		this.binderHistoryId = binderHistoryId;
	}

	public Long getBinderId() {
		return this.binderId;
	}

	public void setBinderId(Long binderId) {
		this.binderId = binderId;
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

	public Integer getHistorySeqNo() {
		return this.historySeqNo;
	}

	public void setHistorySeqNo(Integer historySeqNo) {
		this.historySeqNo = historySeqNo;
	}

}