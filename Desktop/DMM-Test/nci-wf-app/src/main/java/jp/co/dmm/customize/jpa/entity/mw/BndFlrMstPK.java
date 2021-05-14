package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the BND_FLR_MST database table.
 * 
 */
@Embeddable
public class BndFlrMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="BND_FLR_CD")
	private String bndFlrCd;

	public BndFlrMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getBndFlrCd() {
		return this.bndFlrCd;
	}
	public void setBndFlrCd(String bndFlrCd) {
		this.bndFlrCd = bndFlrCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BndFlrMstPK)) {
			return false;
		}
		BndFlrMstPK castOther = (BndFlrMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.bndFlrCd.equals(castOther.bndFlrCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.bndFlrCd.hashCode();
		
		return hash;
	}
}