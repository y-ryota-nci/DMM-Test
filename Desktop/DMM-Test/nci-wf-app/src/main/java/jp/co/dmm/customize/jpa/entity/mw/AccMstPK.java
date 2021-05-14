package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ACC_MST database table.
 * 
 */
@Embeddable
public class AccMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="ACC_CD")
	private String accCd;

	private long sqno;

	public AccMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getAccCd() {
		return this.accCd;
	}
	public void setAccCd(String accCd) {
		this.accCd = accCd;
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
		if (!(other instanceof AccMstPK)) {
			return false;
		}
		AccMstPK castOther = (AccMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.accCd.equals(castOther.accCd)
			&& (this.sqno == castOther.sqno);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.accCd.hashCode();
		hash = hash * prime + ((int) (this.sqno ^ (this.sqno >>> 32)));
		
		return hash;
	}
}