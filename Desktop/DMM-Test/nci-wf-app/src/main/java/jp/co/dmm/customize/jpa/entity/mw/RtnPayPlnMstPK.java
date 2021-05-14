package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RTN_PAY_PLN_MST database table.
 * 
 */
@Embeddable
public class RtnPayPlnMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="RTN_PAY_NO")
	private long rtnPayNo;

	private long sqno;

	public RtnPayPlnMstPK() {
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
		if (!(other instanceof RtnPayPlnMstPK)) {
			return false;
		}
		RtnPayPlnMstPK castOther = (RtnPayPlnMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& (this.rtnPayNo == castOther.rtnPayNo)
			&& (this.sqno == castOther.sqno);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + ((int) (this.rtnPayNo ^ (this.rtnPayNo >>> 32)));
		hash = hash * prime + ((int) (this.sqno ^ (this.sqno >>> 32)));
		
		return hash;
	}
}