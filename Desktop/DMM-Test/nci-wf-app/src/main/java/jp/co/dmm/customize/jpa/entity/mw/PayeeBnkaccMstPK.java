package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PAYEE_BNKACC_MST database table.
 * 
 */
@Embeddable
public class PayeeBnkaccMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PAYEE_BNKACC_CD")
	private String payeeBnkaccCd;

	private long sqno;

	public PayeeBnkaccMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getPayeeBnkaccCd() {
		return this.payeeBnkaccCd;
	}
	public void setPayeeBnkaccCd(String payeeBnkaccCd) {
		this.payeeBnkaccCd = payeeBnkaccCd;
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
		if (!(other instanceof PayeeBnkaccMstPK)) {
			return false;
		}
		PayeeBnkaccMstPK castOther = (PayeeBnkaccMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.payeeBnkaccCd.equals(castOther.payeeBnkaccCd)
			&& (this.sqno == castOther.sqno);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.payeeBnkaccCd.hashCode();
		hash = hash * prime + ((int) (this.sqno ^ (this.sqno >>> 32)));
		
		return hash;
	}
}