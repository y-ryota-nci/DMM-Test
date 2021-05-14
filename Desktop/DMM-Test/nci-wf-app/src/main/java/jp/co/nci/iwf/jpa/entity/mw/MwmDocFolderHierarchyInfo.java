package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWM_DOC_FOLDER_HIERARCHY_INFO database table.
 *
 */
@Entity
@Table(name="MWM_DOC_FOLDER_HIERARCHY_INFO")
@NamedQuery(name="MwmDocFolderHierarchyInfo.findAll", query="SELECT m FROM MwmDocFolderHierarchyInfo m")
public class MwmDocFolderHierarchyInfo extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_FOLDER_HIERARCHY_ID")
	private long docFolderHierarchyId;

	@Column(name="DOC_FOLDER_ID")
	private Long docFolderId;

	@Column(name="PARENT_DOC_FOLDER_ID")
	private Long parentDocFolderId;

	public MwmDocFolderHierarchyInfo() {
	}

	public long getDocFolderHierarchyId() {
		return this.docFolderHierarchyId;
	}

	public void setDocFolderHierarchyId(long docFolderHierarchyId) {
		this.docFolderHierarchyId = docFolderHierarchyId;
	}

	public Long getDocFolderId() {
		return this.docFolderId;
	}

	public void setDocFolderId(Long docFolderId) {
		this.docFolderId = docFolderId;
	}

	public Long getParentDocFolderId() {
		return this.parentDocFolderId;
	}

	public void setParentDocFolderId(Long parentDocFolderId) {
		this.parentDocFolderId = parentDocFolderId;
	}

}