package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the SSAP_SND_INF_HD database table.
 * 
 */
@Embeddable
public class SsapSndInfHdPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="SSAP_SND_NO")
	private String ssapSndNo;

	public SsapSndInfHdPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getSsapSndNo() {
		return this.ssapSndNo;
	}
	public void setSsapSndNo(String ssapSndNo) {
		this.ssapSndNo = ssapSndNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SsapSndInfHdPK)) {
			return false;
		}
		SsapSndInfHdPK castOther = (SsapSndInfHdPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.ssapSndNo.equals(castOther.ssapSndNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.ssapSndNo.hashCode();
		
		return hash;
	}
}