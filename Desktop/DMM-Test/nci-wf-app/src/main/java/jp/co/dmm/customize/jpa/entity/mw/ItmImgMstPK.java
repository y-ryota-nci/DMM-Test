package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ITM_IMG_MST database table.
 * 
 */
@Embeddable
public class ItmImgMstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="ITM_IMG_ID")
	private long itmImgId;

	public ItmImgMstPK() {
	}
	public String getCompanyCd() {
		return this.companyCd;
	}
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}
	public long getItmImgId() {
		return this.itmImgId;
	}
	public void setItmImgId(long itmImgId) {
		this.itmImgId = itmImgId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ItmImgMstPK)) {
			return false;
		}
		ItmImgMstPK castOther = (ItmImgMstPK)other;
		return 
			this.companyCd.equals(castOther.companyCd)
			&& (this.itmImgId == castOther.itmImgId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.companyCd.hashCode();
		hash = hash * prime + ((int) (this.itmImgId ^ (this.itmImgId >>> 32)));
		
		return hash;
	}
}