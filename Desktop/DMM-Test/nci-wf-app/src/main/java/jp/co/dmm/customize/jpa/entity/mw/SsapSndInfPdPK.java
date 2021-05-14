package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the SSAP_SND_INF_PD database table.
 * 
 */
@Embeddable
public class SsapSndInfPdPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="SSAP_SND_NO")
	private String ssapSndNo;

	@Column(name="SSAP_SND_DTL_NO")
	private long ssapSndDtlNo;

	public SsapSndInfPdPK() {
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
	public long getSsapSndDtlNo() {
		return this.ssapSndDtlNo;
	}
	public void setSsapSndDtlNo(long ssapSndDtlNo) {
		this.ssapSndDtlNo = ssapSndDtlNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SsapSndInfPdPK)) {
			return false;
		}
		SsapSndInfPdPK castOther = (SsapSndInfPdPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.ssapSndNo.equals(castOther.ssapSndNo)
			&& (this.ssapSndDtlNo == castOther.ssapSndDtlNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.ssapSndNo.hashCode();
		hash = hash * prime + ((int) (this.ssapSndDtlNo ^ (this.ssapSndDtlNo >>> 32)));
		
		return hash;
	}
}