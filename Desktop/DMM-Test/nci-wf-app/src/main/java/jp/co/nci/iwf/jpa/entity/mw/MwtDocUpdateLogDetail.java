package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_DOC_UPDATE_LOG_DETAIL database table.
 *
 */
@Entity
@Table(name="MWT_DOC_UPDATE_LOG_DETAIL")
@NamedQuery(name="MwtDocUpdateLogDetail.findAll", query="SELECT m FROM MwtDocUpdateLogDetail m")
public class MwtDocUpdateLogDetail extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_UPDATE_LOG_DETAIL_ID")
	private long docUpdateLogDetailId;

	@Column(name="DOC_UPDATE_LOG_ID")
	private Long docUpdateLogId;

	public MwtDocUpdateLogDetail() {
	}

	public long getDocUpdateLogDetailId() {
		return this.docUpdateLogDetailId;
	}

	public void setDocUpdateLogDetailId(long docUpdateLogDetailId) {
		this.docUpdateLogDetailId = docUpdateLogDetailId;
	}

	public Long getDocUpdateLogId() {
		return this.docUpdateLogId;
	}

	public void setDocUpdateLogId(Long docUpdateLogId) {
		this.docUpdateLogId = docUpdateLogId;
	}

}