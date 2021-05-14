package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PURRQSTDTL_INF database table.
 * 
 */
@Embeddable
public class PurrqstdtlInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PURRQST_NO")
	private String purrqstNo;

	@Column(name="PURRQST_DTL_NO")
	private long purrqstDtlNo;

	public PurrqstdtlInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getPurrqstNo() {
		return this.purrqstNo;
	}
	public void setPurrqstNo(String purrqstNo) {
		this.purrqstNo = purrqstNo;
	}
	public long getPurrqstDtlNo() {
		return this.purrqstDtlNo;
	}
	public void setPurrqstDtlNo(long purrqstDtlNo) {
		this.purrqstDtlNo = purrqstDtlNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PurrqstdtlInfPK)) {
			return false;
		}
		PurrqstdtlInfPK castOther = (PurrqstdtlInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.purrqstNo.equals(castOther.purrqstNo)
			&& (this.purrqstDtlNo == castOther.purrqstDtlNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.purrqstNo.hashCode();
		hash = hash * prime + ((int) (this.purrqstDtlNo ^ (this.purrqstDtlNo >>> 32)));
		
		return hash;
	}
}