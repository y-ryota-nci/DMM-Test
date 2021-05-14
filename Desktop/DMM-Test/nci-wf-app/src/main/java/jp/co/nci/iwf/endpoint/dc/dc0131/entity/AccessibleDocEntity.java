package jp.co.nci.iwf.endpoint.dc.dc0131.entity;

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
public class AccessibleDocEntity extends BaseJpaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ACCESSIBLE_DOC_ID")
	public long accessibleDocId;
	@Column(name="CORPORATION_CODE")
	public String corporationCode;
	@Column(name="MENU_ROLE_CODE")
	public String menuRoleCode;
	@Column(name="SCREEN_DOC_ID")
	public Long screenDocId;
	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	public Date validEndDate;
	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	public Date validStartDate;
	@Column(name="SCREEN_DOC_NAME")
	public String screenDocName;

}