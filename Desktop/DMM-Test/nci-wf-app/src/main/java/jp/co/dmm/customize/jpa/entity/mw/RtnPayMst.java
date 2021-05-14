package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the RTN_PAY_MST database table.
 * 
 */
@Entity
@Table(name="RTN_PAY_MST")
@NamedQuery(name="RtnPayMst.findAll", query="SELECT r FROM RtnPayMst r")
public class RtnPayMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtnPayMstPK id;

	@Column(name="ADD_RTO")
	private BigDecimal addRto;

	@Column(name="ADVPAY_TP")
	private String advpayTp;

	@Column(name="BNKACC_CD")
	private String bnkaccCd;

	@Column(name="CHRG_BNKACC_CD")
	private String chrgBnkaccCd;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CRP_PRS_TP")
	private String crpPrsTp;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="DMS_ABR_TP")
	private String dmsAbrTp;

	@Column(name="HLDTAX_AMT")
	private BigDecimal hldtaxAmt;

	@Column(name="HLDTAX_RTO")
	private BigDecimal hldtaxRto;

	@Column(name="HLDTAX_SBJ_AMT")
	private BigDecimal hldtaxSbjAmt;

	@Column(name="HLDTAX_TP")
	private String hldtaxTp;

	@Column(name="IMP_TAX_TP")
	private String impTaxTp;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="ML_ADD_TP")
	private String mlAddTp;

	@Column(name="MNY_CD")
	private String mnyCd;

	@Column(name="MNY_TP")
	private String mnyTp;

	@Column(name="ORGNZ_CD")
	private String orgnzCd;

	@Column(name="PAY_APPL_CD")
	private String payApplCd;

	@Column(name="PAY_APPL_TP")
	private String payApplTp;

	@Column(name="PAY_CMM_OBL_TP")
	private String payCmmOblTp;

	@Column(name="PAY_END_TIME")
	private String payEndTime;

	@Column(name="PAY_SITE_CD")
	private String paySiteCd;

	@Column(name="PAY_START_TIME")
	private String payStartTime;

	@Column(name="PAYEE_BNK_CD")
	private String payeeBnkCd;

	@Column(name="PAYEE_BNKACC_CD")
	private String payeeBnkaccCd;

	@Column(name="PAYEE_BNKACC_NM")
	private String payeeBnkaccNm;

	@Column(name="PAYEE_BNKACC_NM_KN")
	private String payeeBnkaccNmKn;

	@Column(name="PAYEE_BNKACC_NO")
	private String payeeBnkaccNo;

	@Column(name="PAYEE_BNKACC_TP")
	private String payeeBnkaccTp;

	@Column(name="PAYEE_BNKBRC_CD")
	private String payeeBnkbrcCd;

	@Column(name="RTN_PAY_AMT_EXCTAX")
	private BigDecimal rtnPayAmtExctax;

	@Column(name="RTN_PAY_AMT_FC")
	private BigDecimal rtnPayAmtFc;

	@Column(name="RTN_PAY_AMT_FC_EXCTAX")
	private BigDecimal rtnPayAmtFcExctax;

	@Column(name="RTN_PAY_AMT_FC_INCTAX")
	private BigDecimal rtnPayAmtFcInctax;

	@Column(name="RTN_PAY_AMT_INCTAX")
	private BigDecimal rtnPayAmtInctax;

	@Column(name="RTN_PAY_AMT_JPY")
	private BigDecimal rtnPayAmtJpy;

	@Column(name="RTN_PAY_DAY")
	private String rtnPayDay;

	@Column(name="RTN_PAY_MTH")
	private String rtnPayMth;

	@Column(name="RTN_PAY_SITE")
	private String rtnPaySite;

	@Column(name="RTN_PAY_SITE_N")
	private BigDecimal rtnPaySiteN;

	@Column(name="RTN_PAY_TP")
	private String rtnPayTp;

	@Column(name="SBMT_DPT_CD")
	private String sbmtDptCd;

	@Column(name="SBMT_DPT_NM")
	private String sbmtDptNm;

	@Column(name="SBMTR_CD")
	private String sbmtrCd;

	@Column(name="SPLR_CD")
	private String splrCd;

	@Column(name="SPLR_NM_KJ")
	private String splrNmKj;

	@Column(name="SPLR_NM_KN")
	private String splrNmKn;

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

	public RtnPayMst() {
	}

	public RtnPayMstPK getId() {
		return this.id;
	}

	public void setId(RtnPayMstPK id) {
		this.id = id;
	}

	public BigDecimal getAddRto() {
		return this.addRto;
	}

	public void setAddRto(BigDecimal addRto) {
		this.addRto = addRto;
	}

	public String getAdvpayTp() {
		return this.advpayTp;
	}

	public void setAdvpayTp(String advpayTp) {
		this.advpayTp = advpayTp;
	}

	public String getBnkaccCd() {
		return this.bnkaccCd;
	}

	public void setBnkaccCd(String bnkaccCd) {
		this.bnkaccCd = bnkaccCd;
	}

	public String getChrgBnkaccCd() {
		return this.chrgBnkaccCd;
	}

	public void setChrgBnkaccCd(String chrgBnkaccCd) {
		this.chrgBnkaccCd = chrgBnkaccCd;
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

	public String getCrpPrsTp() {
		return this.crpPrsTp;
	}

	public void setCrpPrsTp(String crpPrsTp) {
		this.crpPrsTp = crpPrsTp;
	}

	public String getDltFg() {
		return this.dltFg;
	}

	public void setDltFg(String dltFg) {
		this.dltFg = dltFg;
	}

	public String getDmsAbrTp() {
		return this.dmsAbrTp;
	}

	public void setDmsAbrTp(String dmsAbrTp) {
		this.dmsAbrTp = dmsAbrTp;
	}

	public BigDecimal getHldtaxAmt() {
		return this.hldtaxAmt;
	}

	public void setHldtaxAmt(BigDecimal hldtaxAmt) {
		this.hldtaxAmt = hldtaxAmt;
	}

	public BigDecimal getHldtaxRto() {
		return this.hldtaxRto;
	}

	public void setHldtaxRto(BigDecimal hldtaxRto) {
		this.hldtaxRto = hldtaxRto;
	}

	public BigDecimal getHldtaxSbjAmt() {
		return this.hldtaxSbjAmt;
	}

	public void setHldtaxSbjAmt(BigDecimal hldtaxSbjAmt) {
		this.hldtaxSbjAmt = hldtaxSbjAmt;
	}

	public String getHldtaxTp() {
		return this.hldtaxTp;
	}

	public void setHldtaxTp(String hldtaxTp) {
		this.hldtaxTp = hldtaxTp;
	}

	public String getImpTaxTp() {
		return this.impTaxTp;
	}

	public void setImpTaxTp(String impTaxTp) {
		this.impTaxTp = impTaxTp;
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

	public String getMlAddTp() {
		return this.mlAddTp;
	}

	public void setMlAddTp(String mlAddTp) {
		this.mlAddTp = mlAddTp;
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

	public String getPayApplCd() {
		return this.payApplCd;
	}

	public void setPayApplCd(String payApplCd) {
		this.payApplCd = payApplCd;
	}

	public String getPayApplTp() {
		return this.payApplTp;
	}

	public void setPayApplTp(String payApplTp) {
		this.payApplTp = payApplTp;
	}

	public String getPayCmmOblTp() {
		return this.payCmmOblTp;
	}

	public void setPayCmmOblTp(String payCmmOblTp) {
		this.payCmmOblTp = payCmmOblTp;
	}

	public String getPayEndTime() {
		return this.payEndTime;
	}

	public void setPayEndTime(String payEndTime) {
		this.payEndTime = payEndTime;
	}

	public String getPaySiteCd() {
		return this.paySiteCd;
	}

	public void setPaySiteCd(String paySiteCd) {
		this.paySiteCd = paySiteCd;
	}

	public String getPayStartTime() {
		return this.payStartTime;
	}

	public void setPayStartTime(String payStartTime) {
		this.payStartTime = payStartTime;
	}

	public String getPayeeBnkCd() {
		return this.payeeBnkCd;
	}

	public void setPayeeBnkCd(String payeeBnkCd) {
		this.payeeBnkCd = payeeBnkCd;
	}

	public String getPayeeBnkaccCd() {
		return this.payeeBnkaccCd;
	}

	public void setPayeeBnkaccCd(String payeeBnkaccCd) {
		this.payeeBnkaccCd = payeeBnkaccCd;
	}

	public String getPayeeBnkaccNm() {
		return this.payeeBnkaccNm;
	}

	public void setPayeeBnkaccNm(String payeeBnkaccNm) {
		this.payeeBnkaccNm = payeeBnkaccNm;
	}

	public String getPayeeBnkaccNmKn() {
		return this.payeeBnkaccNmKn;
	}

	public void setPayeeBnkaccNmKn(String payeeBnkaccNmKn) {
		this.payeeBnkaccNmKn = payeeBnkaccNmKn;
	}

	public String getPayeeBnkaccNo() {
		return this.payeeBnkaccNo;
	}

	public void setPayeeBnkaccNo(String payeeBnkaccNo) {
		this.payeeBnkaccNo = payeeBnkaccNo;
	}

	public String getPayeeBnkaccTp() {
		return this.payeeBnkaccTp;
	}

	public void setPayeeBnkaccTp(String payeeBnkaccTp) {
		this.payeeBnkaccTp = payeeBnkaccTp;
	}

	public String getPayeeBnkbrcCd() {
		return this.payeeBnkbrcCd;
	}

	public void setPayeeBnkbrcCd(String payeeBnkbrcCd) {
		this.payeeBnkbrcCd = payeeBnkbrcCd;
	}

	public BigDecimal getRtnPayAmtExctax() {
		return this.rtnPayAmtExctax;
	}

	public void setRtnPayAmtExctax(BigDecimal rtnPayAmtExctax) {
		this.rtnPayAmtExctax = rtnPayAmtExctax;
	}

	public BigDecimal getRtnPayAmtFc() {
		return this.rtnPayAmtFc;
	}

	public void setRtnPayAmtFc(BigDecimal rtnPayAmtFc) {
		this.rtnPayAmtFc = rtnPayAmtFc;
	}

	public BigDecimal getRtnPayAmtFcExctax() {
		return this.rtnPayAmtFcExctax;
	}

	public void setRtnPayAmtFcExctax(BigDecimal rtnPayAmtFcExctax) {
		this.rtnPayAmtFcExctax = rtnPayAmtFcExctax;
	}

	public BigDecimal getRtnPayAmtFcInctax() {
		return this.rtnPayAmtFcInctax;
	}

	public void setRtnPayAmtFcInctax(BigDecimal rtnPayAmtFcInctax) {
		this.rtnPayAmtFcInctax = rtnPayAmtFcInctax;
	}

	public BigDecimal getRtnPayAmtInctax() {
		return this.rtnPayAmtInctax;
	}

	public void setRtnPayAmtInctax(BigDecimal rtnPayAmtInctax) {
		this.rtnPayAmtInctax = rtnPayAmtInctax;
	}

	public BigDecimal getRtnPayAmtJpy() {
		return this.rtnPayAmtJpy;
	}

	public void setRtnPayAmtJpy(BigDecimal rtnPayAmtJpy) {
		this.rtnPayAmtJpy = rtnPayAmtJpy;
	}

	public String getRtnPayDay() {
		return this.rtnPayDay;
	}

	public void setRtnPayDay(String rtnPayDay) {
		this.rtnPayDay = rtnPayDay;
	}

	public String getRtnPayMth() {
		return this.rtnPayMth;
	}

	public void setRtnPayMth(String rtnPayMth) {
		this.rtnPayMth = rtnPayMth;
	}

	public String getRtnPaySite() {
		return this.rtnPaySite;
	}

	public void setRtnPaySite(String rtnPaySite) {
		this.rtnPaySite = rtnPaySite;
	}

	public BigDecimal getRtnPaySiteN() {
		return this.rtnPaySiteN;
	}

	public void setRtnPaySiteN(BigDecimal rtnPaySiteN) {
		this.rtnPaySiteN = rtnPaySiteN;
	}

	public String getRtnPayTp() {
		return this.rtnPayTp;
	}

	public void setRtnPayTp(String rtnPayTp) {
		this.rtnPayTp = rtnPayTp;
	}

	public String getSbmtDptCd() {
		return this.sbmtDptCd;
	}

	public void setSbmtDptCd(String sbmtDptCd) {
		this.sbmtDptCd = sbmtDptCd;
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

	public String getSplrCd() {
		return this.splrCd;
	}

	public void setSplrCd(String splrCd) {
		this.splrCd = splrCd;
	}

	public String getSplrNmKj() {
		return this.splrNmKj;
	}

	public void setSplrNmKj(String splrNmKj) {
		this.splrNmKj = splrNmKj;
	}

	public String getSplrNmKn() {
		return this.splrNmKn;
	}

	public void setSplrNmKn(String splrNmKn) {
		this.splrNmKn = splrNmKn;
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