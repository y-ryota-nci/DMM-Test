package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the JRN_INF database table.
 * 
 */
@Embeddable
public class JrnInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="JRNSLP_NO")
	private String jrnslpNo;

	@Column(name="JRNSLP_DTL_NO")
	private long jrnslpDtlNo;

	public JrnInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getJrnslpNo() {
		return this.jrnslpNo;
	}
	public void setJrnslpNo(String jrnslpNo) {
		this.jrnslpNo = jrnslpNo;
	}
	public long getJrnslpDtlNo() {
		return this.jrnslpDtlNo;
	}
	public void setJrnslpDtlNo(long jrnslpDtlNo) {
		this.jrnslpDtlNo = jrnslpDtlNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof JrnInfPK)) {
			return false;
		}
		JrnInfPK castOther = (JrnInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.jrnslpNo.equals(castOther.jrnslpNo)
			&& (this.jrnslpDtlNo == castOther.jrnslpDtlNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.jrnslpNo.hashCode();
		hash = hash * prime + ((int) (this.jrnslpDtlNo ^ (this.jrnslpDtlNo >>> 32)));
		
		return hash;
	}
}