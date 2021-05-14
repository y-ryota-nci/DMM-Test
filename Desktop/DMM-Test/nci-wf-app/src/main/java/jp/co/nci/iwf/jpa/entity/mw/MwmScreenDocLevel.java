package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MWM_SCREEN_DOC_LEVEL database table.
 *
 */
@Entity
@Table(name="MWM_SCREEN_DOC_LEVEL")
@NamedQuery(name="MwmScreenDocLevel.findAll", query="SELECT m FROM MwmScreenDocLevel m")
public class MwmScreenDocLevel extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_DOC_LEVEL_ID")
	private long screenDocLevelId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="EXPANSION_FLAG")
	private String expansionFlag;

	@Column(name="LEVEL_CODE")
	private String levelCode;

	@Column(name="LEVEL_NAME")
	private String levelName;

	@Column(name="PARENT_LEVEL_CODE")
	private String parentLevelCode;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmScreenDocLevel() {
	}

	public long getScreenDocLevelId() {
		return this.screenDocLevelId;
	}

	public void setScreenDocLevelId(long screenDocLevelId) {
		this.screenDocLevelId = screenDocLevelId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getExpansionFlag() {
		return this.expansionFlag;
	}

	public void setExpansionFlag(String expansionFlag) {
		this.expansionFlag = expansionFlag;
	}

	public String getLevelCode() {
		return this.levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public String getLevelName() {
		return this.levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getParentLevelCode() {
		return this.parentLevelCode;
	}

	public void setParentLevelCode(String parentLevelCode) {
		this.parentLevelCode = parentLevelCode;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}