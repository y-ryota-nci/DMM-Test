package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ITMEXPS1_CHRMST database table.
 * 
 */
@Embeddable
public class Itmexps1ChrmstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="ITMEXPS_CD1")
	private String itmexpsCd1;

	@Column(name="ITMEXPS_CD2")
	private String itmexpsCd2;

	public Itmexps1ChrmstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getItmexpsCd1() {
		return this.itmexpsCd1;
	}
	public void setItmexpsCd1(String itmexpsCd1) {
		this.itmexpsCd1 = itmexpsCd1;
	}
	public String getItmexpsCd2() {
		return this.itmexpsCd2;
	}
	public void setItmexpsCd2(String itmexpsCd2) {
		this.itmexpsCd2 = itmexpsCd2;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Itmexps1ChrmstPK)) {
			return false;
		}
		Itmexps1ChrmstPK castOther = (Itmexps1ChrmstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.itmexpsCd1.equals(castOther.itmexpsCd1)
			&& this.itmexpsCd2.equals(castOther.itmexpsCd2);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.itmexpsCd1.hashCode();
		hash = hash * prime + this.itmexpsCd2.hashCode();
		
		return hash;
	}
}