package jp.co.dmm.customize.endpoint.ct.ct0020;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Ct0020Entity extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CATALOG_ID")
	public long catalogId;
	@Column(name="CORPORATION_CODE")
	public String corporationCode;
	@Column(name="CATALOG_CODE")
	public String catalogCode;
	@Column(name="CATALOG_NAME")
	public String catalogName;
	@Column(name="CATALOG_CATEGORY_ID")
	public Long catalogCategoryId;
	@Column(name="STOCK_TYPE")
	public String stockType;
	@Column(name="VENDOR_NAME")
	public String vendorName;
	@Column(name="CATALOG_UNIT_ID")
	public Long catalogUnitId;
	@Column(name="UNIT_PRICE")
	public Long unitPrice;
	@Column(name="SALES_TAX_TYPE")
	public String salesTaxType;
	@Column(name="MAKER_NAME")
	public String makerName;
	@Column(name="MAKER_MODEL_NUMBER")
	public String makerModelNumber;
	@Column(name="REMARKS")
	public String remarks;
	@Column(name="CATALOG_IMAGE_ID")
	public Long catalogImageId;
	@Column(name="CATALOG_CATEGORY_NAME")
	public String catalogCategoryName;
	@Column(name="CATALOG_UNIT_NAME")
	public String catalogUnitName;
	@Column(name="STOCK_TYPE_NAME")
	public String stockTypeName;
	@Column(name="SALES_TAX_TYPE_NAME")
	public String salesTaxTypeName;

	@Transient
	public String escapeRemarks;
}