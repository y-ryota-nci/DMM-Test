package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the CNTRCT_INF database table.
 * 
 */
@Entity
@Table(name="CNTRCT_INF")
@NamedQuery(name="CntrctInf.findAll", query="SELECT c FROM CntrctInf c")
public class CntrctInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CntrctInfPK id;

	@Column(name="BKBND_CHG_TP")
	private String bkbndChgTp;

	@Column(name="BUMON_CD")
	private String bumonCd;

	@Column(name="CMPN_OCC_PRS")
	private String cmpnOccPrs;

	@Temporal(TemporalType.DATE)
	@Column(name="CNTRCT_CNCL_DT")
	private Date cntrctCnclDt;

	@Temporal(TemporalType.DATE)
	@Column(name="CNTRCT_DT")
	private Date cntrctDt;

	@Temporal(TemporalType.DATE)
	@Column(name="CNTRCT_DT_E")
	private Date cntrctDtE;

	@Temporal(TemporalType.DATE)
	@Column(name="CNTRCT_DT_S")
	private Date cntrctDtS;

	@Lob
	@Column(name="CNTRCT_DTLCND")
	private String cntrctDtlcnd;

	@Column(name="CNTRCT_NM")
	private String cntrctNm;

	@Temporal(TemporalType.DATE)
	@Column(name="CNTRCT_PRD_E_DT")
	private Date cntrctPrdEDt;

	@Temporal(TemporalType.DATE)
	@Column(name="CNTRCT_PRD_S_DT")
	private Date cntrctPrdSDt;

	@Column(name="CNTRCT_SHT_NM")
	private String cntrctShtNm;

	@Column(name="CNTRCT_STS")
	private String cntrctSts;

	@Column(name="CNTRCT_TP")
	private String cntrctTp;

	@Column(name="CNTRCTR_CD")
	private String cntrctrCd;

	@Column(name="CNTRCTR_DPT_CD")
	private String cntrctrDptCd;

	@Column(name="CNTRCTR_DPT_NM")
	private String cntrctrDptNm;

	@Column(name="CNTRCTSHT_FRMT")
	private String cntrctshtFrmt;

	@Column(name="CNTRCTSHT_RQST_TP")
	private String cntrctshtRqstTp;

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

	@Column(name="LGL_RMK")
	private String lglRmk;

	@Column(name="LGLR_CD")
	private String lglrCd;

	@Column(name="MAIL_RMK")
	private String mailRmk;

	@Column(name="MAIL_TP")
	private String mailTp;

	@Column(name="MNY_CD")
	private String mnyCd;

	@Column(name="N_CMPT_OBLG_TP")
	private String nCmptOblgTp;

	@Column(name="ORGNZ_CD")
	private String orgnzCd;

	@Column(name="PAY_CYC_PLN_RMK")
	private String payCycPlnRmk;

	@Column(name="PAY_CYC_PLN_TP")
	private String payCycPlnTp;

	@Column(name="PAY_SITE_CD")
	private String paySiteCd;

	private String rmk;

	@Column(name="RNW_DTLCND")
	private String rnwDtlcnd;

	@Column(name="RNW_MTH_TP")
	private String rnwMthTp;

	@Column(name="RNW_MTH_TP_AUTO")
	private String rnwMthTpAuto;

	@Column(name="RNW_MTH_TP_AUTO_INF_MT")
	private BigDecimal rnwMthTpAutoInfMt;

	@Column(name="RNW_MTH_TP_AUTO_PSTP_MT")
	private BigDecimal rnwMthTpAutoPstpMt;

	@Column(name="RNW_MTH_TP_EXPR")
	private BigDecimal rnwMthTpExpr;

	@Column(name="RNW_MTH_TP_INDF")
	private BigDecimal rnwMthTpIndf;

	@Column(name="RNW_MTH_TP_NEGO")
	private String rnwMthTpNego;

	@Column(name="RNW_MTH_TP_NEGO_INF_MT")
	private BigDecimal rnwMthTpNegoInfMt;

	@Column(name="RNW_PRS")
	private String rnwPrs;

	@Column(name="RTN_PAY_NO")
	private BigDecimal rtnPayNo;

	@Column(name="RTN_PAY_TP")
	private String rtnPayTp;

	@Column(name="RVNSTMP_AMT")
	private BigDecimal rvnstmpAmt;

	@Column(name="RVNSTMP_PRS")
	private String rvnstmpPrs;

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

	@Column(name="STMP_APP_TP")
	private String stmpAppTp;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="TO_ADR")
	private String toAdr;

	@Column(name="TO_APPRV_RMK")
	private String toApprvRmk;

	@Column(name="TRD_EST_AMT_EXCTAX")
	private BigDecimal trdEstAmtExctax;

	@Column(name="TRD_EST_AMT_RMK")
	private String trdEstAmtRmk;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public CntrctInf() {
	}

	public CntrctInfPK getId() {
		return this.id;
	}

	public void setId(CntrctInfPK id) {
		this.id = id;
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

	public String getCmpnOccPrs() {
		return this.cmpnOccPrs;
	}

	public void setCmpnOccPrs(String cmpnOccPrs) {
		this.cmpnOccPrs = cmpnOccPrs;
	}

	public Date getCntrctCnclDt() {
		return this.cntrctCnclDt;
	}

	public void setCntrctCnclDt(Date cntrctCnclDt) {
		this.cntrctCnclDt = cntrctCnclDt;
	}

	public Date getCntrctDt() {
		return this.cntrctDt;
	}

	public void setCntrctDt(Date cntrctDt) {
		this.cntrctDt = cntrctDt;
	}

	public Date getCntrctDtE() {
		return this.cntrctDtE;
	}

	public void setCntrctDtE(Date cntrctDtE) {
		this.cntrctDtE = cntrctDtE;
	}

	public Date getCntrctDtS() {
		return this.cntrctDtS;
	}

	public void setCntrctDtS(Date cntrctDtS) {
		this.cntrctDtS = cntrctDtS;
	}

	public String getCntrctDtlcnd() {
		return this.cntrctDtlcnd;
	}

	public void setCntrctDtlcnd(String cntrctDtlcnd) {
		this.cntrctDtlcnd = cntrctDtlcnd;
	}

	public String getCntrctNm() {
		return this.cntrctNm;
	}

	public void setCntrctNm(String cntrctNm) {
		this.cntrctNm = cntrctNm;
	}

	public Date getCntrctPrdEDt() {
		return this.cntrctPrdEDt;
	}

	public void setCntrctPrdEDt(Date cntrctPrdEDt) {
		this.cntrctPrdEDt = cntrctPrdEDt;
	}

	public Date getCntrctPrdSDt() {
		return this.cntrctPrdSDt;
	}

	public void setCntrctPrdSDt(Date cntrctPrdSDt) {
		this.cntrctPrdSDt = cntrctPrdSDt;
	}

	public String getCntrctShtNm() {
		return this.cntrctShtNm;
	}

	public void setCntrctShtNm(String cntrctShtNm) {
		this.cntrctShtNm = cntrctShtNm;
	}

	public String getCntrctSts() {
		return this.cntrctSts;
	}

	public void setCntrctSts(String cntrctSts) {
		this.cntrctSts = cntrctSts;
	}

	public String getCntrctTp() {
		return this.cntrctTp;
	}

	public void setCntrctTp(String cntrctTp) {
		this.cntrctTp = cntrctTp;
	}

	public String getCntrctrCd() {
		return this.cntrctrCd;
	}

	public void setCntrctrCd(String cntrctrCd) {
		this.cntrctrCd = cntrctrCd;
	}

	public String getCntrctrDptCd() {
		return this.cntrctrDptCd;
	}

	public void setCntrctrDptCd(String cntrctrDptCd) {
		this.cntrctrDptCd = cntrctrDptCd;
	}

	public String getCntrctrDptNm() {
		return this.cntrctrDptNm;
	}

	public void setCntrctrDptNm(String cntrctrDptNm) {
		this.cntrctrDptNm = cntrctrDptNm;
	}

	public String getCntrctshtFrmt() {
		return this.cntrctshtFrmt;
	}

	public void setCntrctshtFrmt(String cntrctshtFrmt) {
		this.cntrctshtFrmt = cntrctshtFrmt;
	}

	public String getCntrctshtRqstTp() {
		return this.cntrctshtRqstTp;
	}

	public void setCntrctshtRqstTp(String cntrctshtRqstTp) {
		this.cntrctshtRqstTp = cntrctshtRqstTp;
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

	public String getLglRmk() {
		return this.lglRmk;
	}

	public void setLglRmk(String lglRmk) {
		this.lglRmk = lglRmk;
	}

	public String getLglrCd() {
		return this.lglrCd;
	}

	public void setLglrCd(String lglrCd) {
		this.lglrCd = lglrCd;
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

	public String getNCmptOblgTp() {
		return this.nCmptOblgTp;
	}

	public void setNCmptOblgTp(String nCmptOblgTp) {
		this.nCmptOblgTp = nCmptOblgTp;
	}

	public String getOrgnzCd() {
		return this.orgnzCd;
	}

	public void setOrgnzCd(String orgnzCd) {
		this.orgnzCd = orgnzCd;
	}

	public String getPayCycPlnRmk() {
		return this.payCycPlnRmk;
	}

	public void setPayCycPlnRmk(String payCycPlnRmk) {
		this.payCycPlnRmk = payCycPlnRmk;
	}

	public String getPayCycPlnTp() {
		return this.payCycPlnTp;
	}

	public void setPayCycPlnTp(String payCycPlnTp) {
		this.payCycPlnTp = payCycPlnTp;
	}

	public String getPaySiteCd() {
		return this.paySiteCd;
	}

	public void setPaySiteCd(String paySiteCd) {
		this.paySiteCd = paySiteCd;
	}

	public String getRmk() {
		return this.rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getRnwDtlcnd() {
		return this.rnwDtlcnd;
	}

	public void setRnwDtlcnd(String rnwDtlcnd) {
		this.rnwDtlcnd = rnwDtlcnd;
	}

	public String getRnwMthTp() {
		return this.rnwMthTp;
	}

	public void setRnwMthTp(String rnwMthTp) {
		this.rnwMthTp = rnwMthTp;
	}

	public String getRnwMthTpAuto() {
		return this.rnwMthTpAuto;
	}

	public void setRnwMthTpAuto(String rnwMthTpAuto) {
		this.rnwMthTpAuto = rnwMthTpAuto;
	}

	public BigDecimal getRnwMthTpAutoInfMt() {
		return this.rnwMthTpAutoInfMt;
	}

	public void setRnwMthTpAutoInfMt(BigDecimal rnwMthTpAutoInfMt) {
		this.rnwMthTpAutoInfMt = rnwMthTpAutoInfMt;
	}

	public BigDecimal getRnwMthTpAutoPstpMt() {
		return this.rnwMthTpAutoPstpMt;
	}

	public void setRnwMthTpAutoPstpMt(BigDecimal rnwMthTpAutoPstpMt) {
		this.rnwMthTpAutoPstpMt = rnwMthTpAutoPstpMt;
	}

	public BigDecimal getRnwMthTpExpr() {
		return this.rnwMthTpExpr;
	}

	public void setRnwMthTpExpr(BigDecimal rnwMthTpExpr) {
		this.rnwMthTpExpr = rnwMthTpExpr;
	}

	public BigDecimal getRnwMthTpIndf() {
		return this.rnwMthTpIndf;
	}

	public void setRnwMthTpIndf(BigDecimal rnwMthTpIndf) {
		this.rnwMthTpIndf = rnwMthTpIndf;
	}

	public String getRnwMthTpNego() {
		return this.rnwMthTpNego;
	}

	public void setRnwMthTpNego(String rnwMthTpNego) {
		this.rnwMthTpNego = rnwMthTpNego;
	}

	public BigDecimal getRnwMthTpNegoInfMt() {
		return this.rnwMthTpNegoInfMt;
	}

	public void setRnwMthTpNegoInfMt(BigDecimal rnwMthTpNegoInfMt) {
		this.rnwMthTpNegoInfMt = rnwMthTpNegoInfMt;
	}

	public String getRnwPrs() {
		return this.rnwPrs;
	}

	public void setRnwPrs(String rnwPrs) {
		this.rnwPrs = rnwPrs;
	}

	public BigDecimal getRtnPayNo() {
		return this.rtnPayNo;
	}

	public void setRtnPayNo(BigDecimal rtnPayNo) {
		this.rtnPayNo = rtnPayNo;
	}

	public String getRtnPayTp() {
		return this.rtnPayTp;
	}

	public void setRtnPayTp(String rtnPayTp) {
		this.rtnPayTp = rtnPayTp;
	}

	public BigDecimal getRvnstmpAmt() {
		return this.rvnstmpAmt;
	}

	public void setRvnstmpAmt(BigDecimal rvnstmpAmt) {
		this.rvnstmpAmt = rvnstmpAmt;
	}

	public String getRvnstmpPrs() {
		return this.rvnstmpPrs;
	}

	public void setRvnstmpPrs(String rvnstmpPrs) {
		this.rvnstmpPrs = rvnstmpPrs;
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

	public String getStmpAppTp() {
		return this.stmpAppTp;
	}

	public void setStmpAppTp(String stmpAppTp) {
		this.stmpAppTp = stmpAppTp;
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

	public String getToAdr() {
		return this.toAdr;
	}

	public void setToAdr(String toAdr) {
		this.toAdr = toAdr;
	}

	public String getToApprvRmk() {
		return this.toApprvRmk;
	}

	public void setToApprvRmk(String toApprvRmk) {
		this.toApprvRmk = toApprvRmk;
	}

	public BigDecimal getTrdEstAmtExctax() {
		return this.trdEstAmtExctax;
	}

	public void setTrdEstAmtExctax(BigDecimal trdEstAmtExctax) {
		this.trdEstAmtExctax = trdEstAmtExctax;
	}

	public String getTrdEstAmtRmk() {
		return this.trdEstAmtRmk;
	}

	public void setTrdEstAmtRmk(String trdEstAmtRmk) {
		this.trdEstAmtRmk = trdEstAmtRmk;
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