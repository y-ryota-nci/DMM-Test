package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the MNY_MST database table.
 * 
 */
@Embeddable
public class MnyMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="MNY_CD")
	private String mnyCd;

	public MnyMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getMnyCd() {
		return this.mnyCd;
	}
	public void setMnyCd(String mnyCd) {
		this.mnyCd = mnyCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MnyMstPK)) {
			return false;
		}
		MnyMstPK castOther = (MnyMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.mnyCd.equals(castOther.mnyCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.mnyCd.hashCode();
		
		return hash;
	}
}