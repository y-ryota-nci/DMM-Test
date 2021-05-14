package jp.co.nci.iwf.jpa.entity.ex;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;


/**
 * The persistent class for the MWM_SCREEN_DOC_DEF database table.
 *
 */
@Entity
public class MwvScreenDocDef extends BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_DOC_ID")
	public long screenDocId;

	@Column(name="SCREEN_DOC_CODE")
	public String screenDocCode;

	@Column(name="SCREEN_DOC_NAME")
	public String screenDocName;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	public String description;

	@Column(name="SCREEN_ID")
	public Long screenId;

	@Column(name="SCREEN_CODE")
	public String screenCode;

	@Column(name="SCREEN_NAME")
	public String screenName;

	@Column(name="SCREEN_DOC_LEVEL_ID")
	public Long screenDocLevelId;

	@Column(name="SCREEN_PROCESS_CODE")
	public String screenProcessCode;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Column(name="DOC_FOLDER_ID")
	public Long docFolderId;

	@Column(name="FOLDER_CODE")
	public String folderCode;

	@Column(name="FOLDER_PATH")
	public String folderPath;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	public Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	public Date validStartDate;

	@Column(name="version")
	public Long version;

}