package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the CRDCRD_BNKACC_MST database table.
 * 
 */
@Embeddable
public class CrdcrdBnkaccMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="SPLR_CD")
	private String splrCd;

	@Column(name="USR_CD")
	private String usrCd;

	public CrdcrdBnkaccMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getSplrCd() {
		return this.splrCd;
	}
	public void setSplrCd(String splrCd) {
		this.splrCd = splrCd;
	}
	public String getUsrCd() {
		return this.usrCd;
	}
	public void setUsrCd(String usrCd) {
		this.usrCd = usrCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CrdcrdBnkaccMstPK)) {
			return false;
		}
		CrdcrdBnkaccMstPK castOther = (CrdcrdBnkaccMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.splrCd.equals(castOther.splrCd)
			&& this.usrCd.equals(castOther.usrCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.splrCd.hashCode();
		hash = hash * prime + this.usrCd.hashCode();
		
		return hash;
	}
}