package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWT_DOC_FOLDER_RELATION_INFO database table.
 *
 */
@Entity
@Table(name="MWT_DOC_FOLDER_RELATION_INFO")
@NamedQuery(name="MwtDocFolderRelationInfo.findAll", query="SELECT m FROM MwtDocFolderRelationInfo m")
public class MwtDocFolderRelationInfo extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_FOLDER_RELATION_ID")
	private long docFolderRelationId;

	@Column(name="DOC_FOLDER_ID")
	private Long docFolderId;

	@Column(name="DOC_ID")
	private Long docId;

	public MwtDocFolderRelationInfo() {
	}

	public long getDocFolderRelationId() {
		return this.docFolderRelationId;
	}

	public void setDocFolderRelationId(long docFolderRelationId) {
		this.docFolderRelationId = docFolderRelationId;
	}

	public Long getDocFolderId() {
		return this.docFolderId;
	}

	public void setDocFolderId(Long docFolderId) {
		this.docFolderId = docFolderId;
	}

	public Long getDocId() {
		return this.docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

}