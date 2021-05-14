package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the BAL_INF database table.
 * 
 */
@Embeddable
public class BalInfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="ADD_YM")
	private String addYm;

	@Column(name="ACC_CD")
	private String accCd;

	@Column(name="ACC_X")
	private long accX;

	@Column(name="ACC_BRKDWN_CD")
	private String accBrkdwnCd;

	@Column(name="ACC_BRKDWN_X")
	private long accBrkdwnX;

	@Column(name="ORD_CD")
	private String ordCd;

	@Column(name="ACC_DPT_CD")
	private String accDptCd;

	@Column(name="ACC_DPT_X")
	private long accDptX;

	@Column(name="SPLR_CD")
	private String splrCd;

	@Column(name="MNY_CD")
	private String mnyCd;

	@Column(name="DC_TP")
	private String dcTp;

	public BalInfPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public String getAddYm() {
		return this.addYm;
	}
	public void setAddYm(String addYm) {
		this.addYm = addYm;
	}
	public String getAccCd() {
		return this.accCd;
	}
	public void setAccCd(String accCd) {
		this.accCd = accCd;
	}
	public long getAccX() {
		return this.accX;
	}
	public void setAccX(long accX) {
		this.accX = accX;
	}
	public String getAccBrkdwnCd() {
		return this.accBrkdwnCd;
	}
	public void setAccBrkdwnCd(String accBrkdwnCd) {
		this.accBrkdwnCd = accBrkdwnCd;
	}
	public long getAccBrkdwnX() {
		return this.accBrkdwnX;
	}
	public void setAccBrkdwnX(long accBrkdwnX) {
		this.accBrkdwnX = accBrkdwnX;
	}
	public String getOrdCd() {
		return this.ordCd;
	}
	public void setOrdCd(String ordCd) {
		this.ordCd = ordCd;
	}
	public String getAccDptCd() {
		return this.accDptCd;
	}
	public void setAccDptCd(String accDptCd) {
		this.accDptCd = accDptCd;
	}
	public long getAccDptX() {
		return this.accDptX;
	}
	public void setAccDptX(long accDptX) {
		this.accDptX = accDptX;
	}
	public String getSplrCd() {
		return this.splrCd;
	}
	public void setSplrCd(String splrCd) {
		this.splrCd = splrCd;
	}
	public String getMnyCd() {
		return this.mnyCd;
	}
	public void setMnyCd(String mnyCd) {
		this.mnyCd = mnyCd;
	}
	public String getDcTp() {
		return this.dcTp;
	}
	public void setDcTp(String dcTp) {
		this.dcTp = dcTp;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BalInfPK)) {
			return false;
		}
		BalInfPK castOther = (BalInfPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& this.addYm.equals(castOther.addYm)
			&& this.accCd.equals(castOther.accCd)
			&& (this.accX == castOther.accX)
			&& this.accBrkdwnCd.equals(castOther.accBrkdwnCd)
			&& (this.accBrkdwnX == castOther.accBrkdwnX)
			&& this.ordCd.equals(castOther.ordCd)
			&& this.accDptCd.equals(castOther.accDptCd)
			&& (this.accDptX == castOther.accDptX)
			&& this.splrCd.equals(castOther.splrCd)
			&& this.mnyCd.equals(castOther.mnyCd)
			&& this.dcTp.equals(castOther.dcTp);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + this.addYm.hashCode();
		hash = hash * prime + this.accCd.hashCode();
		hash = hash * prime + ((int) (this.accX ^ (this.accX >>> 32)));
		hash = hash * prime + this.accBrkdwnCd.hashCode();
		hash = hash * prime + ((int) (this.accBrkdwnX ^ (this.accBrkdwnX >>> 32)));
		hash = hash * prime + this.ordCd.hashCode();
		hash = hash * prime + this.accDptCd.hashCode();
		hash = hash * prime + ((int) (this.accDptX ^ (this.accDptX >>> 32)));
		hash = hash * prime + this.splrCd.hashCode();
		hash = hash * prime + this.mnyCd.hashCode();
		hash = hash * prime + this.dcTp.hashCode();
		
		return hash;
	}
}