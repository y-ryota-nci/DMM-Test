package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_MENU_SCREEN_PROCESS database table.
 *
 */
@Entity
@Table(name="MWM_SCREEN_PROCESS_MENU", uniqueConstraints=@UniqueConstraint(columnNames={"SCREEN_PROCESS_ID", "MENU_ID"}))
@NamedQuery(name="MwmScreenProcessMenu.findAll", query="SELECT m FROM MwmScreenProcessMenu m")
public class MwmScreenProcessMenu extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_PROCESS_MENU_ID")
	private long screenProcessMenuId;

	@Column(name="MENU_ID")
	private Long menuId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="SCREEN_PROCESS_ID")
	private Long screenProcessId;

	public MwmScreenProcessMenu() {
	}

	public long getScreenProcessMenuId() {
		return this.screenProcessMenuId;
	}

	public void setScreenProcessMenuId(long screenProcessMenuId) {
		this.screenProcessMenuId = screenProcessMenuId;
	}

	public Long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getScreenProcessId() {
		return this.screenProcessId;
	}

	public void setScreenProcessId(Long screenProcessId) {
		this.screenProcessId = screenProcessId;
	}

	public String getCorporationCode() {
		return corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

}