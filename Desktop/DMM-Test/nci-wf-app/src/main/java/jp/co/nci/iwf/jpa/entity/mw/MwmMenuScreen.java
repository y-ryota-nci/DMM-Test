package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWM_MENU_SCREEN database table.
 *
 */
@Entity
@Table(name="MWM_MENU_SCREEN")
@NamedQuery(name="MwmMenuScreen.findAll", query="SELECT m FROM MwmMenuScreen m")
public class MwmMenuScreen extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_MENU_ID")
	private long screenMenuId;

	@Column(name="MENU_ID")
	private Long menuId;

	@Column(name="SCREEN_ID")
	private String screenId;

	public MwmMenuScreen() {
	}

	public long getScreenMenuId() {
		return this.screenMenuId;
	}

	public void setScreenMenuId(long screenMenuId) {
		this.screenMenuId = screenMenuId;
	}

	public Long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getScreenId() {
		return this.screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

}