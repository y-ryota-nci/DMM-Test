package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PRD_PURORD_MST database table.
 * 
 */
@Embeddable
public class PrdPurordMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PRD_PURORD_NO")
	private long prdPurordNo;

	public PrdPurordMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public long getPrdPurordNo() {
		return this.prdPurordNo;
	}
	public void setPrdPurordNo(long prdPurordNo) {
		this.prdPurordNo = prdPurordNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PrdPurordMstPK)) {
			return false;
		}
		PrdPurordMstPK castOther = (PrdPurordMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& (this.prdPurordNo == castOther.prdPurordNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + ((int) (this.prdPurordNo ^ (this.prdPurordNo >>> 32)));
		
		return hash;
	}
}