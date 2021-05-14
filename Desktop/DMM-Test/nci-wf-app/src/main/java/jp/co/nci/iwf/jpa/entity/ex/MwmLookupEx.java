package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

/**
 * The persistent class for the MWM_LOOKUP database table.
 *
 */
@Entity
public class MwmLookupEx extends MwmBaseJpaEntity {

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
	private String lookupName;

	@Column(name="LOOKUP_NAME2")
	private String lookupName2;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="LOOKUP_GROUP_NAME")
	private String lookupGroupName;

	@Column(name="DELETE_FLAG_NAME")
	private String deleteFlagName;

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

	/**
	 * @return lookupGroupName
	 */
	public String getLookupGroupName() {
		return lookupGroupName;
	}

	/**
	 * @param lookupGroupName セットする lookupGroupName
	 */
	public void setLookupGroupName(String lookupGroupName) {
		this.lookupGroupName = lookupGroupName;
	}

	/**
	 * @return deleteFlagName
	 */
	public String getDeleteFlagName() {
		return deleteFlagName;
	}

	/**
	 * @param deleteFlagName セットする deleteFlagName
	 */
	public void setDeleteFlagName(String deleteFlagName) {
		this.deleteFlagName = deleteFlagName;
	}
}