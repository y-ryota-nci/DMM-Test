package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the CNTRCT_SPLR_INF database table.
 * 
 */
@Embeddable
public class CntrctSplrInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="CNTRCT_NO")
	private String cntrctNo;

	private long sqno;

	public CntrctSplrInfPK() {
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
	public long getSqno() {
		return this.sqno;
	}
	public void setSqno(long sqno) {
		this.sqno = sqno;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CntrctSplrInfPK)) {
			return false;
		}
		CntrctSplrInfPK castOther = (CntrctSplrInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.cntrctNo.equals(castOther.cntrctNo)
			&& (this.sqno == castOther.sqno);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.cntrctNo.hashCode();
		hash = hash * prime + ((int) (this.sqno ^ (this.sqno >>> 32)));
		
		return hash;
	}
}