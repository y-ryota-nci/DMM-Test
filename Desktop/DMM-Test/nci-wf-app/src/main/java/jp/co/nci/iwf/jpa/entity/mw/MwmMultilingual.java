package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_MULTILINGUAL database table.
 *
 */
@Entity
@Table(name="MWM_MULTILINGUAL", uniqueConstraints=@UniqueConstraint(columnNames={"TABLE_NAME", "COLUMN_NAME", "ID", "LOCALE_CODE"}))
@NamedQuery(name="MwmMultilingual.findAll", query="SELECT m FROM MwmMultilingual m")
public class MwmMultilingual extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MULTILINGUAL_ID")
	private long multilingualId;

	@Column(name="COLUMN_NAME")
	private String columnName;

	private Long id;

	@Column(name="LOCALE_CODE")
	private String localeCode;

	@Column(name="TABLE_NAME")
	private String tableName;

	private String val;

	public MwmMultilingual() {
	}

	public long getMultilingualId() {
		return this.multilingualId;
	}

	public void setMultilingualId(long multilingualId) {
		this.multilingualId = multilingualId;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocaleCode() {
		return this.localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getVal() {
		return this.val;
	}

	public void setVal(String val) {
		this.val = val;
	}

}