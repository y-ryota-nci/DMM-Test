package jp.co.nci.iwf.jpa.entity.ex;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * The persistent class for the MWV_DOC_FOLDER database table.
 *
 */
@Entity
public class MwvDocFolder extends BaseJpaEntity {

	@Id
	@Column(name="DOC_FOLDER_ID")
	private Long docFolderId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="FOLDER_CODE")
	private String folderCode;

	@Column(name="FOLDER_NAME")
	private String folderName;

	@Column(name="LEVEL_DEPTH")
	private Integer levelDepth;

	@Column(name="META_TEMPLATE_ID")
	private Long metaTemplateId;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	private Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	private Date validStartDate;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="version")
	private Long version;

	@Column(name="DOC_FOLDER_HIERARCHY_ID")
	private long docFolderHierarchyId;

	@Column(name="PARENT_DOC_FOLDER_ID")
	private long parentDocFolderId;

	@Column(name="FOLDER_PATH")
	private String folderPath;

	@Column(name="DELETE_FLAG")
	private String deleteFlag;

	public Long getDocFolderId() {
		return this.docFolderId;
	}

	public void setDocFolderId(Long docFolderId) {
		this.docFolderId = docFolderId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getFolderCode() {
		return this.folderCode;
	}

	public void setFolderCode(String folderCode) {
		this.folderCode = folderCode;
	}

	public String getFolderName() {
		return this.folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public Integer getLevelDepth() {
		return levelDepth;
	}

	public void setLevelDepth(Integer levelDepth) {
		this.levelDepth = levelDepth;
	}

	public Long getMetaTemplateId() {
		return this.metaTemplateId;
	}

	public void setMetaTemplateId(Long metaTemplateId) {
		this.metaTemplateId = metaTemplateId;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Date getValidEndDate() {
		return this.validEndDate;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	public Date getValidStartDate() {
		return this.validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

	public long getDocFolderHierarchyId() {
		return this.docFolderHierarchyId;
	}

	public void setDocFolderHierarchyId(long docFolderHierarchyId) {
		this.docFolderHierarchyId = docFolderHierarchyId;
	}

	public long getParentDocFolderId() {
		return parentDocFolderId;
	}

	public void setParentDocFolderId(long parentDocFolderId) {
		this.parentDocFolderId = parentDocFolderId;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public final String getDeleteFlag() {
		return this.deleteFlag;
	}

	public final void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
}
