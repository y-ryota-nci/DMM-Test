package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_SCREEN_PROCESS_LEVEL database table.
 *
 */
@Entity
@Table(name="MWM_SCREEN_PROCESS_LEVEL", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE","LEVEL_CODE"}))
@NamedQuery(name="MwmScreenProcessLevel.findAll", query="SELECT m FROM MwmScreenProcessLevel m")
public class MwmScreenProcessLevel extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_PROCESS_LEVEL_ID")
	private long screenProcessLevelId;

	@Column(name="EXPANSION_FLAG")
	private String expansionFlag;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="PARENT_LEVEL_CODE")
	private String parentLevelCode;

	@Column(name="LEVEL_CODE")
	private String levelCode;

	@Column(name="LEVEL_NAME")
	private String levelName;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmScreenProcessLevel() {
	}

	public long getScreenProcessLevelId() {
		return this.screenProcessLevelId;
	}

	public void setScreenProcessLevelId(long screenProcessLevelId) {
		this.screenProcessLevelId = screenProcessLevelId;
	}

	public String getExpansionFlag() {
		return this.expansionFlag;
	}

	public void setExpansionFlag(String expansionFlag) {
		this.expansionFlag = expansionFlag;
	}

	public String getCorporationCode() {
		return corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getParentLevelCode() {
		return parentLevelCode;
	}

	public void setParentLevelCode(String parentLevelCode) {
		this.parentLevelCode = parentLevelCode;
	}

	public String getLevelCode() {
		return levelCode;
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

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}