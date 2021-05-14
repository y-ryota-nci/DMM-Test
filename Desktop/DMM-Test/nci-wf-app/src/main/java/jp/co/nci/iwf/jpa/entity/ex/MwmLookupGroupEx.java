package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

/**
 * The persistent class for the MWM_LOOKUP_GROUP database table.
 *
 */
@Entity
public class MwmLookupGroupEx extends MwmBaseJpaEntity {

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
	private String lookupGroupName;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="DELETE_FLAG_NAME")
	private String deleteFlagName;

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