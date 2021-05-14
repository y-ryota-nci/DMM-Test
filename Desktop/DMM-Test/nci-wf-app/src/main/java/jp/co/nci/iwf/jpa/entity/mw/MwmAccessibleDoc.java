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
 * The persistent class for the MWM_ACCESSIBLE_DOC database table.
 *
 */
@Entity
@Table(name="MWM_ACCESSIBLE_DOC")
@NamedQuery(name="MwmAccessibleDoc.findAll", query="SELECT m FROM MwmAccessibleDoc m")
public class MwmAccessibleDoc extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ACCESSIBLE_DOC_ID")
	private long accessibleDocId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="MENU_ROLE_CODE")
	private String menuRoleCode;

	@Column(name="SCREEN_DOC_ID")
	private Long screenDocId;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	private Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	private Date validStartDate;

	public MwmAccessibleDoc() {
	}

	public long getAccessibleDocId() {
		return this.accessibleDocId;
	}

	public void setAccessibleDocId(long accessibleDocId) {
		this.accessibleDocId = accessibleDocId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getMenuRoleCode() {
		return this.menuRoleCode;
	}

	public void setMenuRoleCode(String menuRoleCode) {
		this.menuRoleCode = menuRoleCode;
	}

	public Long getScreenDocId() {
		return this.screenDocId;
	}

	public void setScreenDocId(Long screenDocId) {
		this.screenDocId = screenDocId;
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