package jp.co.nci.iwf.jpa.entity.ex;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class MwvScreenProcessDef extends BaseJpaEntity {

	@Id
	@Column(name="SCREEN_PROCESS_ID")
	public long screenProcessId;

	@Column(name="SCREEN_PROCESS_CODE")
	public String screenProcessCode;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	public String description;

	@Column(name="PROCESS_DEF_CODE")
	public String processDefCode;

	@Column(name="PROCESS_DEF_DETAIL_CODE")
	public String processDefDetailCode;

	@Column(name="PROCESS_DEF_DETAIL_NAME")
	public String processDefDetailName;

	@Column(name="SCREEN_ID")
	public Long screenId;

	@Column(name="SCREEN_CODE")
	public String screenCode;

	@Column(name="SCREEN_NAME")
	public String screenName;

	@Column(name="SCREEN_PROCESS_LEVEL_ID")
	public Long screenProcessLevelId;

	@Column(name="SCREEN_PROCESS_NAME")
	public String screenProcessName;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	public Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	public Date validStartDate;

	@Column(name="version")
	public Long version;
}
