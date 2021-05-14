package jp.co.dmm.customize.endpoint.ct.ct0011;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class CatalogUsedDepartment extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CATALOG_USED_DEPARTMENT_ID")
	public long catalogUsedDepartmentId;
	@Column(name="CATALOG_ID")
	public long catalogId;
	@Column(name="CORPORATION_CODE")
	public String corporationCode;
	@Column(name="ORGANIZATION_CODE")
	public String organizationCode;
	@Column(name="IN_CHARGE_FLAG")
	public String inChargeFlag;
	@Column(name="ORGANIZATION_NAME")
	public String organizationName;

}