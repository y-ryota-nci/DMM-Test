package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the CRDCRD_INF database table.
 * 
 */
@Embeddable
public class CrdcrdInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="CRDCRD_IN_NO")
	private String crdcrdInNo;

	public CrdcrdInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getCrdcrdInNo() {
		return this.crdcrdInNo;
	}
	public void setCrdcrdInNo(String crdcrdInNo) {
		this.crdcrdInNo = crdcrdInNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CrdcrdInfPK)) {
			return false;
		}
		CrdcrdInfPK castOther = (CrdcrdInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.crdcrdInNo.equals(castOther.crdcrdInNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.crdcrdInNo.hashCode();
		
		return hash;
	}
}