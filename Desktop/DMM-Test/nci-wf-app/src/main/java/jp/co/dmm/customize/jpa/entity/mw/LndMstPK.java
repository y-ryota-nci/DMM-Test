package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the LND_MST database table.
 * 
 */
@Embeddable
public class LndMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="LND_CD")
	private String lndCd;

	public LndMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getLndCd() {
		return this.lndCd;
	}
	public void setLndCd(String lndCd) {
		this.lndCd = lndCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof LndMstPK)) {
			return false;
		}
		LndMstPK castOther = (LndMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.lndCd.equals(castOther.lndCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.lndCd.hashCode();
		
		return hash;
	}
}