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
 * The persistent class for the MWM_ACCESSIBLE_SCREEN database table.
 *
 */
@Entity
@Table(name="MWM_ACCESSIBLE_SCREEN", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "MENU_ROLE_CODE", "SCREEN_PROCESS_ID"}))
@NamedQuery(name="MwmAccessibleScreen.findAll", query="SELECT m FROM MwmAccessibleScreen m")
public class MwmAccessibleScreen extends MwmBaseJpaEntity  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ACCESSIBLE_SCREEN_ID")
	private long accessibleScreenId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="MENU_ROLE_CODE")
	private String menuRoleCode;

	@Column(name="NEW_APPLY_DISPLAY_FLAG")
	private String newApplyDisplayFlag;

	@Column(name="SCREEN_PROCESS_ID")
	private Long screenProcessId;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	private Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	private Date validStartDate;

	public MwmAccessibleScreen() {
	}

	public long getAccessibleScreenId() {
		return this.accessibleScreenId;
	}

	public void setAccessibleScreenId(long accessibleScreenId) {
		this.accessibleScreenId = accessibleScreenId;
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

	public String getNewApplyDisplayFlag() {
		return this.newApplyDisplayFlag;
	}

	public void setNewApplyDisplayFlag(String newApplyDisplayFlag) {
		this.newApplyDisplayFlag = newApplyDisplayFlag;
	}

	public Long getScreenProcessId() {
		return this.screenProcessId;
	}

	public void setScreenProcessId(Long screenProcessId) {
		this.screenProcessId = screenProcessId;
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