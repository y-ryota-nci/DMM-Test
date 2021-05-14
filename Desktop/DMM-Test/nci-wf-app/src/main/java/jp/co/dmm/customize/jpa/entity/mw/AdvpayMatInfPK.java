package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ADVPAY_MAT_INF database table.
 * 
 */
@Embeddable
public class AdvpayMatInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="ADVPAY_NO")
	private String advpayNo;

	@Column(name="ADVPAY_MAT_NO")
	private long advpayMatNo;

	public AdvpayMatInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getAdvpayNo() {
		return this.advpayNo;
	}
	public void setAdvpayNo(String advpayNo) {
		this.advpayNo = advpayNo;
	}
	public long getAdvpayMatNo() {
		return this.advpayMatNo;
	}
	public void setAdvpayMatNo(long advpayMatNo) {
		this.advpayMatNo = advpayMatNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AdvpayMatInfPK)) {
			return false;
		}
		AdvpayMatInfPK castOther = (AdvpayMatInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.advpayNo.equals(castOther.advpayNo)
			&& (this.advpayMatNo == castOther.advpayMatNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.advpayNo.hashCode();
		hash = hash * prime + ((int) (this.advpayMatNo ^ (this.advpayMatNo >>> 32)));
		
		return hash;
	}
}