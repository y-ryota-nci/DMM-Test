package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PAYHYSLDTL_INF database table.
 * 
 */
@Embeddable
public class PayhysldtlInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PAYHYS_NO")
	private String payhysNo;

	@Column(name="PAYHYS_DTL_NO")
	private long payhysDtlNo;

	public PayhysldtlInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getPayhysNo() {
		return this.payhysNo;
	}
	public void setPayhysNo(String payhysNo) {
		this.payhysNo = payhysNo;
	}
	public long getPayhysDtlNo() {
		return this.payhysDtlNo;
	}
	public void setPayhysDtlNo(long payhysDtlNo) {
		this.payhysDtlNo = payhysDtlNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PayhysldtlInfPK)) {
			return false;
		}
		PayhysldtlInfPK castOther = (PayhysldtlInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.payhysNo.equals(castOther.payhysNo)
			&& (this.payhysDtlNo == castOther.payhysDtlNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.payhysNo.hashCode();
		hash = hash * prime + ((int) (this.payhysDtlNo ^ (this.payhysDtlNo >>> 32)));
		
		return hash;
	}
}