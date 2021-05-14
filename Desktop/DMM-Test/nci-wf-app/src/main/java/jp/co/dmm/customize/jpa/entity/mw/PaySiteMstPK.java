package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PAY_SITE_MST database table.
 * 
 */
@Embeddable
public class PaySiteMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PAY_SITE_CD")
	private String paySiteCd;

	public PaySiteMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getPaySiteCd() {
		return this.paySiteCd;
	}
	public void setPaySiteCd(String paySiteCd) {
		this.paySiteCd = paySiteCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PaySiteMstPK)) {
			return false;
		}
		PaySiteMstPK castOther = (PaySiteMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.paySiteCd.equals(castOther.paySiteCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.paySiteCd.hashCode();
		
		return hash;
	}
}