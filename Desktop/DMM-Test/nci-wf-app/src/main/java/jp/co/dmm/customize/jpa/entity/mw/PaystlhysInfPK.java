package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PAYSTLHYS_INF database table.
 * 
 */
@Embeddable
public class PaystlhysInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PAYHYS_NO")
	private String payhysNo;

	@Column(name="PAYSTL_NO")
	private long paystlNo;

	public PaystlhysInfPK() {
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
		if (!(other instanceof PaystlhysInfPK)) {
			return false;
		}
		PaystlhysInfPK castOther = (PaystlhysInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.payhysNo.equals(castOther.payhysNo)
			&& (this.paystlNo == castOther.paystlNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.payhysNo.hashCode();
		hash = hash * prime + ((int) (this.paystlNo ^ (this.paystlNo >>> 32)));
		
		return hash;
	}
}