package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the SSGL_SND_INF_DT database table.
 * 
 */
@Entity
@Table(name="SSGL_SND_INF_DT")
@NamedQuery(name="SsglSndInfDt.findAll", query="SELECT s FROM SsglSndInfDt s")
public class SsglSndInfDt extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SsglSndInfDtPK id;

	@Column(name="ACC_BRKDWN_CD")
	private String accBrkdwnCd;

	@Column(name="ACC_CD")
	private String accCd;

	@Column(name="AMT_EXCTAX")
	private BigDecimal amtExctax;

	@Column(name="CHG_RTO")
	private BigDecimal chgRto;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DC_TP")
	private String dcTp;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="DPT_CD")
	private String dptCd;

	@Column(name="DTL_LNNO")
	private BigDecimal dtlLnno;

	@Column(name="FNC_CD1")
	private String fncCd1;

	@Column(name="FNC_CD2")
	private String fncCd2;

	@Column(name="FNC_CD3")
	private String fncCd3;

	@Column(name="FNC_CD4")
	private String fncCd4;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="JRN_AMT")
	private BigDecimal jrnAmt;

	@Column(name="PRJ_CD")
	private String prjCd;

	@Column(name="PRTN_ACC_CD")
	private String prtnAccCd;

	@Column(name="RCRD_TP")
	private String rcrdTp;

	@Column(name="RTO_TP")
	private String rtoTp;

	@Temporal(TemporalType.DATE)
	@Column(name="SLP_DT")
	private Date slpDt;

	@Column(name="SLP_GRP")
	private String slpGrp;

	@Column(name="SLP_NO")
	private String slpNo;

	private String smry1;

	private String smry2;

	@Column(name="SPLR_PRS_CD")
	private String splrPrsCd;

	@Column(name="SPLR_PRS_TP")
	private String splrPrsTp;

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

	@Column(name="TAX_AMT")
	private BigDecimal taxAmt;

	@Column(name="TAX_CD")
	private String taxCd;

	@Column(name="TAX_IPT_TP")
	private String taxIptTp;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="TRD_MNY_AMT")
	private BigDecimal trdMnyAmt;

	@Column(name="TRD_MNY_CD")
	private String trdMnyCd;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public SsglSndInfDt() {
	}

	public SsglSndInfDtPK getId() {
		return this.id;
	}

	public void setId(SsglSndInfDtPK id) {
		this.id = id;
	}

	public String getAccBrkdwnCd() {
		return this.accBrkdwnCd;
	}

	public void setAccBrkdwnCd(String accBrkdwnCd) {
		this.accBrkdwnCd = accBrkdwnCd;
	}

	public String getAccCd() {
		return this.accCd;
	}

	public void setAccCd(String accCd) {
		this.accCd = accCd;
	}

	public BigDecimal getAmtExctax() {
		return this.amtExctax;
	}

	public void setAmtExctax(BigDecimal amtExctax) {
		this.amtExctax = amtExctax;
	}

	public BigDecimal getChgRto() {
		return this.chgRto;
	}

	public void setChgRto(BigDecimal chgRto) {
		this.chgRto = chgRto;
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

	public String getDcTp() {
		return this.dcTp;
	}

	public void setDcTp(String dcTp) {
		this.dcTp = dcTp;
	}

	public String getDltFg() {
		return this.dltFg;
	}

	public void setDltFg(String dltFg) {
		this.dltFg = dltFg;
	}

	public String getDptCd() {
		return this.dptCd;
	}

	public void setDptCd(String dptCd) {
		this.dptCd = dptCd;
	}

	public BigDecimal getDtlLnno() {
		return this.dtlLnno;
	}

	public void setDtlLnno(BigDecimal dtlLnno) {
		this.dtlLnno = dtlLnno;
	}

	public String getFncCd1() {
		return this.fncCd1;
	}

	public void setFncCd1(String fncCd1) {
		this.fncCd1 = fncCd1;
	}

	public String getFncCd2() {
		return this.fncCd2;
	}

	public void setFncCd2(String fncCd2) {
		this.fncCd2 = fncCd2;
	}

	public String getFncCd3() {
		return this.fncCd3;
	}

	public void setFncCd3(String fncCd3) {
		this.fncCd3 = fncCd3;
	}

	public String getFncCd4() {
		return this.fncCd4;
	}

	public void setFncCd4(String fncCd4) {
		this.fncCd4 = fncCd4;
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

	public BigDecimal getJrnAmt() {
		return this.jrnAmt;
	}

	public void setJrnAmt(BigDecimal jrnAmt) {
		this.jrnAmt = jrnAmt;
	}

	public String getPrjCd() {
		return this.prjCd;
	}

	public void setPrjCd(String prjCd) {
		this.prjCd = prjCd;
	}

	public String getPrtnAccCd() {
		return this.prtnAccCd;
	}

	public void setPrtnAccCd(String prtnAccCd) {
		this.prtnAccCd = prtnAccCd;
	}

	public String getRcrdTp() {
		return this.rcrdTp;
	}

	public void setRcrdTp(String rcrdTp) {
		this.rcrdTp = rcrdTp;
	}

	public String getRtoTp() {
		return this.rtoTp;
	}

	public void setRtoTp(String rtoTp) {
		this.rtoTp = rtoTp;
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

	public String getSmry1() {
		return this.smry1;
	}

	public void setSmry1(String smry1) {
		this.smry1 = smry1;
	}

	public String getSmry2() {
		return this.smry2;
	}

	public void setSmry2(String smry2) {
		this.smry2 = smry2;
	}

	public String getSplrPrsCd() {
		return this.splrPrsCd;
	}

	public void setSplrPrsCd(String splrPrsCd) {
		this.splrPrsCd = splrPrsCd;
	}

	public String getSplrPrsTp() {
		return this.splrPrsTp;
	}

	public void setSplrPrsTp(String splrPrsTp) {
		this.splrPrsTp = splrPrsTp;
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

	public BigDecimal getTaxAmt() {
		return this.taxAmt;
	}

	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
	}

	public String getTaxCd() {
		return this.taxCd;
	}

	public void setTaxCd(String taxCd) {
		this.taxCd = taxCd;
	}

	public String getTaxIptTp() {
		return this.taxIptTp;
	}

	public void setTaxIptTp(String taxIptTp) {
		this.taxIptTp = taxIptTp;
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

	public BigDecimal getTrdMnyAmt() {
		return this.trdMnyAmt;
	}

	public void setTrdMnyAmt(BigDecimal trdMnyAmt) {
		this.trdMnyAmt = trdMnyAmt;
	}

	public String getTrdMnyCd() {
		return this.trdMnyCd;
	}

	public void setTrdMnyCd(String trdMnyCd) {
		this.trdMnyCd = trdMnyCd;
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