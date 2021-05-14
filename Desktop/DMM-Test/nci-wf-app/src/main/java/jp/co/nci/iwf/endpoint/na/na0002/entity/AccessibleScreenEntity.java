package jp.co.nci.iwf.endpoint.na.na0002.entity;

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
public class AccessibleScreenEntity extends BaseJpaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ACCESSIBLE_SCREEN_ID")
	public long accessibleScreenId;
	@Column(name="CORPORATION_CODE")
	public String corporationCode;
	@Column(name="MENU_ROLE_CODE")
	public String menuRoleCode;
	@Column(name="NEW_APPLY_DISPLAY_FLAG")
	public String newApplyDisplayFlag;
	@Column(name="SCREEN_PROCESS_ID")
	public Long screenProcessId;
	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	public Date validEndDate;
	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	public Date validStartDate;
	@Column(name="SCREEN_PROCESS_NAME")
	public String screenProcessName;

}