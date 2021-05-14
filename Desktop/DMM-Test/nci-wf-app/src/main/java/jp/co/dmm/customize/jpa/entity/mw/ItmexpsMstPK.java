package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the ITMEXPS_MST database table.
 *
 */
@Embeddable
public class ItmexpsMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="ITMEXPS_CD")
	private String itmexpsCd;

	public ItmexpsMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getItmexpsCd() {
		return this.itmexpsCd;
	}
	public void setItmexpsCd(String itmexpsCd) {
		this.itmexpsCd = itmexpsCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ItmexpsMstPK)) {
			return false;
		}
		ItmexpsMstPK castOther = (ItmexpsMstPK)other;
		return
			this.companyCd.equals(castOther.companyCd)
			&& this.itmexpsCd.equals(castOther.itmexpsCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.itmexpsCd.hashCode();

		return hash;
	}
}