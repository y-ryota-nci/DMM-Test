package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_BIZ_DOC_INFO_HISTORY database table.
 *
 */
@Entity
@Table(name="MWT_BIZ_DOC_INFO_HISTORY")
@NamedQuery(name="MwtBizDocInfoHistory.findAll", query="SELECT m FROM MwtBizDocInfoHistory m")
public class MwtBizDocInfoHistory extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BIZ_DOC_HISTORY_ID")
	private long bizDocHistoryId;

	@Column(name="BIZ_DOC_ID")
	private Long bizDocId;

	@Column(name="DOC_HISTORY_ID")
	private Long docHistoryId;

	@Column(name="DOC_ID")
	private Long docId;

	@Column(name="HISTORY_SEQ_NO")
	private Integer historySeqNo;

	@Column(name="SCREEN_DOC_ID")
	private Long screenDocId;

	@Column(name="TRAN_ID")
	private Long tranId;

	public MwtBizDocInfoHistory() {
	}

	public long getBizDocHistoryId() {
		return this.bizDocHistoryId;
	}

	public void setBizDocHistoryId(long bizDocHistoryId) {
		this.bizDocHistoryId = bizDocHistoryId;
	}

	public Long getBizDocId() {
		return this.bizDocId;
	}

	public void setBizDocId(Long bizDocId) {
		this.bizDocId = bizDocId;
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

	public Long getScreenDocId() {
		return this.screenDocId;
	}

	public void setScreenDocId(Long screenDocId) {
		this.screenDocId = screenDocId;
	}

	public Long getTranId() {
		return this.tranId;
	}

	public void setTranId(Long tranId) {
		this.tranId = tranId;
	}

}