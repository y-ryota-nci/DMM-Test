package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;


/**
 * The persistent class for the MWM_CATALOG_USED_DEPARTMENT database table.
 *
 */
@Entity
@Table(name="MWM_CATALOG_USED_DEPARTMENT")
@NamedQuery(name="MwmCatalogUsedDepartment.findAll", query="SELECT m FROM MwmCatalogUsedDepartment m")
public class MwmCatalogUsedDepartment extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CATALOG_USED_DEPARTMENT_ID")
	private long catalogUsedDepartmentId;
	@Column(name="CATALOG_ID")
	private long catalogId;
	@Column(name="CORPORATION_CODE")
	private String corporationCode;
	@Column(name="ORGANIZATION_CODE")
	private String organizationCode;
	@Column(name="IN_CHARGE_FLAG")
	private String inChargeFlag;

	public MwmCatalogUsedDepartment() {
	}

	public long getCatalogUsedDepartmentId() {
		return catalogUsedDepartmentId;
	}
	public void setCatalogUsedDepartmentId(long catalogUsedDepartmentId) {
		this.catalogUsedDepartmentId = catalogUsedDepartmentId;
	}

	public long getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(long catalogId) {
		this.catalogId = catalogId;
	}

	public String getCorporationCode() {
		return corporationCode;
	}
	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getInChargeFlag() {
		return inChargeFlag;
	}
	public void setInChargeFlag(String inChargeFlag) {
		this.inChargeFlag = inChargeFlag;
	}

}