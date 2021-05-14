package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_BINDER_INFO database table.
 *
 */
@Entity
@Table(name="MWT_BINDER_INFO")
@NamedQuery(name="MwtBinderInfo.findAll", query="SELECT m FROM MwtBinderInfo m")
public class MwtBinderInfo extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BINDER_ID")
	private long binderId;

	@Column(name="DOC_ID")
	private Long docId;

	public MwtBinderInfo() {
	}

	public long getBinderId() {
		return this.binderId;
	}

	public void setBinderId(long binderId) {
		this.binderId = binderId;
	}

	public Long getDocId() {
		return this.docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

}