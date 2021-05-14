package jp.co.nci.iwf.jpa.entity.ex;

import java.sql.Timestamp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class MwvScreen extends BaseJpaEntity {
	@Column(name="CONTAINER_ID")
	public Long containerId;

	@Column(name="CONTAINER_NAME")
	public String containerName;

	@Column(name="CONTAINER_CODE")
	public String containerCode;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="CORPORATION_NAME")
	public String corporationName;

	@Id
	@Column(name="SCREEN_ID")
	public long screenId;

	@Column(name="SCREEN_CODE")
	public String screenCode;

	@Column(name="SCREEN_NAME")
	public String screenName;

	@Column(name="SCRATCH_FLAG")
	public String scratchFlag;

	@Column(name="SCRATCH_FLAG_NAME")
	public String scratchFlagName;

	@Column(name="SCREEN_CUSTOM_CLASS")
	public String screenCustomClass;

	@Column(name="SUBMIT_FUNC_NAME")
	public String submitFuncName;

	@Column(name="SUBMIT_FUNC_PARAM")
	public String submitFuncParam;

	@Column(name="LOAD_FUNC_NAME")
	public String loadFuncName;

	@Column(name="LOAD_FUNC_PARAM")
	public String loadFuncParam;

	@Column(name="CHANGE_START_USER_FUNC_NAME")
	public String changeStartUserFuncName;

	@Column(name="CHANGE_START_USER_FUNC_PARAM")
	public String changeStartUserFuncParam;

	@Column(name="UPLOAD_DATETIME")
	public Timestamp uploadDatetime;

	@Column(name="version")
	public Long version;
}
