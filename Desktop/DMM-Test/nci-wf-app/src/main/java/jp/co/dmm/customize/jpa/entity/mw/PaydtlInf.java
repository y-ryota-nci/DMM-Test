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
 * The persistent class for the PAYDTL_INF database table.
 *
 */
@Entity
@Table(name="PAYDTL_INF")
@NamedQuery(name="PaydtlInf.findAll", query="SELECT p FROM PaydtlInf p")
public class PaydtlInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PaydtlInfPK id;

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

	@Column(name="BUY_DTL_NO")
	private BigDecimal buyDtlNo;

	@Column(name="BUY_NO")
	private String buyNo;

	@Column(name="COM_CHG_ORGNZ_CD")
	private String comChgOrgnzCd;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CRDCRD_IN_NO")
	private String crdcrdInNo;

	@Column(name="CRDCRD_TP")
	private String crdcrdTp;

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

	@Column(name="JRNSLP_DTL_NO")
	private BigDecimal jrnslpDtlNo;

	@Column(name="JRNSLP_NO")
	private String jrnslpNo;

	@Column(name="MDA_ID")
	private String mdaId;

	@Column(name="MNY_CD")
	private String mnyCd;

	@Column(name="MSR_TOOL_CD")
	private String msrToolCd;

	@Column(name="ORGNZ_CD")
	private String orgnzCd;

	@Column(name="PAY_AMT_FC")
	private BigDecimal payAmtFc;

	@Column(name="PAY_AMT_JPY")
	private BigDecimal payAmtJpy;

	@Column(name="PAY_AMT_JPY_INCTAX")
	private BigDecimal payAmtJpyInctax;

	@Column(name="PAY_SMRY")
	private String paySmry;

	@Column(name="PURORD_NO")
	private String purordNo;

	@Column(name="RCVINSP_DTL_NO")
	private BigDecimal rcvinspDtlNo;

	@Column(name="RCVINSP_NO")
	private String rcvinspNo;

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

	public PaydtlInf() {
	}

	public PaydtlInfPK getId() {
		return this.id;
	}

	public void setId(PaydtlInfPK id) {
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

	public BigDecimal getBuyDtlNo() {
		return this.buyDtlNo;
	}

	public void setBuyDtlNo(BigDecimal buyDtlNo) {
		this.buyDtlNo = buyDtlNo;
	}

	public String getBuyNo() {
		return this.buyNo;
	}

	public void setBuyNo(String buyNo) {
		this.buyNo = buyNo;
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

	public String getCrdcrdInNo() {
		return this.crdcrdInNo;
	}

	public void setCrdcrdInNo(String crdcrdInNo) {
		this.crdcrdInNo = crdcrdInNo;
	}

	public String getCrdcrdTp() {
		return this.crdcrdTp;
	}

	public void setCrdcrdTp(String crdcrdTp) {
		this.crdcrdTp = crdcrdTp;
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

	public BigDecimal getJrnslpDtlNo() {
		return this.jrnslpDtlNo;
	}

	public void setJrnslpDtlNo(BigDecimal jrnslpDtlNo) {
		this.jrnslpDtlNo = jrnslpDtlNo;
	}

	public String getJrnslpNo() {
		return this.jrnslpNo;
	}

	public void setJrnslpNo(String jrnslpNo) {
		this.jrnslpNo = jrnslpNo;
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

	public BigDecimal getPayAmtFc() {
		return this.payAmtFc;
	}

	public void setPayAmtFc(BigDecimal payAmtFc) {
		this.payAmtFc = payAmtFc;
	}

	public BigDecimal getPayAmtJpy() {
		return this.payAmtJpy;
	}

	public void setPayAmtJpy(BigDecimal payAmtJpy) {
		this.payAmtJpy = payAmtJpy;
	}

	public BigDecimal getPayAmtJpyInctax() {
		return this.payAmtJpyInctax;
	}

	public void setPayAmtJpyInctax(BigDecimal payAmtJpyInctax) {
		this.payAmtJpyInctax = payAmtJpyInctax;
	}

	public String getPaySmry() {
		return this.paySmry;
	}

	public void setPaySmry(String paySmry) {
		this.paySmry = paySmry;
	}

	public String getPurordNo() {
		return this.purordNo;
	}

	public void setPurordNo(String purordNo) {
		this.purordNo = purordNo;
	}

	public BigDecimal getRcvinspDtlNo() {
		return this.rcvinspDtlNo;
	}

	public void setRcvinspDtlNo(BigDecimal rcvinspDtlNo) {
		this.rcvinspDtlNo = rcvinspDtlNo;
	}

	public String getRcvinspNo() {
		return this.rcvinspNo;
	}

	public void setRcvinspNo(String rcvinspNo) {
		this.rcvinspNo = rcvinspNo;
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