package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RCVINSPDTL_INF database table.
 * 
 */
@Embeddable
public class RcvinspdtlInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="RCVINSP_NO")
	private String rcvinspNo;

	@Column(name="RCVINSP_DTL_NO")
	private long rcvinspDtlNo;

	public RcvinspdtlInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getRcvinspNo() {
		return this.rcvinspNo;
	}
	public void setRcvinspNo(String rcvinspNo) {
		this.rcvinspNo = rcvinspNo;
	}
	public long getRcvinspDtlNo() {
		return this.rcvinspDtlNo;
	}
	public void setRcvinspDtlNo(long rcvinspDtlNo) {
		this.rcvinspDtlNo = rcvinspDtlNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RcvinspdtlInfPK)) {
			return false;
		}
		RcvinspdtlInfPK castOther = (RcvinspdtlInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.rcvinspNo.equals(castOther.rcvinspNo)
			&& (this.rcvinspDtlNo == castOther.rcvinspDtlNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.rcvinspNo.hashCode();
		hash = hash * prime + ((int) (this.rcvinspDtlNo ^ (this.rcvinspDtlNo >>> 32)));
		
		return hash;
	}
}