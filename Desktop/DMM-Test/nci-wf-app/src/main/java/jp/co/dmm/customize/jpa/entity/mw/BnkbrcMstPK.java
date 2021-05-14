package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the BNKBRC_MST database table.
 * 
 */
@Embeddable
public class BnkbrcMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="BNK_CD")
	private String bnkCd;

	@Column(name="BNKBRC_CD")
	private String bnkbrcCd;

	public BnkbrcMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getBnkCd() {
		return this.bnkCd;
	}
	public void setBnkCd(String bnkCd) {
		this.bnkCd = bnkCd;
	}
	public String getBnkbrcCd() {
		return this.bnkbrcCd;
	}
	public void setBnkbrcCd(String bnkbrcCd) {
		this.bnkbrcCd = bnkbrcCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BnkbrcMstPK)) {
			return false;
		}
		BnkbrcMstPK castOther = (BnkbrcMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.bnkCd.equals(castOther.bnkCd)
			&& this.bnkbrcCd.equals(castOther.bnkbrcCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.bnkCd.hashCode();
		hash = hash * prime + this.bnkbrcCd.hashCode();
		
		return hash;
	}
}