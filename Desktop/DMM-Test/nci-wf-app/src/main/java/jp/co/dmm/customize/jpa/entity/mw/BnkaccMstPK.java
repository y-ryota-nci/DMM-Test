package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the BNKACC_MST database table.
 * 
 */
@Embeddable
public class BnkaccMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="BNKACC_CD")
	private String bnkaccCd;

	private long sqno;

	public BnkaccMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getBnkaccCd() {
		return this.bnkaccCd;
	}
	public void setBnkaccCd(String bnkaccCd) {
		this.bnkaccCd = bnkaccCd;
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
		if (!(other instanceof BnkaccMstPK)) {
			return false;
		}
		BnkaccMstPK castOther = (BnkaccMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.bnkaccCd.equals(castOther.bnkaccCd)
			&& (this.sqno == castOther.sqno);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.bnkaccCd.hashCode();
		hash = hash * prime + ((int) (this.sqno ^ (this.sqno >>> 32)));
		
		return hash;
	}
}