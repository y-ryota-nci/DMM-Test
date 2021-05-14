package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the CM_ORG_MPG_MST database table.
 * 
 */
@Embeddable
public class CmOrgMpgMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="ORGANIZATION_CODE")
	private String organizationCode;

	public CmOrgMpgMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getOrganizationCode() {
		return this.organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CmOrgMpgMstPK)) {
			return false;
		}
		CmOrgMpgMstPK castOther = (CmOrgMpgMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.organizationCode.equals(castOther.organizationCode);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.organizationCode.hashCode();
		
		return hash;
	}
}