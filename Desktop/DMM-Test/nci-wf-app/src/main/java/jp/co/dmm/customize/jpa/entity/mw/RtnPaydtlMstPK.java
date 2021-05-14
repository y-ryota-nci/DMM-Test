package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RTN_PAYDTL_MST database table.
 * 
 */
@Embeddable
public class RtnPaydtlMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="RTN_PAY_NO")
	private long rtnPayNo;

	@Column(name="RTN_PAY_DTL_NO")
	private long rtnPayDtlNo;

	public RtnPaydtlMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public long getRtnPayNo() {
		return this.rtnPayNo;
	}
	public void setRtnPayNo(long rtnPayNo) {
		this.rtnPayNo = rtnPayNo;
	}
	public long getRtnPayDtlNo() {
		return this.rtnPayDtlNo;
	}
	public void setRtnPayDtlNo(long rtnPayDtlNo) {
		this.rtnPayDtlNo = rtnPayDtlNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RtnPaydtlMstPK)) {
			return false;
		}
		RtnPaydtlMstPK castOther = (RtnPaydtlMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& (this.rtnPayNo == castOther.rtnPayNo)
			&& (this.rtnPayDtlNo == castOther.rtnPayDtlNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + ((int) (this.rtnPayNo ^ (this.rtnPayNo >>> 32)));
		hash = hash * prime + ((int) (this.rtnPayDtlNo ^ (this.rtnPayDtlNo >>> 32)));
		
		return hash;
	}
}