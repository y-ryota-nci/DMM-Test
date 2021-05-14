package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the MLORD_INF database table.
 * 
 */
@Embeddable
public class MlordInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="LOT_NO")
	private String lotNo;

	private long sqno;

	public MlordInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getLotNo() {
		return this.lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public long getSqno() {
		return this.sqno;
	}
	public void setSqno(long sqno) {
		this.sqno = sqno;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MlordInfPK)) {
			return false;
		}
		MlordInfPK castOther = (MlordInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.lotNo.equals(castOther.lotNo)
			&& (this.sqno == castOther.sqno);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.lotNo.hashCode();
		hash = hash * prime + ((int) (this.sqno ^ (this.sqno >>> 32)));
		
		return hash;
	}
}