package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ACC_CLND_MST database table.
 * 
 */
@Embeddable
public class AccClndMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Temporal(TemporalType.DATE)
	@Column(name="CLND_DT")
	private java.util.Date clndDt;

	public AccClndMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public java.util.Date getClndDt() {
		return this.clndDt;
	}
	public void setClndDt(java.util.Date clndDt) {
		this.clndDt = clndDt;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AccClndMstPK)) {
			return false;
		}
		AccClndMstPK castOther = (AccClndMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.clndDt.equals(castOther.clndDt);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.clndDt.hashCode();
		
		return hash;
	}
}