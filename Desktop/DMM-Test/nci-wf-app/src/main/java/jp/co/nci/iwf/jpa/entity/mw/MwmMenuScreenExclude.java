package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWM_MENU_SCREEN_EXCLUDE database table.
 *
 */
@Entity
@Table(name="MWM_MENU_SCREEN_EXCLUDE")
@NamedQuery(name="MwmMenuScreenExclude.findAll", query="SELECT m FROM MwmMenuScreenExclude m")
public class MwmMenuScreenExclude extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MENU_SCREEN_EXCLUDE_ID")
	private long menuScreenExcludeId;

	@Column(name="EXCLUDE_PATTERN")
	private String excludePattern;

	public MwmMenuScreenExclude() {
	}

	public long getMenuScreenExcludeId() {
		return this.menuScreenExcludeId;
	}

	public void setMenuScreenExcludeId(long menuScreenExcludeId) {
		this.menuScreenExcludeId = menuScreenExcludeId;
	}

	public String getExcludePattern() {
		return this.excludePattern;
	}

	public void setExcludePattern(String excludePattern) {
		this.excludePattern = excludePattern;
	}

}