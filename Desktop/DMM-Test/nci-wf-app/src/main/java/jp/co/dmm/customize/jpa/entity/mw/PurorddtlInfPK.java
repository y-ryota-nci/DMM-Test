package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PURORDDTL_INF database table.
 * 
 */
@Embeddable
public class PurorddtlInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PURORD_NO")
	private String purordNo;

	@Column(name="PURORD_DTL_NO")
	private long purordDtlNo;

	public PurorddtlInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getPurordNo() {
		return this.purordNo;
	}
	public void setPurordNo(String purordNo) {
		this.purordNo = purordNo;
	}
	public long getPurordDtlNo() {
		return this.purordDtlNo;
	}
	public void setPurordDtlNo(long purordDtlNo) {
		this.purordDtlNo = purordDtlNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PurorddtlInfPK)) {
			return false;
		}
		PurorddtlInfPK castOther = (PurorddtlInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.purordNo.equals(castOther.purordNo)
			&& (this.purordDtlNo == castOther.purordDtlNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.purordNo.hashCode();
		hash = hash * prime + ((int) (this.purordDtlNo ^ (this.purordDtlNo >>> 32)));
		
		return hash;
	}
}