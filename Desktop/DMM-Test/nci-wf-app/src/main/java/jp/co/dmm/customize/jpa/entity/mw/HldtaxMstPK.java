package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the HLDTAX_MST database table.
 * 
 */
@Embeddable
public class HldtaxMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="HLDTAX_TP")
	private String hldtaxTp;

	public HldtaxMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getHldtaxTp() {
		return this.hldtaxTp;
	}
	public void setHldtaxTp(String hldtaxTp) {
		this.hldtaxTp = hldtaxTp;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof HldtaxMstPK)) {
			return false;
		}
		HldtaxMstPK castOther = (HldtaxMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.hldtaxTp.equals(castOther.hldtaxTp);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.hldtaxTp.hashCode();
		
		return hash;
	}
}