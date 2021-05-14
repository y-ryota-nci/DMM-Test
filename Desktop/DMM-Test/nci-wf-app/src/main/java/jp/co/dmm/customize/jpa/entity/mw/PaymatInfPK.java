package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PAYMAT_INF database table.
 * 
 */
@Embeddable
public class PaymatInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PAY_NO")
	private String payNo;

	@Column(name="PAY_DTL_NO")
	private long payDtlNo;

	@Column(name="PAYMAT_NO")
	private long paymatNo;

	public PaymatInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getPayNo() {
		return this.payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
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
		if (!(other instanceof PaymatInfPK)) {
			return false;
		}
		PaymatInfPK castOther = (PaymatInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.payNo.equals(castOther.payNo)
			&& (this.payDtlNo == castOther.payDtlNo)
			&& (this.paymatNo == castOther.paymatNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.payNo.hashCode();
		hash = hash * prime + ((int) (this.payDtlNo ^ (this.payDtlNo >>> 32)));
		hash = hash * prime + ((int) (this.paymatNo ^ (this.paymatNo >>> 32)));
		
		return hash;
	}
}