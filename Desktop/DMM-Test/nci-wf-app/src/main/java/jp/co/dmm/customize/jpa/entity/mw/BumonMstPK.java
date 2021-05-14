package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the BUMON_MST database table.
 * 
 */
@Embeddable
public class BumonMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="BUMON_CD")
	private String bumonCd;

	public BumonMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getBumonCd() {
		return this.bumonCd;
	}
	public void setBumonCd(String bumonCd) {
		this.bumonCd = bumonCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BumonMstPK)) {
			return false;
		}
		BumonMstPK castOther = (BumonMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.bumonCd.equals(castOther.bumonCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.bumonCd.hashCode();
		
		return hash;
	}
}