package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_CONTAINER database table.
 *
 */
@Entity
@Table(name="MWM_CONTAINER", uniqueConstraints=@UniqueConstraint(columnNames={"CONTAINER_CODE", "CORPORATION_CODE"}))
@NamedQuery(name="MwmContainer.findAll", query="SELECT m FROM MwmContainer m")
public class MwmContainer extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CONTAINER_ID")
	private long containerId;

	@Lob
	@Column(name="BG_HTML")
	private String bgHtml;

	@Column(name="CONTAINER_CODE")
	private String containerCode;

	@Column(name="CONTAINER_NAME")
	private String containerName;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Lob
	@Column(name="CUSTOM_CSS_STYLE")
	private String customCssStyle;

	@Column(name="FONT_SIZE")
	private Integer fontSize;

	@Column(name="PARTS_CODE_SEQ")
	private Integer partsCodeSeq;

	@Column(name="SUBMIT_FUNC_NAME")
	private String submitFuncName;

	@Column(name="SUBMIT_FUNC_PARAM")
	private String submitFuncParam;

	@Column(name="LOAD_FUNC_NAME")
	private String loadFuncName;

	@Column(name="LOAD_FUNC_PARAM")
	private String loadFuncParam;

	@Column(name="TABLE_MODIFIED_TIMESTAMP")
	private Timestamp tableModifiedTimestamp;

	@Column(name="TABLE_NAME")
	private String tableName;

	@Column(name="TABLE_SYNC_TIMESTAMP")
	private Timestamp tableSyncTimestamp;

	@Column(name="CHANGE_START_USER_FUNC_NAME")
	private String changeStartUserFuncName;

	@Column(name="CHANGE_START_USER_FUNC_PARAM")
	private String changeStartUserFuncParam;

	@Column(name="NOT_DROP_TABLE_FLAG")
	private String notDropTableFlag;

	public MwmContainer() {
	}

	public long getContainerId() {
		return this.containerId;
	}

	public void setContainerId(long containerId) {
		this.containerId = containerId;
	}

	public String getBgHtml() {
		return this.bgHtml;
	}

	public void setBgHtml(String bgHtml) {
		this.bgHtml = bgHtml;
	}

	public String getContainerCode() {
		return this.containerCode;
	}

	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public String getContainerName() {
		return this.containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getCustomCssStyle() {
		return this.customCssStyle;
	}

	public void setCustomCssStyle(String customCssStyle) {
		this.customCssStyle = customCssStyle;
	}

	public Integer getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public Integer getPartsCodeSeq() {
		return this.partsCodeSeq;
	}

	public void setPartsCodeSeq(Integer partsCodeSeq) {
		this.partsCodeSeq = partsCodeSeq;
	}

	public String getSubmitFuncName() {
		return this.submitFuncName;
	}

	public void setSubmitFuncName(String submitFuncName) {
		this.submitFuncName = submitFuncName;
	}

	public String getSubmitFuncParam() {
		return this.submitFuncParam;
	}

	public void setSubmitFuncParam(String submitFuncParam) {
		this.submitFuncParam = submitFuncParam;
	}

	public String getLoadFuncName() {
		return loadFuncName;
	}

	public void setLoadFuncName(String loadFuncName) {
		this.loadFuncName = loadFuncName;
	}

	public String getLoadFuncParam() {
		return loadFuncParam;
	}

	public void setLoadFuncParam(String loadFuncParam) {
		this.loadFuncParam = loadFuncParam;
	}

	public Timestamp getTableModifiedTimestamp() {
		return this.tableModifiedTimestamp;
	}

	public void setTableModifiedTimestamp(Timestamp tableModifiedTimestamp) {
		this.tableModifiedTimestamp = tableModifiedTimestamp;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Timestamp getTableSyncTimestamp() {
		return this.tableSyncTimestamp;
	}

	public void setTableSyncTimestamp(Timestamp tableSyncTimestamp) {
		this.tableSyncTimestamp = tableSyncTimestamp;
	}

	public String getChangeStartUserFuncName() {
		return changeStartUserFuncName;
	}

	public void setChangeStartUserFuncName(String changeStartUserFuncName) {
		this.changeStartUserFuncName = changeStartUserFuncName;
	}

	public String getChangeStartUserFuncParam() {
		return changeStartUserFuncParam;
	}

	public void setChangeStartUserFuncParam(String changeStartUserFuncParam) {
		this.changeStartUserFuncParam = changeStartUserFuncParam;
	}

	public String getNotDropTableFlag() {
		return notDropTableFlag;
	}

	public void setNotDropTableFlag(String notDropTableFlag) {
		this.notDropTableFlag = notDropTableFlag;
	}

}