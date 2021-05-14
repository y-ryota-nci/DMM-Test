package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the PAY_INF database table.
 * 
 */
@Entity
@Table(name="PAY_INF")
@NamedQuery(name="PayInf.findAll", query="SELECT p FROM PayInf p")
public class PayInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PayInfPK id;

	@Column(name="ADD_RTO")
	private BigDecimal addRto;

	@Column(name="ADVCST_MRK")
	private String advcstMrk;

	@Column(name="ADVPAY_TP")
	private String advpayTp;

	@Column(name="ANLYS_CD")
	private String anlysCd;

	@Column(name="ASST_TP")
	private String asstTp;

	@Column(name="BKBND_CHG_TP")
	private String bkbndChgTp;

	@Column(name="BNKACC_CD")
	private String bnkaccCd;

	@Column(name="BUMON_CD")
	private String bumonCd;

	@Column(name="CHRG_BNKACC_CD")
	private String chrgBnkaccCd;

	@Column(name="CNTRCT_NO")
	private String cntrctNo;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CRP_PRS_TP")
	private String crpPrsTp;

	@Column(name="CST_ADD_YM")
	private String cstAddYm;

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

	@Column(name="INVSHT_TP")
	private String invshtTp;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="LOT_NO")
	private String lotNo;

	@Column(name="MAIL_RMK")
	private String mailRmk;

	@Column(name="MAIL_TP")
	private String mailTp;

	@Column(name="MNY_CD")
	private String mnyCd;

	@Column(name="MNY_TP")
	private String mnyTp;

	private String msg;

	@Column(name="ORGNZ_CD")
	private String orgnzCd;

	@Column(name="PAY_AMT_EXCTAX")
	private BigDecimal payAmtExctax;

	@Column(name="PAY_AMT_FC_EXCTAX")
	private BigDecimal payAmtFcExctax;

	@Column(name="PAY_AMT_FC_INCTAX")
	private BigDecimal payAmtFcInctax;

	@Column(name="PAY_AMT_INCTAX")
	private BigDecimal payAmtInctax;

	@Column(name="PAY_APPL_CD")
	private String payApplCd;

	@Column(name="PAY_APPL_TP")
	private String payApplTp;

	@Column(name="PAY_CMM_OBL_TP")
	private String payCmmOblTp;

	@Column(name="PAY_COND_CD")
	private String payCondCd;

	@Column(name="PAY_COND_CHGRSN")
	private String payCondChgrsn;

	@Column(name="PAY_COND_NM")
	private String payCondNm;

	@Column(name="PAY_MTH")
	private String payMth;

	@Column(name="PAY_NM")
	private String payNm;

	@Temporal(TemporalType.DATE)
	@Column(name="PAY_PLN_DT")
	private Date payPlnDt;

	@Temporal(TemporalType.DATE)
	@Column(name="PAY_RQST_DT")
	private Date payRqstDt;

	@Temporal(TemporalType.DATE)
	@Column(name="PAY_RSLT_DT")
	private Date payRsltDt;

	@Column(name="PAY_SITE_CD")
	private String paySiteCd;

	@Column(name="PAY_STS")
	private String paySts;

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

	@Column(name="PAYHYS_NO_B_RCNT")
	private String payhysNoBRcnt;

	@Column(name="PAYHYS_NO_R_RCNT")
	private String payhysNoRRcnt;

	@Column(name="PRD_PURORD_NO")
	private BigDecimal prdPurordNo;

	@Column(name="PRD_PURORD_TP")
	private String prdPurordTp;

	@Column(name="PUR_TP1")
	private String purTp1;

	@Column(name="PUR_TP2")
	private String purTp2;

	@Column(name="PURORD_TP")
	private String purordTp;

	@Temporal(TemporalType.DATE)
	@Column(name="RCVINSP_USE_YM_E")
	private Date rcvinspUseYmE;

	@Temporal(TemporalType.DATE)
	@Column(name="RCVINSP_USE_YM_S")
	private Date rcvinspUseYmS;

	@Column(name="RCVINSP_YM")
	private String rcvinspYm;

	@Column(name="RCVINSP_YM_E")
	private String rcvinspYmE;

	@Column(name="RCVINSP_YM_S")
	private String rcvinspYmS;

	@Column(name="RTN_PAY_NO")
	private BigDecimal rtnPayNo;

	@Column(name="SBMT_DPT_CD")
	private String sbmtDptCd;

	@Temporal(TemporalType.DATE)
	@Column(name="SBMT_DPT_DT")
	private Date sbmtDptDt;

	@Column(name="SBMT_DPT_NM")
	private String sbmtDptNm;

	@Column(name="SBMTR_CD")
	private String sbmtrCd;

	@Column(name="SHT_ADR_PRF")
	private String shtAdrPrf;

	@Column(name="SHT_ADR_PRF_CD")
	private String shtAdrPrfCd;

	@Column(name="SHT_ADR1")
	private String shtAdr1;

	@Column(name="SHT_ADR2")
	private String shtAdr2;

	@Column(name="SHT_ADR3")
	private String shtAdr3;

	@Column(name="SHT_DPT_NM")
	private String shtDptNm;

	@Column(name="SHT_MLADR")
	private String shtMladr;

	@Column(name="SHT_PIC_NM")
	private String shtPicNm;

	@Column(name="SHT_TEL_NO")
	private String shtTelNo;

	@Column(name="SHT_TO_ZIP_CD")
	private String shtToZipCd;

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

	public PayInf() {
	}

	public PayInfPK getId() {
		return this.id;
	}

	public void setId(PayInfPK id) {
		this.id = id;
	}

	public BigDecimal getAddRto() {
		return this.addRto;
	}

	public void setAddRto(BigDecimal addRto) {
		this.addRto = addRto;
	}

	public String getAdvcstMrk() {
		return this.advcstMrk;
	}

	public void setAdvcstMrk(String advcstMrk) {
		this.advcstMrk = advcstMrk;
	}

	public String getAdvpayTp() {
		return this.advpayTp;
	}

	public void setAdvpayTp(String advpayTp) {
		this.advpayTp = advpayTp;
	}

	public String getAnlysCd() {
		return this.anlysCd;
	}

	public void setAnlysCd(String anlysCd) {
		this.anlysCd = anlysCd;
	}

	public String getAsstTp() {
		return this.asstTp;
	}

	public void setAsstTp(String asstTp) {
		this.asstTp = asstTp;
	}

	public String getBkbndChgTp() {
		return this.bkbndChgTp;
	}

	public void setBkbndChgTp(String bkbndChgTp) {
		this.bkbndChgTp = bkbndChgTp;
	}

	public String getBnkaccCd() {
		return this.bnkaccCd;
	}

	public void setBnkaccCd(String bnkaccCd) {
		this.bnkaccCd = bnkaccCd;
	}

	public String getBumonCd() {
		return this.bumonCd;
	}

	public void setBumonCd(String bumonCd) {
		this.bumonCd = bumonCd;
	}

	public String getChrgBnkaccCd() {
		return this.chrgBnkaccCd;
	}

	public void setChrgBnkaccCd(String chrgBnkaccCd) {
		this.chrgBnkaccCd = chrgBnkaccCd;
	}

	public String getCntrctNo() {
		return this.cntrctNo;
	}

	public void setCntrctNo(String cntrctNo) {
		this.cntrctNo = cntrctNo;
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

	public String getCstAddYm() {
		return this.cstAddYm;
	}

	public void setCstAddYm(String cstAddYm) {
		this.cstAddYm = cstAddYm;
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

	public String getInvshtTp() {
		return this.invshtTp;
	}

	public void setInvshtTp(String invshtTp) {
		this.invshtTp = invshtTp;
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

	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getMailRmk() {
		return this.mailRmk;
	}

	public void setMailRmk(String mailRmk) {
		this.mailRmk = mailRmk;
	}

	public String getMailTp() {
		return this.mailTp;
	}

	public void setMailTp(String mailTp) {
		this.mailTp = mailTp;
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

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getOrgnzCd() {
		return this.orgnzCd;
	}

	public void setOrgnzCd(String orgnzCd) {
		this.orgnzCd = orgnzCd;
	}

	public BigDecimal getPayAmtExctax() {
		return this.payAmtExctax;
	}

	public void setPayAmtExctax(BigDecimal payAmtExctax) {
		this.payAmtExctax = payAmtExctax;
	}

	public BigDecimal getPayAmtFcExctax() {
		return this.payAmtFcExctax;
	}

	public void setPayAmtFcExctax(BigDecimal payAmtFcExctax) {
		this.payAmtFcExctax = payAmtFcExctax;
	}

	public BigDecimal getPayAmtFcInctax() {
		return this.payAmtFcInctax;
	}

	public void setPayAmtFcInctax(BigDecimal payAmtFcInctax) {
		this.payAmtFcInctax = payAmtFcInctax;
	}

	public BigDecimal getPayAmtInctax() {
		return this.payAmtInctax;
	}

	public void setPayAmtInctax(BigDecimal payAmtInctax) {
		this.payAmtInctax = payAmtInctax;
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

	public String getPayCondCd() {
		return this.payCondCd;
	}

	public void setPayCondCd(String payCondCd) {
		this.payCondCd = payCondCd;
	}

	public String getPayCondChgrsn() {
		return this.payCondChgrsn;
	}

	public void setPayCondChgrsn(String payCondChgrsn) {
		this.payCondChgrsn = payCondChgrsn;
	}

	public String getPayCondNm() {
		return this.payCondNm;
	}

	public void setPayCondNm(String payCondNm) {
		this.payCondNm = payCondNm;
	}

	public String getPayMth() {
		return this.payMth;
	}

	public void setPayMth(String payMth) {
		this.payMth = payMth;
	}

	public String getPayNm() {
		return this.payNm;
	}

	public void setPayNm(String payNm) {
		this.payNm = payNm;
	}

	public Date getPayPlnDt() {
		return this.payPlnDt;
	}

	public void setPayPlnDt(Date payPlnDt) {
		this.payPlnDt = payPlnDt;
	}

	public Date getPayRqstDt() {
		return this.payRqstDt;
	}

	public void setPayRqstDt(Date payRqstDt) {
		this.payRqstDt = payRqstDt;
	}

	public Date getPayRsltDt() {
		return this.payRsltDt;
	}

	public void setPayRsltDt(Date payRsltDt) {
		this.payRsltDt = payRsltDt;
	}

	public String getPaySiteCd() {
		return this.paySiteCd;
	}

	public void setPaySiteCd(String paySiteCd) {
		this.paySiteCd = paySiteCd;
	}

	public String getPaySts() {
		return this.paySts;
	}

	public void setPaySts(String paySts) {
		this.paySts = paySts;
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

	public String getPayhysNoBRcnt() {
		return this.payhysNoBRcnt;
	}

	public void setPayhysNoBRcnt(String payhysNoBRcnt) {
		this.payhysNoBRcnt = payhysNoBRcnt;
	}

	public String getPayhysNoRRcnt() {
		return this.payhysNoRRcnt;
	}

	public void setPayhysNoRRcnt(String payhysNoRRcnt) {
		this.payhysNoRRcnt = payhysNoRRcnt;
	}

	public BigDecimal getPrdPurordNo() {
		return this.prdPurordNo;
	}

	public void setPrdPurordNo(BigDecimal prdPurordNo) {
		this.prdPurordNo = prdPurordNo;
	}

	public String getPrdPurordTp() {
		return this.prdPurordTp;
	}

	public void setPrdPurordTp(String prdPurordTp) {
		this.prdPurordTp = prdPurordTp;
	}

	public String getPurTp1() {
		return this.purTp1;
	}

	public void setPurTp1(String purTp1) {
		this.purTp1 = purTp1;
	}

	public String getPurTp2() {
		return this.purTp2;
	}

	public void setPurTp2(String purTp2) {
		this.purTp2 = purTp2;
	}

	public String getPurordTp() {
		return this.purordTp;
	}

	public void setPurordTp(String purordTp) {
		this.purordTp = purordTp;
	}

	public Date getRcvinspUseYmE() {
		return this.rcvinspUseYmE;
	}

	public void setRcvinspUseYmE(Date rcvinspUseYmE) {
		this.rcvinspUseYmE = rcvinspUseYmE;
	}

	public Date getRcvinspUseYmS() {
		return this.rcvinspUseYmS;
	}

	public void setRcvinspUseYmS(Date rcvinspUseYmS) {
		this.rcvinspUseYmS = rcvinspUseYmS;
	}

	public String getRcvinspYm() {
		return this.rcvinspYm;
	}

	public void setRcvinspYm(String rcvinspYm) {
		this.rcvinspYm = rcvinspYm;
	}

	public String getRcvinspYmE() {
		return this.rcvinspYmE;
	}

	public void setRcvinspYmE(String rcvinspYmE) {
		this.rcvinspYmE = rcvinspYmE;
	}

	public String getRcvinspYmS() {
		return this.rcvinspYmS;
	}

	public void setRcvinspYmS(String rcvinspYmS) {
		this.rcvinspYmS = rcvinspYmS;
	}

	public BigDecimal getRtnPayNo() {
		return this.rtnPayNo;
	}

	public void setRtnPayNo(BigDecimal rtnPayNo) {
		this.rtnPayNo = rtnPayNo;
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

	public String getShtAdrPrf() {
		return this.shtAdrPrf;
	}

	public void setShtAdrPrf(String shtAdrPrf) {
		this.shtAdrPrf = shtAdrPrf;
	}

	public String getShtAdrPrfCd() {
		return this.shtAdrPrfCd;
	}

	public void setShtAdrPrfCd(String shtAdrPrfCd) {
		this.shtAdrPrfCd = shtAdrPrfCd;
	}

	public String getShtAdr1() {
		return this.shtAdr1;
	}

	public void setShtAdr1(String shtAdr1) {
		this.shtAdr1 = shtAdr1;
	}

	public String getShtAdr2() {
		return this.shtAdr2;
	}

	public void setShtAdr2(String shtAdr2) {
		this.shtAdr2 = shtAdr2;
	}

	public String getShtAdr3() {
		return this.shtAdr3;
	}

	public void setShtAdr3(String shtAdr3) {
		this.shtAdr3 = shtAdr3;
	}

	public String getShtDptNm() {
		return this.shtDptNm;
	}

	public void setShtDptNm(String shtDptNm) {
		this.shtDptNm = shtDptNm;
	}

	public String getShtMladr() {
		return this.shtMladr;
	}

	public void setShtMladr(String shtMladr) {
		this.shtMladr = shtMladr;
	}

	public String getShtPicNm() {
		return this.shtPicNm;
	}

	public void setShtPicNm(String shtPicNm) {
		this.shtPicNm = shtPicNm;
	}

	public String getShtTelNo() {
		return this.shtTelNo;
	}

	public void setShtTelNo(String shtTelNo) {
		this.shtTelNo = shtTelNo;
	}

	public String getShtToZipCd() {
		return this.shtToZipCd;
	}

	public void setShtToZipCd(String shtToZipCd) {
		this.shtToZipCd = shtToZipCd;
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