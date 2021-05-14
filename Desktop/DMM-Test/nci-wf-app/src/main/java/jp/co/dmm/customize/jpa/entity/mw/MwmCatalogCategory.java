package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;


/**
 * The persistent class for the MWM_CATALOG_CATEGORY database table.
 *
 */
@Entity
@Table(name="MWM_CATALOG_CATEGORY", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "CATALOG_CATEGORY_CODE"}))
@NamedQuery(name="MwmCatalogCategory.findAll", query="SELECT m FROM MwmCatalogCategory m")
public class MwmCatalogCategory extends MwmBaseJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CATALOG_CATEGORY_ID")
	private long catalogCategoryId;
	@Column(name="CORPORATION_CODE")
	private String corporationCode;
	@Column(name="CATALOG_CATEGORY_CODE")
	private String catalogCategoryCode;
	@Column(name="CATALOG_CATEGORY_NAME")
	private String catalogCategoryName;

	public MwmCatalogCategory() {
	}

	public long getCatalogCategoryId() {
		return catalogCategoryId;
	}
	public void setCatalogCategoryId(long catalogCategoryId) {
		this.catalogCategoryId = catalogCategoryId;
	}

	public String getCorporationCode() {
		return corporationCode;
	}
	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getCatalogCategoryCode() {
		return catalogCategoryCode;
	}
	public void setCatalogCategoryCode(String catalogCategoryCode) {
		this.catalogCategoryCode = catalogCategoryCode;
	}

	public String getCatalogCategoryName() {
		return catalogCategoryName;
	}
	public void setCatalogCategoryName(String catalogCategoryName) {
		this.catalogCategoryName = catalogCategoryName;
	}

}