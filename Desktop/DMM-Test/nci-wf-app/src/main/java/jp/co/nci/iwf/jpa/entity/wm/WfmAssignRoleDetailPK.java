package jp.co.nci.iwf.jpa.entity.wm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the WFM_ASSIGN_ROLE_DETAIL database table.
 *
 */
@Embeddable
public class WfmAssignRoleDetailPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="ASSIGN_ROLE_CODE")
	private String assignRoleCode;

	@Column(name="SEQ_NO_ASSIGN_ROLE_DETAIL")
	private long seqNoAssignRoleDetail;

	public WfmAssignRoleDetailPK() {
	}
	public String getCorporationCode() {
		return this.corporationCode;
	}
	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}
	public String getAssignRoleCode() {
		return this.assignRoleCode;
	}
	public void setAssignRoleCode(String assignRoleCode) {
		this.assignRoleCode = assignRoleCode;
	}
	public long getSeqNoAssignRoleDetail() {
		return this.seqNoAssignRoleDetail;
	}
	public void setSeqNoAssignRoleDetail(long seqNoAssignRoleDetail) {
		this.seqNoAssignRoleDetail = seqNoAssignRoleDetail;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof WfmAssignRoleDetailPK)) {
			return false;
		}
		WfmAssignRoleDetailPK castOther = (WfmAssignRoleDetailPK)other;
		return
			this.corporationCode.equals(castOther.corporationCode)
			&& this.assignRoleCode.equals(castOther.assignRoleCode)
			&& (this.seqNoAssignRoleDetail == castOther.seqNoAssignRoleDetail);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corporationCode.hashCode();
		hash = hash * prime + this.assignRoleCode.hashCode();
		hash = hash * prime + ((int) (this.seqNoAssignRoleDetail ^ (this.seqNoAssignRoleDetail >>> 32)));

		return hash;
	}
}