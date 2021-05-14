package jp.co.nci.iwf.jpa.entity.wf;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the WFM_NAME_LOOKUP database table.
 * 
 */
@Embeddable
public class WfmNameLookupPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="TABLE_NAME")
	private String tableName;

	private long id;

	@Column(name="COLUMN_NAME")
	private String columnName;

	@Column(name="LOCALE_CODE")
	private String localeCode;

	public WfmNameLookupPK() {
	}
	public String getTableName() {
		return this.tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getColumnName() {
		return this.columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getLocaleCode() {
		return this.localeCode;
	}
	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof WfmNameLookupPK)) {
			return false;
		}
		WfmNameLookupPK castOther = (WfmNameLookupPK)other;
		return 
			this.tableName.equals(castOther.tableName)
			&& (this.id == castOther.id)
			&& this.columnName.equals(castOther.columnName)
			&& this.localeCode.equals(castOther.localeCode);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.tableName.hashCode();
		hash = hash * prime + ((int) (this.id ^ (this.id >>> 32)));
		hash = hash * prime + this.columnName.hashCode();
		hash = hash * prime + this.localeCode.hashCode();
		
		return hash;
	}
}