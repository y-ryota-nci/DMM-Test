package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ACC_BRKDWN_MST database table.
 * 
 */
@Embeddable
public class AccBrkdwnMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="ACC_CD")
	private String accCd;

	@Column(name="ACC_BRKDWN_CD")
	private String accBrkdwnCd;

	private long sqno;

	public AccBrkdwnMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getAccCd() {
		return this.accCd;
	}
	public void setAccCd(String accCd) {
		this.accCd = accCd;
	}
	public String getAccBrkdwnCd() {
		return this.accBrkdwnCd;
	}
	public void setAccBrkdwnCd(String accBrkdwnCd) {
		this.accBrkdwnCd = accBrkdwnCd;
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
		if (!(other instanceof AccBrkdwnMstPK)) {
			return false;
		}
		AccBrkdwnMstPK castOther = (AccBrkdwnMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.accCd.equals(castOther.accCd)
			&& this.accBrkdwnCd.equals(castOther.accBrkdwnCd)
			&& (this.sqno == castOther.sqno);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.accCd.hashCode();
		hash = hash * prime + this.accBrkdwnCd.hashCode();
		hash = hash * prime + ((int) (this.sqno ^ (this.sqno >>> 32)));
		
		return hash;
	}
}