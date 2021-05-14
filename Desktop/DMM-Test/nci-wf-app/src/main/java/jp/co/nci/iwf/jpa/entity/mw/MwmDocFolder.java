package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the MWM_DOC_FOLDER database table.
 *
 */
@Entity
@Table(name="MWM_DOC_FOLDER")
@NamedQuery(name="MwmDocFolder.findAll", query="SELECT m FROM MwmDocFolder m")
public class MwmDocFolder extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_FOLDER_ID")
	private long docFolderId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="FOLDER_CODE")
	private String folderCode;

	@Column(name="FOLDER_NAME")
	private String folderName;

	@Column(name="META_TEMPLATE_ID")
	private Long metaTemplateId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	private Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	private Date validStartDate;

	public MwmDocFolder() {
	}

	public long getDocFolderId() {
		return this.docFolderId;
	}

	public void setDocFolderId(long docFolderId) {
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

}