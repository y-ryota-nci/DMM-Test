package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import jp.co.nci.iwf.jpa.listener.NotTrim;


/**
 * The persistent class for the MWM_LOOKUP database table.
 *
 */
@Entity
@Table(name="MWM_LOOKUP", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE","LOOKUP_GROUP_ID","LOOKUP_ID","LOCALE_CODE"}))
@NamedQuery(name="MwmLookup.findAll", query="SELECT m FROM MwmLookup m")
public class MwmLookup extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_LOOKUP_ID")
	private long screenLookupId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="LOCALE_CODE")
	private String localeCode;

	@Column(name="LOOKUP_GROUP_ID")
	private String lookupGroupId;

	@Column(name="LOOKUP_ID")
	private String lookupId;

	@Column(name="LOOKUP_NAME")
	@NotTrim
	private String lookupName;

	@Column(name="LOOKUP_NAME2")
	@NotTrim
	private String lookupName2;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmLookup() {
	}

	public long getScreenLookupId() {
		return this.screenLookupId;
	}

	public void setScreenLookupId(long screenLookupId) {
		this.screenLookupId = screenLookupId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getLocaleCode() {
		return this.localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getLookupGroupId() {
		return this.lookupGroupId;
	}

	public void setLookupGroupId(String lookupGroupId) {
		this.lookupGroupId = lookupGroupId;
	}

	public String getLookupId() {
		return this.lookupId;
	}

	public void setLookupId(String lookupId) {
		this.lookupId = lookupId;
	}

	public String getLookupName() {
		return this.lookupName;
	}

	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}

	public String getLookupName2() {
		return this.lookupName2;
	}

	public void setLookupName2(String lookupName2) {
		this.lookupName2 = lookupName2;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}