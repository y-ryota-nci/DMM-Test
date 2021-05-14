package jp.co.nci.iwf.jpa.entity.wm;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the WFM_CORPORATION_PROPERTY database table.
 * 
 */
@Embeddable
public class WfmCorporationPropertyPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="PROPERTY_CODE")
	private String propertyCode;

	public WfmCorporationPropertyPK() {
	}
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof WfmCorporationPropertyPK)) {
			return false;
		}
		WfmCorporationPropertyPK castOther = (WfmCorporationPropertyPK)other;
		return 
			this.corporationCode.equals(castOther.corporationCode)
			&& this.propertyCode.equals(castOther.propertyCode);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corporationCode.hashCode();
		hash = hash * prime + this.propertyCode.hashCode();
		
		return hash;
	}
}