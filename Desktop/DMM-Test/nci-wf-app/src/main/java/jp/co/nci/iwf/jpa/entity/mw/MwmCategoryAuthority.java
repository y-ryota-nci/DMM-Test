package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_CATEGORY_AUTHORITY database table.
 *
 */
@Entity
@Table(name="MWM_CATEGORY_AUTHORITY", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "MENU_ROLE_CODE", "CATEGORY_ID"}))
@NamedQuery(name="MwmCategoryAuthority.findAll", query="SELECT m FROM MwmCategoryAuthority m")
public class MwmCategoryAuthority extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CATEGORY_AUTHORITY_ID")
	private long categoryAuthorityId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="MENU_ROLE_CODE")
	private String menuRoleCode;

	@Column(name="CATEGORY_ID")
	private Long categoryId;

	public MwmCategoryAuthority() {
	}

	public long getCategoryAuthorityId() {
		return this.categoryAuthorityId;
	}

	public void setCategoryAuthorityId(long categoryAuthorityId) {
		this.categoryAuthorityId = categoryAuthorityId;
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

	public Long getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

}