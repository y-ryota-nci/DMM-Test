package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PAY_APPL_MST database table.
 * 
 */
@Embeddable
public class PayApplMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="PAY_APPL_CD")
	private String payApplCd;

	public PayApplMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getPayApplCd() {
		return this.payApplCd;
	}
	public void setPayApplCd(String payApplCd) {
		this.payApplCd = payApplCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PayApplMstPK)) {
			return false;
		}
		PayApplMstPK castOther = (PayApplMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.payApplCd.equals(castOther.payApplCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.payApplCd.hashCode();
		
		return hash;
	}
}