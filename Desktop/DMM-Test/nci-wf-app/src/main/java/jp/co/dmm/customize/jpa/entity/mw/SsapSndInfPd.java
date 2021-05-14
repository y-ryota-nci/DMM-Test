package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the SSAP_SND_INF_PD database table.
 * 
 */
@Entity
@Table(name="SSAP_SND_INF_PD")
@NamedQuery(name="SsapSndInfPd.findAll", query="SELECT s FROM SsapSndInfPd s")
public class SsapSndInfPd extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SsapSndInfPdPK id;

	@Column(name="AMT_FC")
	private BigDecimal amtFc;

	@Temporal(TemporalType.DATE)
	@Column(name="BLL_TRM_DT")
	private Date bllTrmDt;

	@Column(name="BLLDLV_DPT_CD")
	private String blldlvDptCd;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CSHDLV_DPT_CD")
	private String cshdlvDptCd;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="DPST_ACC_BRKDWN_CD")
	private String dpstAccBrkdwnCd;

	@Column(name="DPST_ACC_CD")
	private String dpstAccCd;

	@Column(name="DPST_AMT")
	private BigDecimal dpstAmt;

	@Column(name="DPST_AMT_EXCTAX")
	private BigDecimal dpstAmtExctax;

	@Column(name="DPST_DPT_CD")
	private String dpstDptCd;

	@Column(name="DPST_FNC_CD1")
	private String dpstFncCd1;

	@Column(name="DPST_FNC_CD2")
	private String dpstFncCd2;

	@Column(name="DPST_FNC_CD3")
	private String dpstFncCd3;

	@Column(name="DPST_FNC_CD4")
	private String dpstFncCd4;

	@Column(name="DPST_PRJ_CD")
	private String dpstPrjCd;

	@Column(name="DPST_PRS_FG")
	private String dpstPrsFg;

	@Column(name="DPST_SMRY1")
	private String dpstSmry1;

	@Column(name="DPST_SMRY2")
	private String dpstSmry2;

	@Column(name="DPST_SPLR_CD")
	private String dpstSplrCd;

	@Column(name="DPST_SPLR_PRS_TP")
	private String dpstSplrPrsTp;

	@Column(name="DPST_TAX_AMT")
	private BigDecimal dpstTaxAmt;

	@Column(name="DPST_TAX_CD")
	private String dpstTaxCd;

	@Column(name="DPST_TAX_IPT_TP")
	private String dpstTaxIptTp;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_APP_PLN_DT")
	private Date dtAppPlnDt;

	@Column(name="DT_TRS_ACC_BRKDWN_CD")
	private String dtTrsAccBrkdwnCd;

	@Column(name="DT_TRS_ACC_CD")
	private String dtTrsAccCd;

	@Column(name="DT_TRS_DPT_CD")
	private String dtTrsDptCd;

	@Column(name="DT_TRS_FNC_CD1")
	private String dtTrsFncCd1;

	@Column(name="DT_TRS_FNC_CD2")
	private String dtTrsFncCd2;

	@Column(name="DT_TRS_FNC_CD3")
	private String dtTrsFncCd3;

	@Column(name="DT_TRS_FNC_CD4")
	private String dtTrsFncCd4;

	@Column(name="DT_TRS_PRJ_CD")
	private String dtTrsPrjCd;

	@Column(name="DT_TRS_SMRY1")
	private String dtTrsSmry1;

	@Column(name="DT_TRS_SMRY2")
	private String dtTrsSmry2;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Temporal(TemporalType.DATE)
	@Column(name="ISS_DT")
	private Date issDt;

	@Column(name="PAY_AMT")
	private BigDecimal payAmt;

	@Column(name="PAY_LNNO")
	private String payLnno;

	@Column(name="PAY_MTH_CD")
	private String payMthCd;

	@Temporal(TemporalType.DATE)
	@Column(name="PAY_PLN_DT")
	private Date payPlnDt;

	@Column(name="PAYER_BNKACC_CD")
	private String payerBnkaccCd;

	@Column(name="RCRD_TP")
	private String rcrdTp;

	@Temporal(TemporalType.DATE)
	@Column(name="SLP_DT")
	private Date slpDt;

	@Column(name="SLP_GRP")
	private String slpGrp;

	@Column(name="SLP_NO")
	private String slpNo;

	@Column(name="SPR_CHR_ITM1")
	private String sprChrItm1;

	@Column(name="SPR_CHR_ITM2")
	private String sprChrItm2;

	@Column(name="SPR_CHR_ITM3")
	private String sprChrItm3;

	@Column(name="SPR_CHR_ITM4")
	private String sprChrItm4;

	@Column(name="SPR_CHR_ITM5")
	private String sprChrItm5;

	@Column(name="SPR_CHR_ITM6")
	private String sprChrItm6;

	@Column(name="SPR_CHR_ITM7")
	private String sprChrItm7;

	@Column(name="SPR_CHR_ITM8")
	private String sprChrItm8;

	@Column(name="SPR_NMR_ITM1")
	private BigDecimal sprNmrItm1;

	@Column(name="SPR_NMR_ITM2")
	private BigDecimal sprNmrItm2;

	@Column(name="SPR_NMR_ITM3")
	private BigDecimal sprNmrItm3;

	@Column(name="SS_COMPANY_CD")
	private String ssCompanyCd;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="TRSFND_INF_CD")
	private String trsfndInfCd;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public SsapSndInfPd() {
	}

	public SsapSndInfPdPK getId() {
		return this.id;
	}

	public void setId(SsapSndInfPdPK id) {
		this.id = id;
	}

	public BigDecimal getAmtFc() {
		return this.amtFc;
	}

	public void setAmtFc(BigDecimal amtFc) {
		this.amtFc = amtFc;
	}

	public Date getBllTrmDt() {
		return this.bllTrmDt;
	}

	public void setBllTrmDt(Date bllTrmDt) {
		this.bllTrmDt = bllTrmDt;
	}

	public String getBlldlvDptCd() {
		return this.blldlvDptCd;
	}

	public void setBlldlvDptCd(String blldlvDptCd) {
		this.blldlvDptCd = blldlvDptCd;
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

	public String getCshdlvDptCd() {
		return this.cshdlvDptCd;
	}

	public void setCshdlvDptCd(String cshdlvDptCd) {
		this.cshdlvDptCd = cshdlvDptCd;
	}

	public String getDltFg() {
		return this.dltFg;
	}

	public void setDltFg(String dltFg) {
		this.dltFg = dltFg;
	}

	public String getDpstAccBrkdwnCd() {
		return this.dpstAccBrkdwnCd;
	}

	public void setDpstAccBrkdwnCd(String dpstAccBrkdwnCd) {
		this.dpstAccBrkdwnCd = dpstAccBrkdwnCd;
	}

	public String getDpstAccCd() {
		return this.dpstAccCd;
	}

	public void setDpstAccCd(String dpstAccCd) {
		this.dpstAccCd = dpstAccCd;
	}

	public BigDecimal getDpstAmt() {
		return this.dpstAmt;
	}

	public void setDpstAmt(BigDecimal dpstAmt) {
		this.dpstAmt = dpstAmt;
	}

	public BigDecimal getDpstAmtExctax() {
		return this.dpstAmtExctax;
	}

	public void setDpstAmtExctax(BigDecimal dpstAmtExctax) {
		this.dpstAmtExctax = dpstAmtExctax;
	}

	public String getDpstDptCd() {
		return this.dpstDptCd;
	}

	public void setDpstDptCd(String dpstDptCd) {
		this.dpstDptCd = dpstDptCd;
	}

	public String getDpstFncCd1() {
		return this.dpstFncCd1;
	}

	public void setDpstFncCd1(String dpstFncCd1) {
		this.dpstFncCd1 = dpstFncCd1;
	}

	public String getDpstFncCd2() {
		return this.dpstFncCd2;
	}

	public void setDpstFncCd2(String dpstFncCd2) {
		this.dpstFncCd2 = dpstFncCd2;
	}

	public String getDpstFncCd3() {
		return this.dpstFncCd3;
	}

	public void setDpstFncCd3(String dpstFncCd3) {
		this.dpstFncCd3 = dpstFncCd3;
	}

	public String getDpstFncCd4() {
		return this.dpstFncCd4;
	}

	public void setDpstFncCd4(String dpstFncCd4) {
		this.dpstFncCd4 = dpstFncCd4;
	}

	public String getDpstPrjCd() {
		return this.dpstPrjCd;
	}

	public void setDpstPrjCd(String dpstPrjCd) {
		this.dpstPrjCd = dpstPrjCd;
	}

	public String getDpstPrsFg() {
		return this.dpstPrsFg;
	}

	public void setDpstPrsFg(String dpstPrsFg) {
		this.dpstPrsFg = dpstPrsFg;
	}

	public String getDpstSmry1() {
		return this.dpstSmry1;
	}

	public void setDpstSmry1(String dpstSmry1) {
		this.dpstSmry1 = dpstSmry1;
	}

	public String getDpstSmry2() {
		return this.dpstSmry2;
	}

	public void setDpstSmry2(String dpstSmry2) {
		this.dpstSmry2 = dpstSmry2;
	}

	public String getDpstSplrCd() {
		return this.dpstSplrCd;
	}

	public void setDpstSplrCd(String dpstSplrCd) {
		this.dpstSplrCd = dpstSplrCd;
	}

	public String getDpstSplrPrsTp() {
		return this.dpstSplrPrsTp;
	}

	public void setDpstSplrPrsTp(String dpstSplrPrsTp) {
		this.dpstSplrPrsTp = dpstSplrPrsTp;
	}

	public BigDecimal getDpstTaxAmt() {
		return this.dpstTaxAmt;
	}

	public void setDpstTaxAmt(BigDecimal dpstTaxAmt) {
		this.dpstTaxAmt = dpstTaxAmt;
	}

	public String getDpstTaxCd() {
		return this.dpstTaxCd;
	}

	public void setDpstTaxCd(String dpstTaxCd) {
		this.dpstTaxCd = dpstTaxCd;
	}

	public String getDpstTaxIptTp() {
		return this.dpstTaxIptTp;
	}

	public void setDpstTaxIptTp(String dpstTaxIptTp) {
		this.dpstTaxIptTp = dpstTaxIptTp;
	}

	public Date getDtAppPlnDt() {
		return this.dtAppPlnDt;
	}

	public void setDtAppPlnDt(Date dtAppPlnDt) {
		this.dtAppPlnDt = dtAppPlnDt;
	}

	public String getDtTrsAccBrkdwnCd() {
		return this.dtTrsAccBrkdwnCd;
	}

	public void setDtTrsAccBrkdwnCd(String dtTrsAccBrkdwnCd) {
		this.dtTrsAccBrkdwnCd = dtTrsAccBrkdwnCd;
	}

	public String getDtTrsAccCd() {
		return this.dtTrsAccCd;
	}

	public void setDtTrsAccCd(String dtTrsAccCd) {
		this.dtTrsAccCd = dtTrsAccCd;
	}

	public String getDtTrsDptCd() {
		return this.dtTrsDptCd;
	}

	public void setDtTrsDptCd(String dtTrsDptCd) {
		this.dtTrsDptCd = dtTrsDptCd;
	}

	public String getDtTrsFncCd1() {
		return this.dtTrsFncCd1;
	}

	public void setDtTrsFncCd1(String dtTrsFncCd1) {
		this.dtTrsFncCd1 = dtTrsFncCd1;
	}

	public String getDtTrsFncCd2() {
		return this.dtTrsFncCd2;
	}

	public void setDtTrsFncCd2(String dtTrsFncCd2) {
		this.dtTrsFncCd2 = dtTrsFncCd2;
	}

	public String getDtTrsFncCd3() {
		return this.dtTrsFncCd3;
	}

	public void setDtTrsFncCd3(String dtTrsFncCd3) {
		this.dtTrsFncCd3 = dtTrsFncCd3;
	}

	public String getDtTrsFncCd4() {
		return this.dtTrsFncCd4;
	}

	public void setDtTrsFncCd4(String dtTrsFncCd4) {
		this.dtTrsFncCd4 = dtTrsFncCd4;
	}

	public String getDtTrsPrjCd() {
		return this.dtTrsPrjCd;
	}

	public void setDtTrsPrjCd(String dtTrsPrjCd) {
		this.dtTrsPrjCd = dtTrsPrjCd;
	}

	public String getDtTrsSmry1() {
		return this.dtTrsSmry1;
	}

	public void setDtTrsSmry1(String dtTrsSmry1) {
		this.dtTrsSmry1 = dtTrsSmry1;
	}

	public String getDtTrsSmry2() {
		return this.dtTrsSmry2;
	}

	public void setDtTrsSmry2(String dtTrsSmry2) {
		this.dtTrsSmry2 = dtTrsSmry2;
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

	public Date getIssDt() {
		return this.issDt;
	}

	public void setIssDt(Date issDt) {
		this.issDt = issDt;
	}

	public BigDecimal getPayAmt() {
		return this.payAmt;
	}

	public void setPayAmt(BigDecimal payAmt) {
		this.payAmt = payAmt;
	}

	public String getPayLnno() {
		return this.payLnno;
	}

	public void setPayLnno(String payLnno) {
		this.payLnno = payLnno;
	}

	public String getPayMthCd() {
		return this.payMthCd;
	}

	public void setPayMthCd(String payMthCd) {
		this.payMthCd = payMthCd;
	}

	public Date getPayPlnDt() {
		return this.payPlnDt;
	}

	public void setPayPlnDt(Date payPlnDt) {
		this.payPlnDt = payPlnDt;
	}

	public String getPayerBnkaccCd() {
		return this.payerBnkaccCd;
	}

	public void setPayerBnkaccCd(String payerBnkaccCd) {
		this.payerBnkaccCd = payerBnkaccCd;
	}

	public String getRcrdTp() {
		return this.rcrdTp;
	}

	public void setRcrdTp(String rcrdTp) {
		this.rcrdTp = rcrdTp;
	}

	public Date getSlpDt() {
		return this.slpDt;
	}

	public void setSlpDt(Date slpDt) {
		this.slpDt = slpDt;
	}

	public String getSlpGrp() {
		return this.slpGrp;
	}

	public void setSlpGrp(String slpGrp) {
		this.slpGrp = slpGrp;
	}

	public String getSlpNo() {
		return this.slpNo;
	}

	public void setSlpNo(String slpNo) {
		this.slpNo = slpNo;
	}

	public String getSprChrItm1() {
		return this.sprChrItm1;
	}

	public void setSprChrItm1(String sprChrItm1) {
		this.sprChrItm1 = sprChrItm1;
	}

	public String getSprChrItm2() {
		return this.sprChrItm2;
	}

	public void setSprChrItm2(String sprChrItm2) {
		this.sprChrItm2 = sprChrItm2;
	}

	public String getSprChrItm3() {
		return this.sprChrItm3;
	}

	public void setSprChrItm3(String sprChrItm3) {
		this.sprChrItm3 = sprChrItm3;
	}

	public String getSprChrItm4() {
		return this.sprChrItm4;
	}

	public void setSprChrItm4(String sprChrItm4) {
		this.sprChrItm4 = sprChrItm4;
	}

	public String getSprChrItm5() {
		return this.sprChrItm5;
	}

	public void setSprChrItm5(String sprChrItm5) {
		this.sprChrItm5 = sprChrItm5;
	}

	public String getSprChrItm6() {
		return this.sprChrItm6;
	}

	public void setSprChrItm6(String sprChrItm6) {
		this.sprChrItm6 = sprChrItm6;
	}

	public String getSprChrItm7() {
		return this.sprChrItm7;
	}

	public void setSprChrItm7(String sprChrItm7) {
		this.sprChrItm7 = sprChrItm7;
	}

	public String getSprChrItm8() {
		return this.sprChrItm8;
	}

	public void setSprChrItm8(String sprChrItm8) {
		this.sprChrItm8 = sprChrItm8;
	}

	public BigDecimal getSprNmrItm1() {
		return this.sprNmrItm1;
	}

	public void setSprNmrItm1(BigDecimal sprNmrItm1) {
		this.sprNmrItm1 = sprNmrItm1;
	}

	public BigDecimal getSprNmrItm2() {
		return this.sprNmrItm2;
	}

	public void setSprNmrItm2(BigDecimal sprNmrItm2) {
		this.sprNmrItm2 = sprNmrItm2;
	}

	public BigDecimal getSprNmrItm3() {
		return this.sprNmrItm3;
	}

	public void setSprNmrItm3(BigDecimal sprNmrItm3) {
		this.sprNmrItm3 = sprNmrItm3;
	}

	public String getSsCompanyCd() {
		return this.ssCompanyCd;
	}

	public void setSsCompanyCd(String ssCompanyCd) {
		this.ssCompanyCd = ssCompanyCd;
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

	public String getTrsfndInfCd() {
		return this.trsfndInfCd;
	}

	public void setTrsfndInfCd(String trsfndInfCd) {
		this.trsfndInfCd = trsfndInfCd;
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