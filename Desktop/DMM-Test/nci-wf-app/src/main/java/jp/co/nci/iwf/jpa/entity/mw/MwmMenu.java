package jp.co.nci.iwf.jpa.entity.mw;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_MENU database table.
 *
 */
@Entity
@Table(name="MWM_MENU", uniqueConstraints=@UniqueConstraint(columnNames={"MENU_ITEM_CODE1", "MENU_ITEM_CODE2", "MENU_ITEM_CODE3"}))
@NamedQuery(name="MwmMenu.findAll", query="SELECT m FROM MwmMenu m")
public class MwmMenu extends MwmBaseJpaEntity  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MENU_ID")
	private long menuId;

	@Column(name="MENU_ITEM_CODE1")
	private String menuItemCode1;

	@Column(name="MENU_ITEM_CODE2")
	private String menuItemCode2;

	@Column(name="MENU_ITEM_CODE3")
	private String menuItemCode3;

	@Column(name="MENU_NAME")
	private String menuName;

	private String url;


	public long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	public String getMenuItemCode1() {
		return this.menuItemCode1;
	}

	public void setMenuItemCode1(String menuItemCode1) {
		this.menuItemCode1 = menuItemCode1;
	}

	public String getMenuItemCode2() {
		return this.menuItemCode2;
	}

	public void setMenuItemCode2(String menuItemCode2) {
		this.menuItemCode2 = menuItemCode2;
	}

	public String getMenuItemCode3() {
		return this.menuItemCode3;
	}

	public void setMenuItemCode3(String menuItemCode3) {
		this.menuItemCode3 = menuItemCode3;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}