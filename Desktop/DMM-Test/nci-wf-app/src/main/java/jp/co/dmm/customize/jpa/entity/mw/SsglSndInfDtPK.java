package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the SSGL_SND_INF_DT database table.
 * 
 */
@Embeddable
public class SsglSndInfDtPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="SSGL_SND_NO")
	private String ssglSndNo;

	@Column(name="SSGL_SND_DTL_NO")
	private long ssglSndDtlNo;

	public SsglSndInfDtPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getSsglSndNo() {
		return this.ssglSndNo;
	}
	public void setSsglSndNo(String ssglSndNo) {
		this.ssglSndNo = ssglSndNo;
	}
	public long getSsglSndDtlNo() {
		return this.ssglSndDtlNo;
	}
	public void setSsglSndDtlNo(long ssglSndDtlNo) {
		this.ssglSndDtlNo = ssglSndDtlNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SsglSndInfDtPK)) {
			return false;
		}
		SsglSndInfDtPK castOther = (SsglSndInfDtPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.ssglSndNo.equals(castOther.ssglSndNo)
			&& (this.ssglSndDtlNo == castOther.ssglSndDtlNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.ssglSndNo.hashCode();
		hash = hash * prime + ((int) (this.ssglSndDtlNo ^ (this.ssglSndDtlNo >>> 32)));
		
		return hash;
	}
}