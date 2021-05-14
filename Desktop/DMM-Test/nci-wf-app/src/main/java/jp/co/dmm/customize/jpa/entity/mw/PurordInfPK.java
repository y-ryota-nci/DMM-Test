package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PURORD_INF database table.
 * 
 */
@Embeddable
public class PurordInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PURORD_NO")
	private String purordNo;

	public PurordInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getPurordNo() {
		return this.purordNo;
	}
	public void setPurordNo(String purordNo) {
		this.purordNo = purordNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PurordInfPK)) {
			return false;
		}
		PurordInfPK castOther = (PurordInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.purordNo.equals(castOther.purordNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.purordNo.hashCode();
		
		return hash;
	}
}