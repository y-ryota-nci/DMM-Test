package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the BUYDTL_INF database table.
 * 
 */
@Embeddable
public class BuydtlInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="BUY_NO")
	private String buyNo;

	@Column(name="BUY_DTL_NO")
	private long buyDtlNo;

	public BuydtlInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getBuyNo() {
		return this.buyNo;
	}
	public void setBuyNo(String buyNo) {
		this.buyNo = buyNo;
	}
	public long getBuyDtlNo() {
		return this.buyDtlNo;
	}
	public void setBuyDtlNo(long buyDtlNo) {
		this.buyDtlNo = buyDtlNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BuydtlInfPK)) {
			return false;
		}
		BuydtlInfPK castOther = (BuydtlInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.buyNo.equals(castOther.buyNo)
			&& (this.buyDtlNo == castOther.buyDtlNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.buyNo.hashCode();
		hash = hash * prime + ((int) (this.buyDtlNo ^ (this.buyDtlNo >>> 32)));
		
		return hash;
	}
}