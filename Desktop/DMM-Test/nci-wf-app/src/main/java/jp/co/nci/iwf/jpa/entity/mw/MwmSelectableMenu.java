package jp.co.nci.iwf.jpa.entity.mw;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_SELECTABLE_MENU database table.
 *
 */
@Entity
@Table(name="MWM_SELECTABLE_MENU", uniqueConstraints=@UniqueConstraint(columnNames={"MENU_ROLE_TYPE", "MENU_ID"}))
@NamedQuery(name="MwmSelectableMenu.findAll", query="SELECT m FROM MwmSelectableMenu m")
public class MwmSelectableMenu extends MwmBaseJpaEntity  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SELECTABLE_MENU_ID")
	private long selectableMenuId;

	@Column(name="MENU_ID")
	private Long menuId;

	@Column(name="MENU_ROLE_TYPE")
	private String menuRoleType;


	public long getSelectableMenuId() {
		return this.selectableMenuId;
	}

	public void setSelectableMenuId(long selectableMenuId) {
		this.selectableMenuId = selectableMenuId;
	}

	public Long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuRoleType() {
		return this.menuRoleType;
	}

	public void setMenuRoleType(String menuRoleType) {
		this.menuRoleType = menuRoleType;
	}

}