package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the BGT_PLN database table.
 * 
 */
@Embeddable
public class BgtPlnPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="YR_CD")
	private String yrCd;

	@Column(name="ORGANIZATION_CODE")
	private String organizationCode;

	@Column(name="BGT_ITM_CD")
	private String bgtItmCd;

	@Column(name="RCVINSP_PAY_TP")
	private String rcvinspPayTp;

	public BgtPlnPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getYrCd() {
		return this.yrCd;
	}
	public void setYrCd(String yrCd) {
		this.yrCd = yrCd;
	}
	public String getOrganizationCode() {
		return this.organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	public String getBgtItmCd() {
		return this.bgtItmCd;
	}
	public void setBgtItmCd(String bgtItmCd) {
		this.bgtItmCd = bgtItmCd;
	}
	public String getRcvinspPayTp() {
		return this.rcvinspPayTp;
	}
	public void setRcvinspPayTp(String rcvinspPayTp) {
		this.rcvinspPayTp = rcvinspPayTp;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BgtPlnPK)) {
			return false;
		}
		BgtPlnPK castOther = (BgtPlnPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.yrCd.equals(castOther.yrCd)
			&& this.organizationCode.equals(castOther.organizationCode)
			&& this.bgtItmCd.equals(castOther.bgtItmCd)
			&& this.rcvinspPayTp.equals(castOther.rcvinspPayTp);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.yrCd.hashCode();
		hash = hash * prime + this.organizationCode.hashCode();
		hash = hash * prime + this.bgtItmCd.hashCode();
		hash = hash * prime + this.rcvinspPayTp.hashCode();
		
		return hash;
	}
}