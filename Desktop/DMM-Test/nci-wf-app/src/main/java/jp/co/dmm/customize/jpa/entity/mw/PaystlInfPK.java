package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PAYSTL_INF database table.
 * 
 */
@Embeddable
public class PaystlInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PAY_NO")
	private String payNo;

	@Column(name="PAYSTL_NO")
	private long paystlNo;

	public PaystlInfPK() {
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
	public long getPaystlNo() {
		return this.paystlNo;
	}
	public void setPaystlNo(long paystlNo) {
		this.paystlNo = paystlNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PaystlInfPK)) {
			return false;
		}
		PaystlInfPK castOther = (PaystlInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.payNo.equals(castOther.payNo)
			&& (this.paystlNo == castOther.paystlNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.payNo.hashCode();
		hash = hash * prime + ((int) (this.paystlNo ^ (this.paystlNo >>> 32)));
		
		return hash;
	}
}