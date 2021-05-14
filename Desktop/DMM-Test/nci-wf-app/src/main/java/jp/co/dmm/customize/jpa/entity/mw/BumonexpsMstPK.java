package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the BUMONEXPS_MST database table.
 * 
 */
@Embeddable
public class BumonexpsMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="BUMON_CD")
	private String bumonCd;

	@Column(name="ORGNZ_CD")
	private String orgnzCd;

	public BumonexpsMstPK() {
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
	public String getOrgnzCd() {
		return this.orgnzCd;
	}
	public void setOrgnzCd(String orgnzCd) {
		this.orgnzCd = orgnzCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BumonexpsMstPK)) {
			return false;
		}
		BumonexpsMstPK castOther = (BumonexpsMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.bumonCd.equals(castOther.bumonCd)
			&& this.orgnzCd.equals(castOther.orgnzCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.bumonCd.hashCode();
		hash = hash * prime + this.orgnzCd.hashCode();
		
		return hash;
	}
}