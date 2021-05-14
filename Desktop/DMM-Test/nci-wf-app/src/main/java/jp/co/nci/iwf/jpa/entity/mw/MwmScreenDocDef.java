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
 * The persistent class for the MWM_SCREEN_DOC_DEF database table.
 *
 */
@Entity
@Table(name="MWM_SCREEN_DOC_DEF")
@NamedQuery(name="MwmScreenDocDef.findAll", query="SELECT m FROM MwmScreenDocDef m")
public class MwmScreenDocDef extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_DOC_ID")
	private long screenDocId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	private String description;

	@Column(name="FOLDER_CODE")
	private String folderCode;

	@Column(name="SCREEN_DOC_CODE")
	private String screenDocCode;

	@Column(name="SCREEN_DOC_LEVEL_ID")
	private Long screenDocLevelId;

	@Column(name="SCREEN_DOC_NAME")
	private String screenDocName;

	@Column(name="SCREEN_ID")
	private Long screenId;

	@Column(name="SCREEN_PROCESS_CODE")
	private String screenProcessCode;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	private Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	private Date validStartDate;

	public MwmScreenDocDef() {
	}

	public long getScreenDocId() {
		return this.screenDocId;
	}

	public void setScreenDocId(long screenDocId) {
		this.screenDocId = screenDocId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFolderCode() {
		return this.folderCode;
	}

	public void setFolderCode(String folderCode) {
		this.folderCode = folderCode;
	}

	public String getScreenDocCode() {
		return this.screenDocCode;
	}

	public void setScreenDocCode(String screenDocCode) {
		this.screenDocCode = screenDocCode;
	}

	public Long getScreenDocLevelId() {
		return this.screenDocLevelId;
	}

	public void setScreenDocLevelId(Long screenDocLevelId) {
		this.screenDocLevelId = screenDocLevelId;
	}

	public String getScreenDocName() {
		return this.screenDocName;
	}

	public void setScreenDocName(String screenDocName) {
		this.screenDocName = screenDocName;
	}

	public Long getScreenId() {
		return this.screenId;
	}

	public void setScreenId(Long screenId) {
		this.screenId = screenId;
	}

	public String getScreenProcessCode() {
		return this.screenProcessCode;
	}

	public void setScreenProcessCode(String screenProcessCode) {
		this.screenProcessCode = screenProcessCode;
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