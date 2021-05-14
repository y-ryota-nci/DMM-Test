package jp.co.nci.iwf.jpa.entity.mw;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_ACCESSIBLE_MENU database table.
 *
 */
@Entity
@Table(name="MWM_ACCESSIBLE_MENU", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "MENU_ROLE_CODE", "MENU_ID"}))
@NamedQuery(name="MwmAccessibleMenu.findAll", query="SELECT m FROM MwmAccessibleMenu m")
public class MwmAccessibleMenu extends MwmBaseJpaEntity  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ACCESSIBLE_MENU_ID")
	private long accessibleMenuId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="MENU_ID")
	private Long menuId;

	@Column(name="MENU_ROLE_CODE")
	private String menuRoleCode;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	private Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	private Date validStartDate;

	public MwmAccessibleMenu() {
	}

	public long getAccessibleMenuId() {
		return this.accessibleMenuId;
	}

	public void setAccessibleMenuId(long accessibleMenuId) {
		this.accessibleMenuId = accessibleMenuId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuRoleCode() {
		return this.menuRoleCode;
	}

	public void setMenuRoleCode(String menuRoleCode) {
		this.menuRoleCode = menuRoleCode;
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