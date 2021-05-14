package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ADVPAY_INF database table.
 * 
 */
@Embeddable
public class AdvpayInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="ADVPAY_NO")
	private String advpayNo;

	public AdvpayInfPK() {
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AdvpayInfPK)) {
			return false;
		}
		AdvpayInfPK castOther = (AdvpayInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.advpayNo.equals(castOther.advpayNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.advpayNo.hashCode();
		
		return hash;
	}
}