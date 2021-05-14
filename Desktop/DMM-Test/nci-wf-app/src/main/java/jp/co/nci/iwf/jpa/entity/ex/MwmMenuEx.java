package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;


/**
 * The persistent class for the MWM_MENU database table.
 *
 */
@Entity
public class MwmMenuEx extends MwmBaseJpaEntity  {
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

	@Column(name="URL")
	private String url;

	@Column(name="USER_NAME_CREATED")
	private String userNameCreated;

	@Column(name="EXIST")
	private String exist;

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

	public String getUserNameCreated() {
		return userNameCreated;
	}

	public void setUserNameCreated(String userNameCreated) {
		this.userNameCreated = userNameCreated;
	}

	public String getExist() {
		return exist;
	}

	public void setExist(String exist) {
		this.exist = exist;
	}

	private static final String NULL_CODE = "0000";
	private static final String SPACE = "■";

	public String getName(){
		if (NULL_CODE.equals(this.getMenuItemCode2())) {
			return getMenuName();
		} else if(NULL_CODE.equals(getMenuItemCode3())){
			return SPACE + getMenuName();
		}else {
			return SPACE + SPACE + getMenuName();
		}
	}
}