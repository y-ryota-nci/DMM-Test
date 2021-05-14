package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PAYMATHYS_INF database table.
 * 
 */
@Embeddable
public class PaymathysInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PAYHYS_NO")
	private String payhysNo;

	@Column(name="PAY_DTL_NO")
	private long payDtlNo;

	@Column(name="PAYMAT_NO")
	private long paymatNo;

	public PaymathysInfPK() {
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
	public long getPayDtlNo() {
		return this.payDtlNo;
	}
	public void setPayDtlNo(long payDtlNo) {
		this.payDtlNo = payDtlNo;
	}
	public long getPaymatNo() {
		return this.paymatNo;
	}
	public void setPaymatNo(long paymatNo) {
		this.paymatNo = paymatNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PaymathysInfPK)) {
			return false;
		}
		PaymathysInfPK castOther = (PaymathysInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.payhysNo.equals(castOther.payhysNo)
			&& (this.payDtlNo == castOther.payDtlNo)
			&& (this.paymatNo == castOther.paymatNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.payhysNo.hashCode();
		hash = hash * prime + ((int) (this.payDtlNo ^ (this.payDtlNo >>> 32)));
		hash = hash * prime + ((int) (this.paymatNo ^ (this.paymatNo >>> 32)));
		
		return hash;
	}
}