package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_ATTACH_FILE_DOC database table.
 *
 */
@Entity
@Table(name="MWT_ATTACH_FILE_DOC")
@NamedQuery(name="MwtAttachFileDoc.findAll", query="SELECT m FROM MwtAttachFileDoc m")
public class MwtAttachFileDoc extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ATTACH_FILE_DOC_ID")
	private long attachFileDocId;

	private String comments;

	@Column(name="DOC_FILE_DATA_ID")
	private Long docFileDataId;

	@Column(name="DOC_ID")
	private Long docId;

	public MwtAttachFileDoc() {
	}

	public long getAttachFileDocId() {
		return this.attachFileDocId;
	}

	public void setAttachFileDocId(long attachFileDocId) {
		this.attachFileDocId = attachFileDocId;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getDocFileDataId() {
		return this.docFileDataId;
	}

	public void setDocFileDataId(Long docFileDataId) {
		this.docFileDataId = docFileDataId;
	}

	public Long getDocId() {
		return this.docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

}