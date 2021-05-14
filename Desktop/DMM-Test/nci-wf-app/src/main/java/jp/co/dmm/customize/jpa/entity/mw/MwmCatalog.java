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
 * The persistent class for the MWM_CATALOG database table.
 *
 */
@Entity
@Table(name="MWM_CATALOG", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "CATALOG_CODE"}))
@NamedQuery(name="MwmCatalog.findAll", query="SELECT m FROM MwmCatalog m")
public class MwmCatalog extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CATALOG_ID")
	private long catalogId;
	@Column(name="CORPORATION_CODE")
	private String corporationCode;
	@Column(name="CATALOG_CODE")
	private String catalogCode;
	@Column(name="CATALOG_NAME")
	private String catalogName;
	@Column(name="CATALOG_CATEGORY_ID")
	private Long catalogCategoryId;
	@Column(name="STOCK_TYPE")
	private String stockType;
	@Column(name="VENDOR_NAME")
	private String vendorName;
	@Column(name="CATALOG_UNIT_ID")
	private Long catalogUnitId;
	@Column(name="UNIT_PRICE")
	private Long unitPrice;
	@Column(name="SALES_TAX_TYPE")
	private String salesTaxType;
	@Column(name="MAKER_NAME")
	private String makerName;
	@Column(name="MAKER_MODEL_NUMBER")
	private String makerModelNumber;
	@Column(name="REMARKS")
	private String remarks;

	public MwmCatalog() {
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

	public String getCatalogCode() {
		return catalogCode;
	}
	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}

	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public Long getCatalogCategoryId() {
		return catalogCategoryId;
	}
	public void setCatalogCategoryId(Long catalogCategoryId) {
		this.catalogCategoryId = catalogCategoryId;
	}

	public String getStockType() {
		return stockType;
	}
	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Long getCatalogUnitId() {
		return catalogUnitId;
	}
	public void setCatalogUnitId(Long catalogUnitId) {
		this.catalogUnitId = catalogUnitId;
	}

	public Long getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Long unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getSalesTaxType() {
		return salesTaxType;
	}
	public void setSalesTaxType(String salesTaxType) {
		this.salesTaxType = salesTaxType;
	}

	public String getMakerName() {
		return makerName;
	}
	public void setMakerName(String makerName) {
		this.makerName = makerName;
	}

	public String getMakerModelNumber() {
		return makerModelNumber;
	}
	public void setMakerModelNumber(String makerModelNumber) {
		this.makerModelNumber = makerModelNumber;
	}

	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}