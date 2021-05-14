package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the SSAP_SND_INF_HD database table.
 * 
 */
@Entity
@Table(name="SSAP_SND_INF_HD")
@NamedQuery(name="SsapSndInfHd.findAll", query="SELECT s FROM SsapSndInfHd s")
public class SsapSndInfHd extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SsapSndInfHdPK id;

	@Column(name="AP_SLT_TP")
	private String apSltTp;

	@Column(name="BUYER_CD")
	private String buyerCd;

	@Column(name="CHG_RTO")
	private BigDecimal chgRto;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DC_RVR_TP")
	private String dcRvrTp;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="ELS_SYS_SLP_NO")
	private String elsSysSlpNo;

	@Temporal(TemporalType.DATE)
	@Column(name="INVSHT_DT")
	private Date invshtDt;

	@Column(name="INVSHT_NO")
	private String invshtNo;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Temporal(TemporalType.DATE)
	@Column(name="ISSVCHER_DT")
	private Date issvcherDt;

	@Column(name="ISSVCHER_ID")
	private String issvcherId;

	@Column(name="MNY_CD")
	private String mnyCd;

	@Temporal(TemporalType.DATE)
	@Column(name="ORG_SLP_DT")
	private Date orgSlpDt;

	@Column(name="ORG_SLP_GRP")
	private String orgSlpGrp;

	@Column(name="ORG_SLP_NO")
	private String orgSlpNo;

	@Column(name="PAY_APPL_CD")
	private String payApplCd;

	@Column(name="PAYER_TP")
	private String payerTp;

	@Column(name="R_SLP_TP")
	private String rSlpTp;

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

	@Column(name="SLP_SMRY")
	private String slpSmry;

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

	@Column(name="SPT_PAY_TP")
	private String sptPayTp;

	@Column(name="SS_COMPANY_CD")
	private String ssCompanyCd;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public SsapSndInfHd() {
	}

	public SsapSndInfHdPK getId() {
		return this.id;
	}

	public void setId(SsapSndInfHdPK id) {
		this.id = id;
	}

	public String getApSltTp() {
		return this.apSltTp;
	}

	public void setApSltTp(String apSltTp) {
		this.apSltTp = apSltTp;
	}

	public String getBuyerCd() {
		return this.buyerCd;
	}

	public void setBuyerCd(String buyerCd) {
		this.buyerCd = buyerCd;
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

	public String getDcRvrTp() {
		return this.dcRvrTp;
	}

	public void setDcRvrTp(String dcRvrTp) {
		this.dcRvrTp = dcRvrTp;
	}

	public String getDltFg() {
		return this.dltFg;
	}

	public void setDltFg(String dltFg) {
		this.dltFg = dltFg;
	}

	public String getElsSysSlpNo() {
		return this.elsSysSlpNo;
	}

	public void setElsSysSlpNo(String elsSysSlpNo) {
		this.elsSysSlpNo = elsSysSlpNo;
	}

	public Date getInvshtDt() {
		return this.invshtDt;
	}

	public void setInvshtDt(Date invshtDt) {
		this.invshtDt = invshtDt;
	}

	public String getInvshtNo() {
		return this.invshtNo;
	}

	public void setInvshtNo(String invshtNo) {
		this.invshtNo = invshtNo;
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

	public Date getIssvcherDt() {
		return this.issvcherDt;
	}

	public void setIssvcherDt(Date issvcherDt) {
		this.issvcherDt = issvcherDt;
	}

	public String getIssvcherId() {
		return this.issvcherId;
	}

	public void setIssvcherId(String issvcherId) {
		this.issvcherId = issvcherId;
	}

	public String getMnyCd() {
		return this.mnyCd;
	}

	public void setMnyCd(String mnyCd) {
		this.mnyCd = mnyCd;
	}

	public Date getOrgSlpDt() {
		return this.orgSlpDt;
	}

	public void setOrgSlpDt(Date orgSlpDt) {
		this.orgSlpDt = orgSlpDt;
	}

	public String getOrgSlpGrp() {
		return this.orgSlpGrp;
	}

	public void setOrgSlpGrp(String orgSlpGrp) {
		this.orgSlpGrp = orgSlpGrp;
	}

	public String getOrgSlpNo() {
		return this.orgSlpNo;
	}

	public void setOrgSlpNo(String orgSlpNo) {
		this.orgSlpNo = orgSlpNo;
	}

	public String getPayApplCd() {
		return this.payApplCd;
	}

	public void setPayApplCd(String payApplCd) {
		this.payApplCd = payApplCd;
	}

	public String getPayerTp() {
		return this.payerTp;
	}

	public void setPayerTp(String payerTp) {
		this.payerTp = payerTp;
	}

	public String getRSlpTp() {
		return this.rSlpTp;
	}

	public void setRSlpTp(String rSlpTp) {
		this.rSlpTp = rSlpTp;
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

	public String getSlpSmry() {
		return this.slpSmry;
	}

	public void setSlpSmry(String slpSmry) {
		this.slpSmry = slpSmry;
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

	public String getSptPayTp() {
		return this.sptPayTp;
	}

	public void setSptPayTp(String sptPayTp) {
		this.sptPayTp = sptPayTp;
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