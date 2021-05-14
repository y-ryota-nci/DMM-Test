package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ITM_MST database table.
 * 
 */
@Embeddable
public class ItmMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="ORGNZ_CD")
	private String orgnzCd;

	@Column(name="ITM_CD")
	private String itmCd;

	private long sqno;

	public ItmMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getOrgnzCd() {
		return this.orgnzCd;
	}
	public void setOrgnzCd(String orgnzCd) {
		this.orgnzCd = orgnzCd;
	}
	public String getItmCd() {
		return this.itmCd;
	}
	public void setItmCd(String itmCd) {
		this.itmCd = itmCd;
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
		if (!(other instanceof ItmMstPK)) {
			return false;
		}
		ItmMstPK castOther = (ItmMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.orgnzCd.equals(castOther.orgnzCd)
			&& this.itmCd.equals(castOther.itmCd)
			&& (this.sqno == castOther.sqno);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.orgnzCd.hashCode();
		hash = hash * prime + this.itmCd.hashCode();
		hash = hash * prime + ((int) (this.sqno ^ (this.sqno >>> 32)));
		
		return hash;
	}
}