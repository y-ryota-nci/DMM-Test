package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_DOC_CONTENTS database table.
 *
 */
@Entity
@Table(name="MWT_DOC_CONTENTS_INFO")
@NamedQuery(name="MwtDocContentsInfo.findAll", query="SELECT m FROM MwtDocContentsInfo m")
public class MwtDocContentsInfo extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_CONTENTS_ID")
	private long docContentsId;

	@Lob
	@Column(name="DOC_CONTENTS")
	private String docContents;

	@Column(name="DOC_ID")
	private Long docId;

	public MwtDocContentsInfo() {
	}

	public long getDocContentsId() {
		return this.docContentsId;
	}

	public void setDocContentsId(long docContentsId) {
		this.docContentsId = docContentsId;
	}

	public String getDocContents() {
		return this.docContents;
	}

	public void setDocContents(String docContents) {
		this.docContents = docContents;
	}

	public Long getDocId() {
		return this.docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

}