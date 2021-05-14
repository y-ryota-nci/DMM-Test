package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RTN_PAY_MST database table.
 * 
 */
@Embeddable
public class RtnPayMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="RTN_PAY_NO")
	private long rtnPayNo;

	@Column(name="CNTRCT_NO")
	private String cntrctNo;

	public RtnPayMstPK() {
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
	public String getCntrctNo() {
		return this.cntrctNo;
	}
	public void setCntrctNo(String cntrctNo) {
		this.cntrctNo = cntrctNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RtnPayMstPK)) {
			return false;
		}
		RtnPayMstPK castOther = (RtnPayMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& (this.rtnPayNo == castOther.rtnPayNo)
			&& this.cntrctNo.equals(castOther.cntrctNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + ((int) (this.rtnPayNo ^ (this.rtnPayNo >>> 32)));
		hash = hash * prime + this.cntrctNo.hashCode();
		
		return hash;
	}
}