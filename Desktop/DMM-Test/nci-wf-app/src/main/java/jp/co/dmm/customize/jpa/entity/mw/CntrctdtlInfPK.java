package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the CNTRCTDTL_INF database table.
 * 
 */
@Embeddable
public class CntrctdtlInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="CNTRCT_NO")
	private String cntrctNo;

	@Column(name="CNTRCT_DTL_NO")
	private long cntrctDtlNo;

	public CntrctdtlInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getCntrctNo() {
		return this.cntrctNo;
	}
	public void setCntrctNo(String cntrctNo) {
		this.cntrctNo = cntrctNo;
	}
	public long getCntrctDtlNo() {
		return this.cntrctDtlNo;
	}
	public void setCntrctDtlNo(long cntrctDtlNo) {
		this.cntrctDtlNo = cntrctDtlNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CntrctdtlInfPK)) {
			return false;
		}
		CntrctdtlInfPK castOther = (CntrctdtlInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.cntrctNo.equals(castOther.cntrctNo)
			&& (this.cntrctDtlNo == castOther.cntrctDtlNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.cntrctNo.hashCode();
		hash = hash * prime + ((int) (this.cntrctDtlNo ^ (this.cntrctDtlNo >>> 32)));
		
		return hash;
	}
}