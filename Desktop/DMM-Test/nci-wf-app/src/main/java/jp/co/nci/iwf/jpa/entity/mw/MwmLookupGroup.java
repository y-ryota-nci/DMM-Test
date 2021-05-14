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
 * The persistent class for the MWM_LOOKUP_GROUP database table.
 *
 */
@Entity
@Table(name="MWM_LOOKUP_GROUP", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "LOOKUP_GROUP_ID", "LOCALE_CODE"}))
@NamedQuery(name="MwmLookupGroup.findAll", query="SELECT m FROM MwmLookupGroup m")
public class MwmLookupGroup extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_LOOKUP_GROUP_ID")
	private long screenLookupGroupId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="LOCALE_CODE")
	private String localeCode;

	@Column(name="LOOKUP_GROUP_ID")
	private String lookupGroupId;

	@Column(name="LOOKUP_GROUP_NAME")
	@NotTrim
	private String lookupGroupName;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmLookupGroup() {
	}

	public long getScreenLookupGroupId() {
		return this.screenLookupGroupId;
	}

	public void setScreenLookupGroupId(long screenLookupGroupId) {
		this.screenLookupGroupId = screenLookupGroupId;
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

	public String getLookupGroupName() {
		return this.lookupGroupName;
	}

	public void setLookupGroupName(String lookupGroupName) {
		this.lookupGroupName = lookupGroupName;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}