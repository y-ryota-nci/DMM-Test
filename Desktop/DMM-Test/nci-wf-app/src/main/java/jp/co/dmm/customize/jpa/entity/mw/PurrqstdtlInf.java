package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the PURRQSTDTL_INF database table.
 * 
 */
@Entity
@Table(name="PURRQSTDTL_INF")
@NamedQuery(name="PurrqstdtlInf.findAll", query="SELECT p FROM PurrqstdtlInf p")
public class PurrqstdtlInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PurrqstdtlInfPK id;

	@Column(name="BUMON_CD")
	private String bumonCd;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="ITM_CD")
	private String itmCd;

	@Column(name="ITM_NM")
	private String itmNm;

	@Column(name="MAKER_MDL_NO")
	private String makerMdlNo;

	@Column(name="MAKER_NM")
	private String makerNm;

	@Column(name="MNY_CD")
	private String mnyCd;

	@Column(name="PURRQST_ABST")
	private String purrqstAbst;

	@Column(name="PURRQST_AMT_FC")
	private BigDecimal purrqstAmtFc;

	@Column(name="PURRQST_AMT_JPY")
	private BigDecimal purrqstAmtJpy;

	@Column(name="PURRQST_AMT_JPY_INCTAX")
	private BigDecimal purrqstAmtJpyInctax;

	@Column(name="PURRQST_QNT")
	private BigDecimal purrqstQnt;

	@Column(name="PURRQST_TP")
	private String purrqstTp;

	@Column(name="PURRQST_UC_FC")
	private BigDecimal purrqstUcFc;

	@Column(name="PURRQST_UC_JPY")
	private BigDecimal purrqstUcJpy;

	@Column(name="TAX_CD")
	private String taxCd;

	@Column(name="TAX_FG")
	private String taxFg;

	@Column(name="TAX_FG_CHG")
	private String taxFgChg;

	@Column(name="TAX_KND_CD")
	private String taxKndCd;

	@Column(name="TAX_SBJ_TP")
	private String taxSbjTp;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public PurrqstdtlInf() {
	}

	public PurrqstdtlInfPK getId() {
		return this.id;
	}

	public void setId(PurrqstdtlInfPK id) {
		this.id = id;
	}

	public String getBumonCd() {
		return this.bumonCd;
	}

	public void setBumonCd(String bumonCd) {
		this.bumonCd = bumonCd;
	}

	public String getCorporationCodeCreated() {
		return this.corporationCodeCreated;
	}

	public void setCorporationCodeCreated(String corporationCodeCreated) {
		this.corporationCodeCreated = corporationCodeCreated;
	}

	public String getCorporationCodeUpdated() {
		return this.corporationCodeUpdated;
	}

	public void setCorporationCodeUpdated(String corporationCodeUpdated) {
		this.corporationCodeUpdated = corporationCodeUpdated;
	}

	public String getDltFg() {
		return this.dltFg;
	}

	public void setDltFg(String dltFg) {
		this.dltFg = dltFg;
	}

	public String getIpCreated() {
		return this.ipCreated;
	}

	public void setIpCreated(String ipCreated) {
		this.ipCreated = ipCreated;
	}

	public String getIpUpdated() {
		return this.ipUpdated;
	}

	public void setIpUpdated(String ipUpdated) {
		this.ipUpdated = ipUpdated;
	}

	public String getItmCd() {
		return this.itmCd;
	}

	public void setItmCd(String itmCd) {
		this.itmCd = itmCd;
	}

	public String getItmNm() {
		return this.itmNm;
	}

	public void setItmNm(String itmNm) {
		this.itmNm = itmNm;
	}

	public String getMakerMdlNo() {
		return this.makerMdlNo;
	}

	public void setMakerMdlNo(String makerMdlNo) {
		this.makerMdlNo = makerMdlNo;
	}

	public String getMakerNm() {
		return this.makerNm;
	}

	public void setMakerNm(String makerNm) {
		this.makerNm = makerNm;
	}

	public String getMnyCd() {
		return this.mnyCd;
	}

	public void setMnyCd(String mnyCd) {
		this.mnyCd = mnyCd;
	}

	public String getPurrqstAbst() {
		return this.purrqstAbst;
	}

	public void setPurrqstAbst(String purrqstAbst) {
		this.purrqstAbst = purrqstAbst;
	}

	public BigDecimal getPurrqstAmtFc() {
		return this.purrqstAmtFc;
	}

	public void setPurrqstAmtFc(BigDecimal purrqstAmtFc) {
		this.purrqstAmtFc = purrqstAmtFc;
	}

	public BigDecimal getPurrqstAmtJpy() {
		return this.purrqstAmtJpy;
	}

	public void setPurrqstAmtJpy(BigDecimal purrqstAmtJpy) {
		this.purrqstAmtJpy = purrqstAmtJpy;
	}

	public BigDecimal getPurrqstAmtJpyInctax() {
		return this.purrqstAmtJpyInctax;
	}

	public void setPurrqstAmtJpyInctax(BigDecimal purrqstAmtJpyInctax) {
		this.purrqstAmtJpyInctax = purrqstAmtJpyInctax;
	}

	public BigDecimal getPurrqstQnt() {
		return this.purrqstQnt;
	}

	public void setPurrqstQnt(BigDecimal purrqstQnt) {
		this.purrqstQnt = purrqstQnt;
	}

	public String getPurrqstTp() {
		return this.purrqstTp;
	}

	public void setPurrqstTp(String purrqstTp) {
		this.purrqstTp = purrqstTp;
	}

	public BigDecimal getPurrqstUcFc() {
		return this.purrqstUcFc;
	}

	public void setPurrqstUcFc(BigDecimal purrqstUcFc) {
		this.purrqstUcFc = purrqstUcFc;
	}

	public BigDecimal getPurrqstUcJpy() {
		return this.purrqstUcJpy;
	}

	public void setPurrqstUcJpy(BigDecimal purrqstUcJpy) {
		this.purrqstUcJpy = purrqstUcJpy;
	}

	public String getTaxCd() {
		return this.taxCd;
	}

	public void setTaxCd(String taxCd) {
		this.taxCd = taxCd;
	}

	public String getTaxFg() {
		return this.taxFg;
	}

	public void setTaxFg(String taxFg) {
		this.taxFg = taxFg;
	}

	public String getTaxFgChg() {
		return this.taxFgChg;
	}

	public void setTaxFgChg(String taxFgChg) {
		this.taxFgChg = taxFgChg;
	}

	public String getTaxKndCd() {
		return this.taxKndCd;
	}

	public void setTaxKndCd(String taxKndCd) {
		this.taxKndCd = taxKndCd;
	}

	public String getTaxSbjTp() {
		return this.taxSbjTp;
	}

	public void setTaxSbjTp(String taxSbjTp) {
		this.taxSbjTp = taxSbjTp;
	}

	public Timestamp getTimestampCreated() {
		return this.timestampCreated;
	}

	public void setTimestampCreated(Timestamp timestampCreated) {
		this.timestampCreated = timestampCreated;
	}

	public Timestamp getTimestampUpdated() {
		return this.timestampUpdated;
	}

	public void setTimestampUpdated(Timestamp timestampUpdated) {
		this.timestampUpdated = timestampUpdated;
	}

	public String getUserCodeCreated() {
		return this.userCodeCreated;
	}

	public void setUserCodeCreated(String userCodeCreated) {
		this.userCodeCreated = userCodeCreated;
	}

	public String getUserCodeUpdated() {
		return this.userCodeUpdated;
	}

	public void setUserCodeUpdated(String userCodeUpdated) {
		this.userCodeUpdated = userCodeUpdated;
	}

}