package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the SPLR_MST database table.
 *
 */
@Entity
@Table(name="SPLR_MST")
@NamedQuery(name="SplrMst.findAll", query="SELECT s FROM SplrMst s")
public class SplrMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SplrMstPK id;

	@Column(name="ABST_IN")
	private String abstIn;

	@Column(name="ADR_PRF")
	private String adrPrf;

	@Column(name="ADR_PRF_CD")
	private String adrPrfCd;

	private String adr1;

	private String adr2;

	private String adr3;

	@Column(name="AFFCMP_TP")
	private String affcmpTp;

	@Temporal(TemporalType.DATE)
	@Column(name="BRTH_DT")
	private Date brthDt;

	@Column(name="BUMON_CD")
	private String bumonCd;

	@Column(name="CMPT_ETR_NO")
	private String cmptEtrNo;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	private BigDecimal cptl;

	@Column(name="CRP_NO")
	private String crpNo;

	@Column(name="CRP_PRS_TP")
	private String crpPrsTp;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="DMS_ABR_TP")
	private String dmsAbrTp;

	@Column(name="FAX_NO")
	private String faxNo;

	@Column(name="HLD_TRT_TP")
	private String hldTrtTp;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="LAST_JDG_RMK")
	private String lastJdgRmk;

	@Column(name="LAST_JDG_TP")
	private String lastJdgTp;

	@Column(name="LND_CD")
	private String lndCd;

	@Column(name="LND_NM")
	private String lndNm;

	private String mladr1;

	private String mladr2;

	@Column(name="MYNUM_ID")
	private String mynumId;

	@Column(name="PAY_BSN_CD")
	private String payBsnCd;

	@Column(name="PAY_CMM_OBL_TP")
	private String payCmmOblTp;

	@Column(name="PAY_CMM_SBT_TP")
	private String payCmmSbtTp;

	@Column(name="PAY_COND_CD")
	private String payCondCd;

	@Column(name="PAY_COND_NM")
	private String payCondNm;

	private String rmk;

	@Column(name="SPLR_DPT_NM")
	private String splrDptNm;

	@Column(name="SPLR_NM_E")
	private String splrNmE;

	@Column(name="SPLR_NM_KJ")
	private String splrNmKj;

	@Column(name="SPLR_NM_KN")
	private String splrNmKn;

	@Column(name="SPLR_NM_S")
	private String splrNmS;

	@Column(name="SPLR_PIC_NM")
	private String splrPicNm;

	@Column(name="SPLR_PST_NM")
	private String splrPstNm;

	@Column(name="SUB_CNTRCT_TP")
	private String subCntrctTp;

	@Column(name="TEL_NO")
	private String telNo;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="TRD_STS_TP")
	private String trdStsTp;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	@Temporal(TemporalType.DATE)
	@Column(name="VD_DT_E")
	private Date vdDtE;

	@Temporal(TemporalType.DATE)
	@Column(name="VD_DT_S")
	private Date vdDtS;

	@Column(name="ZIP_CD")
	private String zipCd;

	public SplrMst() {
	}

	public SplrMstPK getId() {
		return this.id;
	}

	public void setId(SplrMstPK id) {
		this.id = id;
	}

	public String getAbstIn() {
		return this.abstIn;
	}

	public void setAbstIn(String abstIn) {
		this.abstIn = abstIn;
	}

	public String getAdrPrf() {
		return this.adrPrf;
	}

	public void setAdrPrf(String adrPrf) {
		this.adrPrf = adrPrf;
	}

	public String getAdrPrfCd() {
		return this.adrPrfCd;
	}

	public void setAdrPrfCd(String adrPrfCd) {
		this.adrPrfCd = adrPrfCd;
	}

	public String getAdr1() {
		return this.adr1;
	}

	public void setAdr1(String adr1) {
		this.adr1 = adr1;
	}

	public String getAdr2() {
		return this.adr2;
	}

	public void setAdr2(String adr2) {
		this.adr2 = adr2;
	}

	public String getAdr3() {
		return this.adr3;
	}

	public void setAdr3(String adr3) {
		this.adr3 = adr3;
	}

	public String getAffcmpTp() {
		return this.affcmpTp;
	}

	public void setAffcmpTp(String affcmpTp) {
		this.affcmpTp = affcmpTp;
	}

	public Date getBrthDt() {
		return this.brthDt;
	}

	public void setBrthDt(Date brthDt) {
		this.brthDt = brthDt;
	}

	public String getBumonCd() {
		return this.bumonCd;
	}

	public void setBumonCd(String bumonCd) {
		this.bumonCd = bumonCd;
	}

	public String getCmptEtrNo() {
		return this.cmptEtrNo;
	}

	public void setCmptEtrNo(String cmptEtrNo) {
		this.cmptEtrNo = cmptEtrNo;
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

	public BigDecimal getCptl() {
		return this.cptl;
	}

	public void setCptl(BigDecimal cptl) {
		this.cptl = cptl;
	}

	public String getCrpNo() {
		return this.crpNo;
	}

	public void setCrpNo(String crpNo) {
		this.crpNo = crpNo;
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

	public String getFaxNo() {
		return this.faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getHldTrtTp() {
		return this.hldTrtTp;
	}

	public void setHldTrtTp(String hldTrtTp) {
		this.hldTrtTp = hldTrtTp;
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

	public String getLastJdgRmk() {
		return this.lastJdgRmk;
	}

	public void setLastJdgRmk(String lastJdgRmk) {
		this.lastJdgRmk = lastJdgRmk;
	}

	public String getLastJdgTp() {
		return this.lastJdgTp;
	}

	public void setLastJdgTp(String lastJdgTp) {
		this.lastJdgTp = lastJdgTp;
	}

	public String getLndCd() {
		return this.lndCd;
	}

	public void setLndCd(String lndCd) {
		this.lndCd = lndCd;
	}

	public String getLndNm() {
		return this.lndNm;
	}

	public void setLndNm(String lndNm) {
		this.lndNm = lndNm;
	}

	public String getMladr1() {
		return this.mladr1;
	}

	public void setMladr1(String mladr1) {
		this.mladr1 = mladr1;
	}

	public String getMladr2() {
		return this.mladr2;
	}

	public void setMladr2(String mladr2) {
		this.mladr2 = mladr2;
	}

	public String getMynumId() {
		return this.mynumId;
	}

	public void setMynumId(String mynumId) {
		this.mynumId = mynumId;
	}

	public String getPayBsnCd() {
		return this.payBsnCd;
	}

	public void setPayBsnCd(String payBsnCd) {
		this.payBsnCd = payBsnCd;
	}

	public String getPayCmmOblTp() {
		return this.payCmmOblTp;
	}

	public void setPayCmmOblTp(String payCmmOblTp) {
		this.payCmmOblTp = payCmmOblTp;
	}

	public String getPayCmmSbtTp() {
		return this.payCmmSbtTp;
	}

	public void setPayCmmSbtTp(String payCmmSbtTp) {
		this.payCmmSbtTp = payCmmSbtTp;
	}

	public String getPayCondCd() {
		return this.payCondCd;
	}

	public void setPayCondCd(String payCondCd) {
		this.payCondCd = payCondCd;
	}

	public String getPayCondNm() {
		return this.payCondNm;
	}

	public void setPayCondNm(String payCondNm) {
		this.payCondNm = payCondNm;
	}

	public String getRmk() {
		return this.rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getSplrDptNm() {
		return this.splrDptNm;
	}

	public void setSplrDptNm(String splrDptNm) {
		this.splrDptNm = splrDptNm;
	}

	public String getSplrNmE() {
		return this.splrNmE;
	}

	public void setSplrNmE(String splrNmE) {
		this.splrNmE = splrNmE;
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

	public String getSplrNmS() {
		return this.splrNmS;
	}

	public void setSplrNmS(String splrNmS) {
		this.splrNmS = splrNmS;
	}

	public String getSplrPicNm() {
		return this.splrPicNm;
	}

	public void setSplrPicNm(String splrPicNm) {
		this.splrPicNm = splrPicNm;
	}

	public String getSplrPstNm() {
		return this.splrPstNm;
	}

	public void setSplrPstNm(String splrPstNm) {
		this.splrPstNm = splrPstNm;
	}

	public String getSubCntrctTp() {
		return this.subCntrctTp;
	}

	public void setSubCntrctTp(String subCntrctTp) {
		this.subCntrctTp = subCntrctTp;
	}

	public String getTelNo() {
		return this.telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
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

	public String getTrdStsTp() {
		return this.trdStsTp;
	}

	public void setTrdStsTp(String trdStsTp) {
		this.trdStsTp = trdStsTp;
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

	public Date getVdDtE() {
		return this.vdDtE;
	}

	public void setVdDtE(Date vdDtE) {
		this.vdDtE = vdDtE;
	}

	public Date getVdDtS() {
		return this.vdDtS;
	}

	public void setVdDtS(Date vdDtS) {
		this.vdDtS = vdDtS;
	}

	public String getZipCd() {
		return this.zipCd;
	}

	public void setZipCd(String zipCd) {
		this.zipCd = zipCd;
	}

}