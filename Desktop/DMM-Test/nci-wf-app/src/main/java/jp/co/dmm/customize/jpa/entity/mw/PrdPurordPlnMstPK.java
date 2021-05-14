package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the PRD_PURORD_PLN_MST database table.
 *
 */
@Embeddable
public class PrdPurordPlnMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PRD_PURORD_NO")
	private long prdPurordNo;

	private long sqno;

	public PrdPurordPlnMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public long getPrdPurordNo() {
		return this.prdPurordNo;
	}
	public void setPrdPurordNo(long prdPurordNo) {
		this.prdPurordNo = prdPurordNo;
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
		if (!(other instanceof PrdPurordPlnMstPK)) {
			return false;
		}
		PrdPurordPlnMstPK castOther = (PrdPurordPlnMstPK)other;
		return
			this.companyCd.equals(castOther.companyCd)
			&& (this.prdPurordNo == castOther.prdPurordNo)
			&& (this.sqno == castOther.sqno);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + ((int) (this.prdPurordNo ^ (this.prdPurordNo >>> 32)));
		hash = hash * prime + ((int) (this.sqno ^ (this.sqno >>> 32)));

		return hash;
	}
}