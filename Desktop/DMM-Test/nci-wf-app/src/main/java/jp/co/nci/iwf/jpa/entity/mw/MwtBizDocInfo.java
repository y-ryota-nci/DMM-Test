package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_BIZ_DOC_INFO database table.
 *
 */
@Entity
@Table(name="MWT_BIZ_DOC_INFO")
@NamedQuery(name="MwtBizDocInfo.findAll", query="SELECT m FROM MwtBizDocInfo m")
public class MwtBizDocInfo extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BIZ_DOC_ID")
	private long bizDocId;

	@Column(name="DOC_ID")
	private Long docId;

	@Column(name="SCREEN_DOC_ID")
	private Long screenDocId;

	@Column(name="TRAN_ID")
	private Long tranId;

	public MwtBizDocInfo() {
	}

	public long getBizDocId() {
		return this.bizDocId;
	}

	public void setBizDocId(long bizDocId) {
		this.bizDocId = bizDocId;
	}

	public Long getDocId() {
		return this.docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
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