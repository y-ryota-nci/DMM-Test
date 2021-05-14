package jp.co.nci.iwf.endpoint.ti.ti0021;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Ti0021Table extends BaseJpaEntity {
	@Transient
	public boolean selected;

	@Column(name="TABLE_AUTHORITY_ID")
	public Long tableAuthorityId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	@Column(name="MENU_ROLE_CODE")
	public String menuRoleCode;

	@Column(name="CATEGORY_ID")
	public Long categoryId;

	@Id
	@Column(name="TABLE_ID")
	public Long tableId;

	@Column(name="TABLE_NAME")
	public String tableName;

	@Column(name="LOGICAL_TABLE_NAME")
	public String logicalTableName;

	@Version
	@Column(name="VERSION")
	public Long tableVersion;
}
