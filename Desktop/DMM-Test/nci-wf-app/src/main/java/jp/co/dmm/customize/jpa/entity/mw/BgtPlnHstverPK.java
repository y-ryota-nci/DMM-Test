package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the BGT_PLN_HSTVER database table.
 * 
 */
@Embeddable
public class BgtPlnHstverPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="YR_CD")
	private String yrCd;

	@Column(name="ORGANIZATION_CODE")
	private String organizationCode;

	@Column(name="RCVINSP_PAY_TP")
	private String rcvinspPayTp;

	@Column(name="HST_VERSION")
	private long hstVersion;

	public BgtPlnHstverPK() {
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
	public String getRcvinspPayTp() {
		return this.rcvinspPayTp;
	}
	public void setRcvinspPayTp(String rcvinspPayTp) {
		this.rcvinspPayTp = rcvinspPayTp;
	}
	public long getHstVersion() {
		return this.hstVersion;
	}
	public void setHstVersion(long hstVersion) {
		this.hstVersion = hstVersion;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BgtPlnHstverPK)) {
			return false;
		}
		BgtPlnHstverPK castOther = (BgtPlnHstverPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.yrCd.equals(castOther.yrCd)
			&& this.organizationCode.equals(castOther.organizationCode)
			&& this.rcvinspPayTp.equals(castOther.rcvinspPayTp)
			&& (this.hstVersion == castOther.hstVersion);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.yrCd.hashCode();
		hash = hash * prime + this.organizationCode.hashCode();
		hash = hash * prime + this.rcvinspPayTp.hashCode();
		hash = hash * prime + ((int) (this.hstVersion ^ (this.hstVersion >>> 32)));
		
		return hash;
	}
}