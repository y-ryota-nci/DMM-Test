package jp.co.nci.iwf.designer.service;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class ScreenProcessInfo extends BaseJpaEntity {

	@Id
	@Column(name="SCREEN_PROCESS_ID")
	public long screenProcessId;

	@Column(name="SCREEN_PROCESS_CODE")
	public String screenProcessCode;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="SCREEN_ID")
	public Long screenId;

	@Column(name="SCREEN_CODE")
	public String screenCode;

	@Column(name="SCREEN_NAME")
	public String screenName;

	@Column(name="SCREEN_PROCESS_NAME")
	public String screenProcessName;

	@Column(name="SCRATCH_FLAG")
	public String scratchFlag;

	@Column(name="SCREEN_CUSTOM_CLASS")
	public String screenCustomClass;

	@Column(name="CONTAINER_ID")
	public Long containerId;

	@Column(name="LOAD_FUNC_NAME")
	public String loadFuncName;

	@Column(name="LOAD_FUNC_PARAM")
	public String loadFuncParam;

}
