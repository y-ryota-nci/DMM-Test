package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the PURRQST_INF database table.
 * 
 */
@Entity
@Table(name="PURRQST_INF")
@NamedQuery(name="PurrqstInf.findAll", query="SELECT p FROM PurrqstInf p")
public class PurrqstInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PurrqstInfPK id;

	@Column(name="ADD_RTO")
	private BigDecimal addRto;

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

	@Column(name="MNY_CD")
	private String mnyCd;

	@Column(name="MNY_TP")
	private String mnyTp;

	@Column(name="ORGNZ_CD")
	private String orgnzCd;

	@Column(name="PRC_FLD_TP")
	private String prcFldTp;

	@Column(name="PURRQST_AMT_EXCTAX")
	private BigDecimal purrqstAmtExctax;

	@Column(name="PURRQST_AMT_FC_EXCTAX")
	private BigDecimal purrqstAmtFcExctax;

	@Column(name="PURRQST_AMT_FC_INCTAX")
	private BigDecimal purrqstAmtFcInctax;

	@Column(name="PURRQST_AMT_INCTAX")
	private BigDecimal purrqstAmtInctax;

	@Column(name="PURRQST_NM")
	private String purrqstNm;

	@Column(name="PURRQST_STS")
	private String purrqstSts;

	@Column(name="RCP_LC_CD")
	private String rcpLcCd;

	private String rmk;

	@Column(name="SBMT_DPT_CD")
	private String sbmtDptCd;

	@Temporal(TemporalType.DATE)
	@Column(name="SBMT_DPT_DT")
	private Date sbmtDptDt;

	@Column(name="SBMT_DPT_NM")
	private String sbmtDptNm;

	@Column(name="SBMTR_CD")
	private String sbmtrCd;

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

	@Column(name="TAX_UNT")
	private String taxUnt;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="TTLAMT_EXCTAX1")
	private BigDecimal ttlamtExctax1;

	@Column(name="TTLAMT_EXCTAX2")
	private BigDecimal ttlamtExctax2;

	@Column(name="TTLAMT_EXCTAX3")
	private BigDecimal ttlamtExctax3;

	@Column(name="TTLAMT_FC_EXCTAX1")
	private BigDecimal ttlamtFcExctax1;

	@Column(name="TTLAMT_FC_EXCTAX3")
	private BigDecimal ttlamtFcExctax3;

	@Column(name="TTLAMT_FC_INCTAX1")
	private BigDecimal ttlamtFcInctax1;

	@Column(name="TTLAMT_FC_INCTAX3")
	private BigDecimal ttlamtFcInctax3;

	@Column(name="TTLAMT_FC_TAX1")
	private BigDecimal ttlamtFcTax1;

	@Column(name="TTLAMT_FC_TAX3")
	private BigDecimal ttlamtFcTax3;

	@Column(name="TTLAMT_INCTAX1")
	private BigDecimal ttlamtInctax1;

	@Column(name="TTLAMT_INCTAX2")
	private BigDecimal ttlamtInctax2;

	@Column(name="TTLAMT_INCTAX3")
	private BigDecimal ttlamtInctax3;

	@Column(name="TTLAMT_TAX1")
	private BigDecimal ttlamtTax1;

	@Column(name="TTLAMT_TAX2")
	private BigDecimal ttlamtTax2;

	@Column(name="TTLAMT_TAX3")
	private BigDecimal ttlamtTax3;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public PurrqstInf() {
	}

	public PurrqstInfPK getId() {
		return this.id;
	}

	public void setId(PurrqstInfPK id) {
		this.id = id;
	}

	public BigDecimal getAddRto() {
		return this.addRto;
	}

	public void setAddRto(BigDecimal addRto) {
		this.addRto = addRto;
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

	public String getMnyCd() {
		return this.mnyCd;
	}

	public void setMnyCd(String mnyCd) {
		this.mnyCd = mnyCd;
	}

	public String getMnyTp() {
		return this.mnyTp;
	}

	public void setMnyTp(String mnyTp) {
		this.mnyTp = mnyTp;
	}

	public String getOrgnzCd() {
		return this.orgnzCd;
	}

	public void setOrgnzCd(String orgnzCd) {
		this.orgnzCd = orgnzCd;
	}

	public String getPrcFldTp() {
		return this.prcFldTp;
	}

	public void setPrcFldTp(String prcFldTp) {
		this.prcFldTp = prcFldTp;
	}

	public BigDecimal getPurrqstAmtExctax() {
		return this.purrqstAmtExctax;
	}

	public void setPurrqstAmtExctax(BigDecimal purrqstAmtExctax) {
		this.purrqstAmtExctax = purrqstAmtExctax;
	}

	public BigDecimal getPurrqstAmtFcExctax() {
		return this.purrqstAmtFcExctax;
	}

	public void setPurrqstAmtFcExctax(BigDecimal purrqstAmtFcExctax) {
		this.purrqstAmtFcExctax = purrqstAmtFcExctax;
	}

	public BigDecimal getPurrqstAmtFcInctax() {
		return this.purrqstAmtFcInctax;
	}

	public void setPurrqstAmtFcInctax(BigDecimal purrqstAmtFcInctax) {
		this.purrqstAmtFcInctax = purrqstAmtFcInctax;
	}

	public BigDecimal getPurrqstAmtInctax() {
		return this.purrqstAmtInctax;
	}

	public void setPurrqstAmtInctax(BigDecimal purrqstAmtInctax) {
		this.purrqstAmtInctax = purrqstAmtInctax;
	}

	public String getPurrqstNm() {
		return this.purrqstNm;
	}

	public void setPurrqstNm(String purrqstNm) {
		this.purrqstNm = purrqstNm;
	}

	public String getPurrqstSts() {
		return this.purrqstSts;
	}

	public void setPurrqstSts(String purrqstSts) {
		this.purrqstSts = purrqstSts;
	}

	public String getRcpLcCd() {
		return this.rcpLcCd;
	}

	public void setRcpLcCd(String rcpLcCd) {
		this.rcpLcCd = rcpLcCd;
	}

	public String getRmk() {
		return this.rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getSbmtDptCd() {
		return this.sbmtDptCd;
	}

	public void setSbmtDptCd(String sbmtDptCd) {
		this.sbmtDptCd = sbmtDptCd;
	}

	public Date getSbmtDptDt() {
		return this.sbmtDptDt;
	}

	public void setSbmtDptDt(Date sbmtDptDt) {
		this.sbmtDptDt = sbmtDptDt;
	}

	public String getSbmtDptNm() {
		return this.sbmtDptNm;
	}

	public void setSbmtDptNm(String sbmtDptNm) {
		this.sbmtDptNm = sbmtDptNm;
	}

	public String getSbmtrCd() {
		return this.sbmtrCd;
	}

	public void setSbmtrCd(String sbmtrCd) {
		this.sbmtrCd = sbmtrCd;
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

	public String getTaxUnt() {
		return this.taxUnt;
	}

	public void setTaxUnt(String taxUnt) {
		this.taxUnt = taxUnt;
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

	public BigDecimal getTtlamtExctax1() {
		return this.ttlamtExctax1;
	}

	public void setTtlamtExctax1(BigDecimal ttlamtExctax1) {
		this.ttlamtExctax1 = ttlamtExctax1;
	}

	public BigDecimal getTtlamtExctax2() {
		return this.ttlamtExctax2;
	}

	public void setTtlamtExctax2(BigDecimal ttlamtExctax2) {
		this.ttlamtExctax2 = ttlamtExctax2;
	}

	public BigDecimal getTtlamtExctax3() {
		return this.ttlamtExctax3;
	}

	public void setTtlamtExctax3(BigDecimal ttlamtExctax3) {
		this.ttlamtExctax3 = ttlamtExctax3;
	}

	public BigDecimal getTtlamtFcExctax1() {
		return this.ttlamtFcExctax1;
	}

	public void setTtlamtFcExctax1(BigDecimal ttlamtFcExctax1) {
		this.ttlamtFcExctax1 = ttlamtFcExctax1;
	}

	public BigDecimal getTtlamtFcExctax3() {
		return this.ttlamtFcExctax3;
	}

	public void setTtlamtFcExctax3(BigDecimal ttlamtFcExctax3) {
		this.ttlamtFcExctax3 = ttlamtFcExctax3;
	}

	public BigDecimal getTtlamtFcInctax1() {
		return this.ttlamtFcInctax1;
	}

	public void setTtlamtFcInctax1(BigDecimal ttlamtFcInctax1) {
		this.ttlamtFcInctax1 = ttlamtFcInctax1;
	}

	public BigDecimal getTtlamtFcInctax3() {
		return this.ttlamtFcInctax3;
	}

	public void setTtlamtFcInctax3(BigDecimal ttlamtFcInctax3) {
		this.ttlamtFcInctax3 = ttlamtFcInctax3;
	}

	public BigDecimal getTtlamtFcTax1() {
		return this.ttlamtFcTax1;
	}

	public void setTtlamtFcTax1(BigDecimal ttlamtFcTax1) {
		this.ttlamtFcTax1 = ttlamtFcTax1;
	}

	public BigDecimal getTtlamtFcTax3() {
		return this.ttlamtFcTax3;
	}

	public void setTtlamtFcTax3(BigDecimal ttlamtFcTax3) {
		this.ttlamtFcTax3 = ttlamtFcTax3;
	}

	public BigDecimal getTtlamtInctax1() {
		return this.ttlamtInctax1;
	}

	public void setTtlamtInctax1(BigDecimal ttlamtInctax1) {
		this.ttlamtInctax1 = ttlamtInctax1;
	}

	public BigDecimal getTtlamtInctax2() {
		return this.ttlamtInctax2;
	}

	public void setTtlamtInctax2(BigDecimal ttlamtInctax2) {
		this.ttlamtInctax2 = ttlamtInctax2;
	}

	public BigDecimal getTtlamtInctax3() {
		return this.ttlamtInctax3;
	}

	public void setTtlamtInctax3(BigDecimal ttlamtInctax3) {
		this.ttlamtInctax3 = ttlamtInctax3;
	}

	public BigDecimal getTtlamtTax1() {
		return this.ttlamtTax1;
	}

	public void setTtlamtTax1(BigDecimal ttlamtTax1) {
		this.ttlamtTax1 = ttlamtTax1;
	}

	public BigDecimal getTtlamtTax2() {
		return this.ttlamtTax2;
	}

	public void setTtlamtTax2(BigDecimal ttlamtTax2) {
		this.ttlamtTax2 = ttlamtTax2;
	}

	public BigDecimal getTtlamtTax3() {
		return this.ttlamtTax3;
	}

	public void setTtlamtTax3(BigDecimal ttlamtTax3) {
		this.ttlamtTax3 = ttlamtTax3;
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