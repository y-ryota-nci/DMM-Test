package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_TABLE_SEARCH database table.
 * 
 */
@Entity
@Table(name="MWM_TABLE_SEARCH", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "TABLE_SEARCH_CODE", "TABLE_ID"}))
@NamedQuery(name="MwmTableSearch.findAll", query="SELECT m FROM MwmTableSearch m")
public class MwmTableSearch extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TABLE_SEARCH_ID")
	private long tableSearchId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DEFAULT_SEARCH_FLAG")
	private String defaultSearchFlag;

	@Column(name="DISPLAY_NAME")
	private String displayName;

	@Column(name="TABLE_ID")
	private Long tableId;

	@Column(name="TABLE_SEARCH_CODE")
	private String tableSearchCode;

	@Column(name="TABLE_SEARCH_NAME")
	private String tableSearchName;

	public MwmTableSearch() {
	}

	public long getTableSearchId() {
		return this.tableSearchId;
	}

	public void setTableSearchId(long tableSearchId) {
		this.tableSearchId = tableSearchId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getDefaultSearchFlag() {
		return this.defaultSearchFlag;
	}

	public void setDefaultSearchFlag(String defaultSearchFlag) {
		this.defaultSearchFlag = defaultSearchFlag;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Long getTableId() {
		return this.tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public String getTableSearchCode() {
		return this.tableSearchCode;
	}

	public void setTableSearchCode(String tableSearchCode) {
		this.tableSearchCode = tableSearchCode;
	}

	public String getTableSearchName() {
		return this.tableSearchName;
	}

	public void setTableSearchName(String tableSearchName) {
		this.tableSearchName = tableSearchName;
	}

}