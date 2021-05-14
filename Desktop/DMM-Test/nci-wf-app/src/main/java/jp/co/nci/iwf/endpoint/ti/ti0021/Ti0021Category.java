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
public class Ti0021Category extends BaseJpaEntity {
	@Transient
	public boolean selected;

	@Column(name="CATEGORY_AUTHORITY_ID")
	public Long categoryAuthorityId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="MENU_ROLE_CODE")
	public String menuRoleCode;

	@Id
	@Column(name="CATEGORY_ID")
	public Long categoryId;

	@Column(name="CATEGORY_NAME")
	public String categoryName;

	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	@Version
	@Column(name="VERSION")
	public Long version;
}
