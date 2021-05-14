package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the CNTRCTDTL_INF database table.
 * 
 */
@Entity
@Table(name="CNTRCTDTL_INF")
@NamedQuery(name="CntrctdtlInf.findAll", query="SELECT c FROM CntrctdtlInf c")
public class CntrctdtlInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CntrctdtlInfPK id;

	@Column(name="ADD_RTO")
	private BigDecimal addRto;

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

	@Column(name="BRKDWN_TP")
	private String brkdwnTp;

	@Column(name="BUMON_CD")
	private String bumonCd;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CTRCT_TP")
	private String ctrctTp;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="HLDTAX_AMT")
	private BigDecimal hldtaxAmt;

	@Column(name="HLDTAX_RTO")
	private BigDecimal hldtaxRto;

	@Column(name="HLDTAX_TP")
	private String hldtaxTp;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="ITM_CD")
	private String itmCd;

	@Column(name="ITM_NM")
	private String itmNm;

	@Column(name="ITMEXPS_CD1")
	private String itmexpsCd1;

	@Column(name="ITMEXPS_CD2")
	private String itmexpsCd2;

	@Column(name="MNY_CD")
	private String mnyCd;

	@Column(name="ORGNZ_CD")
	private String orgnzCd;

	@Column(name="PAY_AMT_FC")
	private BigDecimal payAmtFc;

	@Column(name="PAY_AMT_JPY")
	private BigDecimal payAmtJpy;

	@Column(name="PAY_SMRY")
	private String paySmry;

	@Column(name="TAX_CD")
	private String taxCd;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public CntrctdtlInf() {
	}

	public CntrctdtlInfPK getId() {
		return this.id;
	}

	public void setId(CntrctdtlInfPK id) {
		this.id = id;
	}

	public BigDecimal getAddRto() {
		return this.addRto;
	}

	public void setAddRto(BigDecimal addRto) {
		this.addRto = addRto;
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

	public String getMnyCd() {
		return this.mnyCd;
	}

	public void setMnyCd(String mnyCd) {
		this.mnyCd = mnyCd;
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

	public String getPaySmry() {
		return this.paySmry;
	}

	public void setPaySmry(String paySmry) {
		this.paySmry = paySmry;
	}

	public String getTaxCd() {
		return this.taxCd;
	}

	public void setTaxCd(String taxCd) {
		this.taxCd = taxCd;
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