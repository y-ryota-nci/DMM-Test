package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the RCVINSP_INF database table.
 * 
 */
@Entity
@Table(name="RCVINSP_INF")
@NamedQuery(name="RcvinspInf.findAll", query="SELECT r FROM RcvinspInf r")
public class RcvinspInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RcvinspInfPK id;

	@Column(name="ABST_IN")
	private String abstIn;

	@Column(name="ADD_RTO")
	private BigDecimal addRto;

	@Column(name="ADVCST_MRK")
	private String advcstMrk;

	@Column(name="ADVPAY_TP")
	private String advpayTp;

	@Column(name="ANLYS_CD")
	private String anlysCd;

	@Column(name="BKBND_CHG_TP")
	private String bkbndChgTp;

	@Column(name="BUMON_CD")
	private String bumonCd;

	@Column(name="BUY_NO_B_RCNT")
	private String buyNoBRcnt;

	@Column(name="BUY_NO_R_RCNT")
	private String buyNoRRcnt;

	@Column(name="CHRG_BNKACC_CD")
	private String chrgBnkaccCd;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CST_ADD_YM")
	private String cstAddYm;

	@Column(name="DLT_FG")
	private String dltFg;

	@Temporal(TemporalType.DATE)
	@Column(name="DLV_DT")
	private Date dlvDt;

	@Column(name="DLV_LC")
	private String dlvLc;

	@Column(name="DLV_OBJ")
	private String dlvObj;

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

	@Temporal(TemporalType.DATE)
	@Column(name="INSP_COMP_DT")
	private Date inspCompDt;

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

	@Column(name="ORGNZ_CD")
	private String orgnzCd;

	@Column(name="PAY_APPL_CD")
	private String payApplCd;

	@Column(name="PAY_APPL_TP")
	private String payApplTp;

	@Column(name="PAY_COND_CD")
	private String payCondCd;

	@Column(name="PAY_COND_CHGRSN")
	private String payCondChgrsn;

	@Column(name="PAY_COND_NM")
	private String payCondNm;

	@Column(name="PAY_MTH")
	private String payMth;

	@Temporal(TemporalType.DATE)
	@Column(name="PAY_PLN_DT")
	private Date payPlnDt;

	@Column(name="PAY_SITE_CD")
	private String paySiteCd;

	@Column(name="PRD_PURORD_NO")
	private BigDecimal prdPurordNo;

	@Column(name="PRD_PURORD_TP")
	private String prdPurordTp;

	@Column(name="PURORD_TP")
	private String purordTp;

	private String purps;

	@Column(name="RCVINSP_AMT_EXCTAX")
	private BigDecimal rcvinspAmtExctax;

	@Column(name="RCVINSP_AMT_FC_EXCTAX")
	private BigDecimal rcvinspAmtFcExctax;

	@Column(name="RCVINSP_AMT_FC_INCTAX")
	private BigDecimal rcvinspAmtFcInctax;

	@Column(name="RCVINSP_AMT_INCTAX")
	private BigDecimal rcvinspAmtInctax;

	@Column(name="RCVINSP_CONT")
	private String rcvinspCont;

	@Temporal(TemporalType.DATE)
	@Column(name="RCVINSP_DT")
	private Date rcvinspDt;

	@Column(name="RCVINSP_NM")
	private String rcvinspNm;

	@Column(name="RCVINSP_STMP")
	private String rcvinspStmp;

	@Column(name="RCVINSP_STS")
	private String rcvinspSts;

	@Column(name="RCVINSP_YM")
	private String rcvinspYm;

	@Column(name="RCVINSP_YM_E")
	private String rcvinspYmE;

	@Column(name="RCVINSP_YM_S")
	private String rcvinspYmS;

	private String rcvinspr;

	@Column(name="RCVINSPSHT_ADR")
	private String rcvinspshtAdr;

	@Column(name="RCVINSPSHT_COMPANY_NM")
	private String rcvinspshtCompanyNm;

	@Column(name="RCVINSPSHT_FRMT")
	private String rcvinspshtFrmt;

	@Temporal(TemporalType.DATE)
	@Column(name="RCVINSPSHT_ISS_DT")
	private Date rcvinspshtIssDt;

	@Column(name="RCVINSPSHT_PIC_NM")
	private String rcvinspshtPicNm;

	@Column(name="RCVINSPSHT_RMK")
	private String rcvinspshtRmk;

	@Column(name="RCVINSPSHT_TO_ADR")
	private String rcvinspshtToAdr;

	@Column(name="RCVINSPSHT_TO_COMPANY_NM")
	private String rcvinspshtToCompanyNm;

	@Column(name="RCVINSPSHT_TO_DPT_NM")
	private String rcvinspshtToDptNm;

	@Column(name="RCVINSPSHT_TO_PIC_NM")
	private String rcvinspshtToPicNm;

	@Column(name="RCVINSPSHT_TO_TEL_NO")
	private String rcvinspshtToTelNo;

	@Column(name="RCVINSPSHT_TO_ZIP_CD")
	private String rcvinspshtToZipCd;

	private String rmk;

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

	@Column(name="STMP_TP")
	private String stmpTp;

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

	public RcvinspInf() {
	}

	public RcvinspInfPK getId() {
		return this.id;
	}

	public void setId(RcvinspInfPK id) {
		this.id = id;
	}

	public String getAbstIn() {
		return this.abstIn;
	}

	public void setAbstIn(String abstIn) {
		this.abstIn = abstIn;
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

	public String getBkbndChgTp() {
		return this.bkbndChgTp;
	}

	public void setBkbndChgTp(String bkbndChgTp) {
		this.bkbndChgTp = bkbndChgTp;
	}

	public String getBumonCd() {
		return this.bumonCd;
	}

	public void setBumonCd(String bumonCd) {
		this.bumonCd = bumonCd;
	}

	public String getBuyNoBRcnt() {
		return this.buyNoBRcnt;
	}

	public void setBuyNoBRcnt(String buyNoBRcnt) {
		this.buyNoBRcnt = buyNoBRcnt;
	}

	public String getBuyNoRRcnt() {
		return this.buyNoRRcnt;
	}

	public void setBuyNoRRcnt(String buyNoRRcnt) {
		this.buyNoRRcnt = buyNoRRcnt;
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

	public Date getDlvDt() {
		return this.dlvDt;
	}

	public void setDlvDt(Date dlvDt) {
		this.dlvDt = dlvDt;
	}

	public String getDlvLc() {
		return this.dlvLc;
	}

	public void setDlvLc(String dlvLc) {
		this.dlvLc = dlvLc;
	}

	public String getDlvObj() {
		return this.dlvObj;
	}

	public void setDlvObj(String dlvObj) {
		this.dlvObj = dlvObj;
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

	public Date getInspCompDt() {
		return this.inspCompDt;
	}

	public void setInspCompDt(Date inspCompDt) {
		this.inspCompDt = inspCompDt;
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

	public Date getPayPlnDt() {
		return this.payPlnDt;
	}

	public void setPayPlnDt(Date payPlnDt) {
		this.payPlnDt = payPlnDt;
	}

	public String getPaySiteCd() {
		return this.paySiteCd;
	}

	public void setPaySiteCd(String paySiteCd) {
		this.paySiteCd = paySiteCd;
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

	public String getPurordTp() {
		return this.purordTp;
	}

	public void setPurordTp(String purordTp) {
		this.purordTp = purordTp;
	}

	public String getPurps() {
		return this.purps;
	}

	public void setPurps(String purps) {
		this.purps = purps;
	}

	public BigDecimal getRcvinspAmtExctax() {
		return this.rcvinspAmtExctax;
	}

	public void setRcvinspAmtExctax(BigDecimal rcvinspAmtExctax) {
		this.rcvinspAmtExctax = rcvinspAmtExctax;
	}

	public BigDecimal getRcvinspAmtFcExctax() {
		return this.rcvinspAmtFcExctax;
	}

	public void setRcvinspAmtFcExctax(BigDecimal rcvinspAmtFcExctax) {
		this.rcvinspAmtFcExctax = rcvinspAmtFcExctax;
	}

	public BigDecimal getRcvinspAmtFcInctax() {
		return this.rcvinspAmtFcInctax;
	}

	public void setRcvinspAmtFcInctax(BigDecimal rcvinspAmtFcInctax) {
		this.rcvinspAmtFcInctax = rcvinspAmtFcInctax;
	}

	public BigDecimal getRcvinspAmtInctax() {
		return this.rcvinspAmtInctax;
	}

	public void setRcvinspAmtInctax(BigDecimal rcvinspAmtInctax) {
		this.rcvinspAmtInctax = rcvinspAmtInctax;
	}

	public String getRcvinspCont() {
		return this.rcvinspCont;
	}

	public void setRcvinspCont(String rcvinspCont) {
		this.rcvinspCont = rcvinspCont;
	}

	public Date getRcvinspDt() {
		return this.rcvinspDt;
	}

	public void setRcvinspDt(Date rcvinspDt) {
		this.rcvinspDt = rcvinspDt;
	}

	public String getRcvinspNm() {
		return this.rcvinspNm;
	}

	public void setRcvinspNm(String rcvinspNm) {
		this.rcvinspNm = rcvinspNm;
	}

	public String getRcvinspStmp() {
		return this.rcvinspStmp;
	}

	public void setRcvinspStmp(String rcvinspStmp) {
		this.rcvinspStmp = rcvinspStmp;
	}

	public String getRcvinspSts() {
		return this.rcvinspSts;
	}

	public void setRcvinspSts(String rcvinspSts) {
		this.rcvinspSts = rcvinspSts;
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

	public String getRcvinspr() {
		return this.rcvinspr;
	}

	public void setRcvinspr(String rcvinspr) {
		this.rcvinspr = rcvinspr;
	}

	public String getRcvinspshtAdr() {
		return this.rcvinspshtAdr;
	}

	public void setRcvinspshtAdr(String rcvinspshtAdr) {
		this.rcvinspshtAdr = rcvinspshtAdr;
	}

	public String getRcvinspshtCompanyNm() {
		return this.rcvinspshtCompanyNm;
	}

	public void setRcvinspshtCompanyNm(String rcvinspshtCompanyNm) {
		this.rcvinspshtCompanyNm = rcvinspshtCompanyNm;
	}

	public String getRcvinspshtFrmt() {
		return this.rcvinspshtFrmt;
	}

	public void setRcvinspshtFrmt(String rcvinspshtFrmt) {
		this.rcvinspshtFrmt = rcvinspshtFrmt;
	}

	public Date getRcvinspshtIssDt() {
		return this.rcvinspshtIssDt;
	}

	public void setRcvinspshtIssDt(Date rcvinspshtIssDt) {
		this.rcvinspshtIssDt = rcvinspshtIssDt;
	}

	public String getRcvinspshtPicNm() {
		return this.rcvinspshtPicNm;
	}

	public void setRcvinspshtPicNm(String rcvinspshtPicNm) {
		this.rcvinspshtPicNm = rcvinspshtPicNm;
	}

	public String getRcvinspshtRmk() {
		return this.rcvinspshtRmk;
	}

	public void setRcvinspshtRmk(String rcvinspshtRmk) {
		this.rcvinspshtRmk = rcvinspshtRmk;
	}

	public String getRcvinspshtToAdr() {
		return this.rcvinspshtToAdr;
	}

	public void setRcvinspshtToAdr(String rcvinspshtToAdr) {
		this.rcvinspshtToAdr = rcvinspshtToAdr;
	}

	public String getRcvinspshtToCompanyNm() {
		return this.rcvinspshtToCompanyNm;
	}

	public void setRcvinspshtToCompanyNm(String rcvinspshtToCompanyNm) {
		this.rcvinspshtToCompanyNm = rcvinspshtToCompanyNm;
	}

	public String getRcvinspshtToDptNm() {
		return this.rcvinspshtToDptNm;
	}

	public void setRcvinspshtToDptNm(String rcvinspshtToDptNm) {
		this.rcvinspshtToDptNm = rcvinspshtToDptNm;
	}

	public String getRcvinspshtToPicNm() {
		return this.rcvinspshtToPicNm;
	}

	public void setRcvinspshtToPicNm(String rcvinspshtToPicNm) {
		this.rcvinspshtToPicNm = rcvinspshtToPicNm;
	}

	public String getRcvinspshtToTelNo() {
		return this.rcvinspshtToTelNo;
	}

	public void setRcvinspshtToTelNo(String rcvinspshtToTelNo) {
		this.rcvinspshtToTelNo = rcvinspshtToTelNo;
	}

	public String getRcvinspshtToZipCd() {
		return this.rcvinspshtToZipCd;
	}

	public void setRcvinspshtToZipCd(String rcvinspshtToZipCd) {
		this.rcvinspshtToZipCd = rcvinspshtToZipCd;
	}

	public String getRmk() {
		return this.rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
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

	public String getStmpTp() {
		return this.stmpTp;
	}

	public void setStmpTp(String stmpTp) {
		this.stmpTp = stmpTp;
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