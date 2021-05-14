package jp.co.nci.iwf.jpa.entity.mw;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_SCREEN database table.
 *
 */
@Entity
@Table(name="MWM_SCREEN", uniqueConstraints=@UniqueConstraint(columnNames={"SCREEN_CODE", "CORPORATION_CODE"}))
@NamedQuery(name="MwmScreen.findAll", query="SELECT m FROM MwmScreen m")
public class MwmScreen extends MwmBaseJpaEntity  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_ID")
	private long screenId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="CONTAINER_ID")
	private Long containerId;

	@Column(name="SCRATCH_FLAG")
	private String scratchFlag;

	@Column(name="SCREEN_CODE")
	private String screenCode;

	@Column(name="SCREEN_CUSTOM_CLASS")
	private String screenCustomClass;

	@Column(name="SCREEN_NAME")
	private String screenName;

	@Column(name="SUBMIT_FUNC_NAME")
	private String submitFuncName;

	@Column(name="SUBMIT_FUNC_PARAM")
	private String submitFuncParam;

	@Column(name="LOAD_FUNC_NAME")
	private String loadFuncName;

	@Column(name="LOAD_FUNC_PARAM")
	private String loadFuncParam;

	@Column(name="CHANGE_START_USER_FUNC_NAME")
	private String changeStartUserFuncName;

	@Column(name="CHANGE_START_USER_FUNC_PARAM")
	private String changeStartUserFuncParam;

	@Column(name="UPLOAD_DATETIME")
	private Timestamp uploadDatetime;

	public long getScreenId() {
		return this.screenId;
	}

	public void setScreenId(long screenId) {
		this.screenId = screenId;
	}

	public String getCorporationCode() {
		return corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Long getContainerId() {
		return this.containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public String getScratchFlag() {
		return this.scratchFlag;
	}

	public void setScratchFlag(String scratchFlag) {
		this.scratchFlag = scratchFlag;
	}

	public String getScreenCode() {
		return this.screenCode;
	}

	public void setScreenCode(String screenCode) {
		this.screenCode = screenCode;
	}

	public String getScreenCustomClass() {
		return screenCustomClass;
	}

	public void setScreenCustomClass(String screenCustomClass) {
		this.screenCustomClass = screenCustomClass;
	}

	public String getScreenName() {
		return this.screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getSubmitFuncName() {
		return submitFuncName;
	}

	public void setSubmitFuncName(String submitFuncName) {
		this.submitFuncName = submitFuncName;
	}

	public String getSubmitFuncParam() {
		return submitFuncParam;
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

	public Timestamp getUploadDatetime() {
		return uploadDatetime;
	}

	public void setUploadDatetime(Timestamp uploadDatetime) {
		this.uploadDatetime = uploadDatetime;
	}
}