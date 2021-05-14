package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the RCVINSPDTL_INF database table.
 *
 */
@Entity
@Table(name="RCVINSPDTL_INF")
@NamedQuery(name="RcvinspdtlInf.findAll", query="SELECT r FROM RcvinspdtlInf r")
public class RcvinspdtlInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RcvinspdtlInfPK id;

	@Column(name="ADD_RTO")
	private BigDecimal addRto;

	@Column(name="ADVPAY_APLY_AMT_FC")
	private BigDecimal advpayAplyAmtFc;

	@Column(name="ADVPAY_APLY_AMT_JPY")
	private BigDecimal advpayAplyAmtJpy;

	@Column(name="ADVPAY_NO")
	private String advpayNo;

	@Column(name="ANLYS_CD")
	private String anlysCd;

	@Column(name="APPL_CONT")
	private String applCont;

	@Temporal(TemporalType.DATE)
	@Column(name="APPL_PRD_E_DT")
	private Date applPrdEDt;

	@Temporal(TemporalType.DATE)
	@Column(name="APPL_PRD_S_DT")
	private Date applPrdSDt;

	@Column(name="ASST_TP")
	private String asstTp;

	@Column(name="BND_FLR_CD")
	private String bndFlrCd;

	@Column(name="BRKDWN_TP")
	private String brkdwnTp;

	@Column(name="BUMON_CD")
	private String bumonCd;

	@Column(name="COM_CHG_ORGNZ_CD")
	private String comChgOrgnzCd;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CTRCT_TP")
	private String ctrctTp;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="DVC_CD")
	private String dvcCd;

	@Column(name="FRT_PNT_CD")
	private String frtPntCd;

	@Column(name="HLDTAX_AMT")
	private BigDecimal hldtaxAmt;

	@Column(name="HLDTAX_RTO")
	private BigDecimal hldtaxRto;

	@Column(name="HLDTAX_TP")
	private String hldtaxTp;

	@Column(name="INV_COMPANY_CD")
	private String invCompanyCd;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="ITM_CD")
	private String itmCd;

	@Column(name="ITM_NM")
	private String itmNm;

	@Column(name="ITM_TP1")
	private String itmTp1;

	@Column(name="ITM_TP2")
	private String itmTp2;

	@Column(name="ITM_TP3")
	private String itmTp3;

	@Column(name="ITMEXPS_CD1")
	private String itmexpsCd1;

	@Column(name="ITMEXPS_CD2")
	private String itmexpsCd2;

	@Column(name="MDA_ID")
	private String mdaId;

	@Column(name="MNY_CD")
	private String mnyCd;

	@Column(name="MSR_TOOL_CD")
	private String msrToolCd;

	@Column(name="ORGNZ_CD")
	private String orgnzCd;

	@Column(name="PURORD_AMT_FC")
	private BigDecimal purordAmtFc;

	@Column(name="PURORD_AMT_JPY")
	private BigDecimal purordAmtJpy;

	@Column(name="PURORD_AMT_JPY_INCTAX")
	private BigDecimal purordAmtJpyInctax;

	@Column(name="PURORD_DTL_NO")
	private BigDecimal purordDtlNo;

	@Column(name="PURORD_DTL_TP")
	private String purordDtlTp;

	@Column(name="PURORD_NO")
	private String purordNo;

	@Column(name="PURORD_QNT")
	private BigDecimal purordQnt;

	@Column(name="PURORD_UC_FC")
	private BigDecimal purordUcFc;

	@Column(name="PURORD_UC_JPY")
	private BigDecimal purordUcJpy;

	@Column(name="RCVINSP_AMT_FC")
	private BigDecimal rcvinspAmtFc;

	@Column(name="RCVINSP_AMT_JPY")
	private BigDecimal rcvinspAmtJpy;

	@Column(name="RCVINSP_AMT_JPY_INCTAX")
	private BigDecimal rcvinspAmtJpyInctax;

	@Column(name="RCVINSP_QNT")
	private BigDecimal rcvinspQnt;

	@Column(name="RCVINSP_SMRY")
	private String rcvinspSmry;

	@Column(name="RCVINSP_UC_FC")
	private BigDecimal rcvinspUcFc;

	@Column(name="RCVINSP_UC_JPY")
	private BigDecimal rcvinspUcJpy;

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

	@Column(name="UNT_CD")
	private String untCd;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public RcvinspdtlInf() {
	}

	public RcvinspdtlInfPK getId() {
		return this.id;
	}

	public void setId(RcvinspdtlInfPK id) {
		this.id = id;
	}

	public BigDecimal getAddRto() {
		return this.addRto;
	}

	public void setAddRto(BigDecimal addRto) {
		this.addRto = addRto;
	}

	public BigDecimal getAdvpayAplyAmtFc() {
		return this.advpayAplyAmtFc;
	}

	public void setAdvpayAplyAmtFc(BigDecimal advpayAplyAmtFc) {
		this.advpayAplyAmtFc = advpayAplyAmtFc;
	}

	public BigDecimal getAdvpayAplyAmtJpy() {
		return this.advpayAplyAmtJpy;
	}

	public void setAdvpayAplyAmtJpy(BigDecimal advpayAplyAmtJpy) {
		this.advpayAplyAmtJpy = advpayAplyAmtJpy;
	}

	public String getAdvpayNo() {
		return this.advpayNo;
	}

	public void setAdvpayNo(String advpayNo) {
		this.advpayNo = advpayNo;
	}

	public String getAnlysCd() {
		return this.anlysCd;
	}

	public void setAnlysCd(String anlysCd) {
		this.anlysCd = anlysCd;
	}

	public String getApplCont() {
		return this.applCont;
	}

	public void setApplCont(String applCont) {
		this.applCont = applCont;
	}

	public Date getApplPrdEDt() {
		return this.applPrdEDt;
	}

	public void setApplPrdEDt(Date applPrdEDt) {
		this.applPrdEDt = applPrdEDt;
	}

	public Date getApplPrdSDt() {
		return this.applPrdSDt;
	}

	public void setApplPrdSDt(Date applPrdSDt) {
		this.applPrdSDt = applPrdSDt;
	}

	public String getAsstTp() {
		return this.asstTp;
	}

	public void setAsstTp(String asstTp) {
		this.asstTp = asstTp;
	}

	public String getBndFlrCd() {
		return this.bndFlrCd;
	}

	public void setBndFlrCd(String bndFlrCd) {
		this.bndFlrCd = bndFlrCd;
	}

	public String getBrkdwnTp() {
		return this.brkdwnTp;
	}

	public void setBrkdwnTp(String brkdwnTp) {
		this.brkdwnTp = brkdwnTp;
	}

	public String getBumonCd() {
		return this.bumonCd;
	}

	public void setBumonCd(String bumonCd) {
		this.bumonCd = bumonCd;
	}

	public String getComChgOrgnzCd() {
		return this.comChgOrgnzCd;
	}

	public void setComChgOrgnzCd(String comChgOrgnzCd) {
		this.comChgOrgnzCd = comChgOrgnzCd;
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

	public String getCtrctTp() {
		return this.ctrctTp;
	}

	public void setCtrctTp(String ctrctTp) {
		this.ctrctTp = ctrctTp;
	}

	public String getDltFg() {
		return this.dltFg;
	}

	public void setDltFg(String dltFg) {
		this.dltFg = dltFg;
	}

	public String getDvcCd() {
		return this.dvcCd;
	}

	public void setDvcCd(String dvcCd) {
		this.dvcCd = dvcCd;
	}

	public String getFrtPntCd() {
		return this.frtPntCd;
	}

	public void setFrtPntCd(String frtPntCd) {
		this.frtPntCd = frtPntCd;
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

	public String getHldtaxTp() {
		return this.hldtaxTp;
	}

	public void setHldtaxTp(String hldtaxTp) {
		this.hldtaxTp = hldtaxTp;
	}

	public String getInvCompanyCd() {
		return this.invCompanyCd;
	}

	public void setInvCompanyCd(String invCompanyCd) {
		this.invCompanyCd = invCompanyCd;
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

	public String getItmTp1() {
		return this.itmTp1;
	}

	public void setItmTp1(String itmTp1) {
		this.itmTp1 = itmTp1;
	}

	public String getItmTp2() {
		return this.itmTp2;
	}

	public void setItmTp2(String itmTp2) {
		this.itmTp2 = itmTp2;
	}

	public String getItmTp3() {
		return this.itmTp3;
	}

	public void setItmTp3(String itmTp3) {
		this.itmTp3 = itmTp3;
	}

	public String getItmexpsCd1() {
		return this.itmexpsCd1;
	}

	public void setItmexpsCd1(String itmexpsCd1) {
		this.itmexpsCd1 = itmexpsCd1;
	}

	public String getItmexpsCd2() {
		return this.itmexpsCd2;
	}

	public void setItmexpsCd2(String itmexpsCd2) {
		this.itmexpsCd2 = itmexpsCd2;
	}

	public String getMdaId() {
		return this.mdaId;
	}

	public void setMdaId(String mdaId) {
		this.mdaId = mdaId;
	}

	public String getMnyCd() {
		return this.mnyCd;
	}

	public void setMnyCd(String mnyCd) {
		this.mnyCd = mnyCd;
	}

	public String getMsrToolCd() {
		return this.msrToolCd;
	}

	public void setMsrToolCd(String msrToolCd) {
		this.msrToolCd = msrToolCd;
	}

	public String getOrgnzCd() {
		return this.orgnzCd;
	}

	public void setOrgnzCd(String orgnzCd) {
		this.orgnzCd = orgnzCd;
	}

	public BigDecimal getPurordAmtFc() {
		return this.purordAmtFc;
	}

	public void setPurordAmtFc(BigDecimal purordAmtFc) {
		this.purordAmtFc = purordAmtFc;
	}

	public BigDecimal getPurordAmtJpy() {
		return this.purordAmtJpy;
	}

	public void setPurordAmtJpy(BigDecimal purordAmtJpy) {
		this.purordAmtJpy = purordAmtJpy;
	}

	public BigDecimal getPurordAmtJpyInctax() {
		return this.purordAmtJpyInctax;
	}

	public void setPurordAmtJpyInctax(BigDecimal purordAmtJpyInctax) {
		this.purordAmtJpyInctax = purordAmtJpyInctax;
	}

	public BigDecimal getPurordDtlNo() {
		return this.purordDtlNo;
	}

	public void setPurordDtlNo(BigDecimal purordDtlNo) {
		this.purordDtlNo = purordDtlNo;
	}

	public String getPurordDtlTp() {
		return this.purordDtlTp;
	}

	public void setPurordDtlTp(String purordDtlTp) {
		this.purordDtlTp = purordDtlTp;
	}

	public String getPurordNo() {
		return this.purordNo;
	}

	public void setPurordNo(String purordNo) {
		this.purordNo = purordNo;
	}

	public BigDecimal getPurordQnt() {
		return this.purordQnt;
	}

	public void setPurordQnt(BigDecimal purordQnt) {
		this.purordQnt = purordQnt;
	}

	public BigDecimal getPurordUcFc() {
		return this.purordUcFc;
	}

	public void setPurordUcFc(BigDecimal purordUcFc) {
		this.purordUcFc = purordUcFc;
	}

	public BigDecimal getPurordUcJpy() {
		return this.purordUcJpy;
	}

	public void setPurordUcJpy(BigDecimal purordUcJpy) {
		this.purordUcJpy = purordUcJpy;
	}

	public BigDecimal getRcvinspAmtFc() {
		return this.rcvinspAmtFc;
	}

	public void setRcvinspAmtFc(BigDecimal rcvinspAmtFc) {
		this.rcvinspAmtFc = rcvinspAmtFc;
	}

	public BigDecimal getRcvinspAmtJpy() {
		return this.rcvinspAmtJpy;
	}

	public void setRcvinspAmtJpy(BigDecimal rcvinspAmtJpy) {
		this.rcvinspAmtJpy = rcvinspAmtJpy;
	}

	public BigDecimal getRcvinspAmtJpyInctax() {
		return this.rcvinspAmtJpyInctax;
	}

	public void setRcvinspAmtJpyInctax(BigDecimal rcvinspAmtJpyInctax) {
		this.rcvinspAmtJpyInctax = rcvinspAmtJpyInctax;
	}

	public BigDecimal getRcvinspQnt() {
		return this.rcvinspQnt;
	}

	public void setRcvinspQnt(BigDecimal rcvinspQnt) {
		this.rcvinspQnt = rcvinspQnt;
	}

	public String getRcvinspSmry() {
		return this.rcvinspSmry;
	}

	public void setRcvinspSmry(String rcvinspSmry) {
		this.rcvinspSmry = rcvinspSmry;
	}

	public BigDecimal getRcvinspUcFc() {
		return this.rcvinspUcFc;
	}

	public void setRcvinspUcFc(BigDecimal rcvinspUcFc) {
		this.rcvinspUcFc = rcvinspUcFc;
	}

	public BigDecimal getRcvinspUcJpy() {
		return this.rcvinspUcJpy;
	}

	public void setRcvinspUcJpy(BigDecimal rcvinspUcJpy) {
		this.rcvinspUcJpy = rcvinspUcJpy;
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

	public String getUntCd() {
		return this.untCd;
	}

	public void setUntCd(String untCd) {
		this.untCd = untCd;
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