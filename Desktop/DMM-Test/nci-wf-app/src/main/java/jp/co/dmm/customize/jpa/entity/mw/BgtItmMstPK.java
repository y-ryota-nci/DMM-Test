package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the BGT_ITM_MST database table.
 * 
 */
@Embeddable
public class BgtItmMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="BGT_ITM_CD")
	private String bgtItmCd;

	public BgtItmMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getBgtItmCd() {
		return this.bgtItmCd;
	}
	public void setBgtItmCd(String bgtItmCd) {
		this.bgtItmCd = bgtItmCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BgtItmMstPK)) {
			return false;
		}
		BgtItmMstPK castOther = (BgtItmMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.bgtItmCd.equals(castOther.bgtItmCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.bgtItmCd.hashCode();
		
		return hash;
	}
}