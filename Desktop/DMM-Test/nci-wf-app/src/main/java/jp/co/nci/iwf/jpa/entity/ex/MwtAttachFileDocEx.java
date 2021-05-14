package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;


/**
 * The persistent class for the MWT_ATTACH_FILE_DOC database table.
 *
 */
@Entity
public class MwtAttachFileDocEx extends MwmBaseJpaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ATTACH_FILE_DOC_ID")
	private long attachFileDocId;

	private String comments;

	@Column(name="DOC_FILE_DATA_ID")
	private Long docFileDataId;

	@Column(name="DOC_ID")
	private Long docId;

	@Column(name="FILE_NAME")
	public String fileName;

	public MwtAttachFileDocEx() {
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}