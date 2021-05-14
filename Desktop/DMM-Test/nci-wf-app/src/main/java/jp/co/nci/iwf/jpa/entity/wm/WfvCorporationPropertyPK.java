package jp.co.nci.iwf.jpa.entity.wm;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class WfvCorporationPropertyPK implements Serializable {
	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="PROPERTY_CODE")
	private String propertyCode;

	@Column(name="LOCALE_CODE")
	private String localeCode;

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getPropertyCode() {
		return this.propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WfvCorporationPropertyPK) {
			WfvCorporationPropertyPK o = (WfvCorporationPropertyPK)obj;
			return Objects.equals(corporationCode, o.getCorporationCode())
					&& Objects.equals(propertyCode, o.getPropertyCode())
					&& Objects.equals(localeCode, o.getLocaleCode());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return Objects.hash(corporationCode, propertyCode, localeCode);
	}
}
