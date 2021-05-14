package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TAXEXPS_MST database table.
 * 
 */
@Embeddable
public class TaxexpsMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="TAX_KND_CD")
	private String taxKndCd;

	@Column(name="TAX_SPC")
	private String taxSpc;

	public TaxexpsMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getTaxKndCd() {
		return this.taxKndCd;
	}
	public void setTaxKndCd(String taxKndCd) {
		this.taxKndCd = taxKndCd;
	}
	public String getTaxSpc() {
		return this.taxSpc;
	}
	public void setTaxSpc(String taxSpc) {
		this.taxSpc = taxSpc;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TaxexpsMstPK)) {
			return false;
		}
		TaxexpsMstPK castOther = (TaxexpsMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.taxKndCd.equals(castOther.taxKndCd)
			&& this.taxSpc.equals(castOther.taxSpc);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.taxKndCd.hashCode();
		hash = hash * prime + this.taxSpc.hashCode();
		
		return hash;
	}
}