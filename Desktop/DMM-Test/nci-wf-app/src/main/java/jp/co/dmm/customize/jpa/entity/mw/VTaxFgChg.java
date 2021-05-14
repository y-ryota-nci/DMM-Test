package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the V_TAX_FG_CHG database table.
 *
 */
@Entity
@Table(name="V_TAX_FG_CHG")
@NamedQuery(name="VTaxFgChg.findAll", query="SELECT v FROM VTaxFgChg v")
public class VTaxFgChg extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="LOCALE_CODE")
	private String localeCode;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="TAX_CD")
	private String taxCd;

	@Column(name="TAX_FG_CHG")
	private String taxFgChg;

	@Column(name="TAX_FG_CHG_NM")
	private String taxFgChgNm;

	@Column(name="TAX_KND_CD")
	private String taxKndCd;

	@Column(name="TAX_NM")
	private String taxNm;

	@Column(name="TAX_RTO")
	private BigDecimal taxRto;

	@Column(name="TAX_SBJ_TP")
	private String taxSbjTp;

	@Column(name="TAX_SPC")
	private String taxSpc;

	@Column(name="TAX_TP")
	private String taxTp;

	@Temporal(TemporalType.DATE)
	@Column(name="VD_DT_E")
	private Date vdDtE;

	@Temporal(TemporalType.DATE)
	@Column(name="VD_DT_S", insertable=false)
	private Date vdDtS;

	public VTaxFgChg() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompanyCd() {
		return this.companyCd;
	}

	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}

	public String getLocaleCode() {
		return this.localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getTaxCd() {
		return this.taxCd;
	}

	public void setTaxCd(String taxCd) {
		this.taxCd = taxCd;
	}

	public String getTaxFgChg() {
		return this.taxFgChg;
	}

	public void setTaxFgChg(String taxFgChg) {
		this.taxFgChg = taxFgChg;
	}

	public String getTaxFgChgNm() {
		return this.taxFgChgNm;
	}

	public void setTaxFgChgNm(String taxFgChgNm) {
		this.taxFgChgNm = taxFgChgNm;
	}

	public String getTaxKndCd() {
		return this.taxKndCd;
	}

	public void setTaxKndCd(String taxKndCd) {
		this.taxKndCd = taxKndCd;
	}

	public String getTaxNm() {
		return this.taxNm;
	}

	public void setTaxNm(String taxNm) {
		this.taxNm = taxNm;
	}

	public BigDecimal getTaxRto() {
		return this.taxRto;
	}

	public void setTaxRto(BigDecimal taxRto) {
		this.taxRto = taxRto;
	}

	public String getTaxSbjTp() {
		return this.taxSbjTp;
	}

	public void setTaxSbjTp(String taxSbjTp) {
		this.taxSbjTp = taxSbjTp;
	}

	public String getTaxSpc() {
		return this.taxSpc;
	}

	public void setTaxSpc(String taxSpc) {
		this.taxSpc = taxSpc;
	}

	public String getTaxTp() {
		return this.taxTp;
	}

	public void setTaxTp(String taxTp) {
		this.taxTp = taxTp;
	}

	public Date getVdDtE() {
		return this.vdDtE;
	}

	public void setVdDtE(Date vdDtE) {
		this.vdDtE = vdDtE;
	}

	public Date getVdDtS() {
		return this.vdDtS;
	}

	public void setVdDtS(Date vdDtS) {
		this.vdDtS = vdDtS;
	}

}