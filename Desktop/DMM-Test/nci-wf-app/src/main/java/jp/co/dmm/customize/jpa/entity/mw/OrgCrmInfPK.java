package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ORG_CRM_INF database table.
 * 
 */
@Embeddable
public class OrgCrmInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="SPLR_CD")
	private String splrCd;

	private long sqno;

	private long brno;

	public OrgCrmInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getSplrCd() {
		return this.splrCd;
	}
	public void setSplrCd(String splrCd) {
		this.splrCd = splrCd;
	}
	public long getSqno() {
		return this.sqno;
	}
	public void setSqno(long sqno) {
		this.sqno = sqno;
	}
	public long getBrno() {
		return this.brno;
	}
	public void setBrno(long brno) {
		this.brno = brno;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof OrgCrmInfPK)) {
			return false;
		}
		OrgCrmInfPK castOther = (OrgCrmInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.splrCd.equals(castOther.splrCd)
			&& (this.sqno == castOther.sqno)
			&& (this.brno == castOther.brno);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.splrCd.hashCode();
		hash = hash * prime + ((int) (this.sqno ^ (this.sqno >>> 32)));
		hash = hash * prime + ((int) (this.brno ^ (this.brno >>> 32)));
		
		return hash;
	}
}