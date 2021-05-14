package jp.co.nci.iwf.jpa.entity.wm;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the WFM_MENU_ROLE_DETAIL database table.
 * 
 */
@Embeddable
public class WfmMenuRoleDetailPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="MENU_ROLE_CODE")
	private String menuRoleCode;

	@Column(name="SEQ_NO_MENU_ROLE_DETAIL")
	private long seqNoMenuRoleDetail;

	public WfmMenuRoleDetailPK() {
	}
	public String getCorporationCode() {
		return this.corporationCode;
	}
	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}
	public String getMenuRoleCode() {
		return this.menuRoleCode;
	}
	public void setMenuRoleCode(String menuRoleCode) {
		this.menuRoleCode = menuRoleCode;
	}
	public long getSeqNoMenuRoleDetail() {
		return this.seqNoMenuRoleDetail;
	}
	public void setSeqNoMenuRoleDetail(long seqNoMenuRoleDetail) {
		this.seqNoMenuRoleDetail = seqNoMenuRoleDetail;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof WfmMenuRoleDetailPK)) {
			return false;
		}
		WfmMenuRoleDetailPK castOther = (WfmMenuRoleDetailPK)other;
		return 
			this.corporationCode.equals(castOther.corporationCode)
			&& this.menuRoleCode.equals(castOther.menuRoleCode)
			&& (this.seqNoMenuRoleDetail == castOther.seqNoMenuRoleDetail);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corporationCode.hashCode();
		hash = hash * prime + this.menuRoleCode.hashCode();
		hash = hash * prime + ((int) (this.seqNoMenuRoleDetail ^ (this.seqNoMenuRoleDetail >>> 32)));
		
		return hash;
	}
}