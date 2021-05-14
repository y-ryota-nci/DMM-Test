package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the KNTN_INF database table.
 * 
 */
@Entity
@Table(name="KNTN_INF")
@NamedQuery(name="KntnInf.findAll", query="SELECT k FROM KntnInf k")
public class KntnInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private KntnInfPK id;

	@Column(name="ADJ_BASE_AMT")
	private BigDecimal adjBaseAmt;

	@Column(name="ADJ_TRNSP_EXP_AMT")
	private BigDecimal adjTrnspExpAmt;

	@Column(name="ANLYS_CD")
	private String anlysCd;

	@Column(name="BASE_AMT")
	private BigDecimal baseAmt;

	@Column(name="BUMON_CD")
	private String bumonCd;

	@Column(name="BUY_NO")
	private String buyNo;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="EVNT_CONT")
	private String evntCont;

	@Column(name="EVNT_MNG_NO")
	private String evntMngNo;

	@Column(name="EVNT_NO")
	private String evntNo;

	@Temporal(TemporalType.DATE)
	@Column(name="EXHB_DT")
	private Date exhbDt;

	@Column(name="EXHB_YM")
	private String exhbYm;

	@Column(name="HLL_NM")
	private String hllNm;

	@Column(name="INV_AMT")
	private BigDecimal invAmt;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="ITMEXPS_CD1")
	private String itmexpsCd1;

	@Column(name="ITMEXPS_CD2")
	private String itmexpsCd2;

	@Column(name="KNTN_HLL_ID")
	private String kntnHllId;

	@Column(name="KNTN_STS")
	private String kntnSts;

	@Column(name="MNSCR_EXP_AMT")
	private BigDecimal mnscrExpAmt;

	@Column(name="PAY_NO")
	private String payNo;

	@Column(name="PRDCT_ID")
	private String prdctId;

	@Column(name="PRDCT_NM")
	private String prdctNm;

	@Column(name="PURORD_NO")
	private String purordNo;

	@Column(name="RCVINSP_NO")
	private String rcvinspNo;

	@Column(name="REC_NO")
	private BigDecimal recNo;

	private String smry;

	@Column(name="SPLR_CD")
	private String splrCd;

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

	@Column(name="TLNT_NM")
	private String tlntNm;

	@Column(name="TRNSP_EXP_AMT")
	private BigDecimal trnspExpAmt;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public KntnInf() {
	}

	public KntnInfPK getId() {
		return this.id;
	}

	public void setId(KntnInfPK id) {
		this.id = id;
	}

	public BigDecimal getAdjBaseAmt() {
		return this.adjBaseAmt;
	}

	public void setAdjBaseAmt(BigDecimal adjBaseAmt) {
		this.adjBaseAmt = adjBaseAmt;
	}

	public BigDecimal getAdjTrnspExpAmt() {
		return this.adjTrnspExpAmt;
	}

	public void setAdjTrnspExpAmt(BigDecimal adjTrnspExpAmt) {
		this.adjTrnspExpAmt = adjTrnspExpAmt;
	}

	public String getAnlysCd() {
		return this.anlysCd;
	}

	public void setAnlysCd(String anlysCd) {
		this.anlysCd = anlysCd;
	}

	public BigDecimal getBaseAmt() {
		return this.baseAmt;
	}

	public void setBaseAmt(BigDecimal baseAmt) {
		this.baseAmt = baseAmt;
	}

	public String getBumonCd() {
		return this.bumonCd;
	}

	public void setBumonCd(String bumonCd) {
		this.bumonCd = bumonCd;
	}

	public String getBuyNo() {
		return this.buyNo;
	}

	public void setBuyNo(String buyNo) {
		this.buyNo = buyNo;
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

	public String getEvntCont() {
		return this.evntCont;
	}

	public void setEvntCont(String evntCont) {
		this.evntCont = evntCont;
	}

	public String getEvntMngNo() {
		return this.evntMngNo;
	}

	public void setEvntMngNo(String evntMngNo) {
		this.evntMngNo = evntMngNo;
	}

	public String getEvntNo() {
		return this.evntNo;
	}

	public void setEvntNo(String evntNo) {
		this.evntNo = evntNo;
	}

	public Date getExhbDt() {
		return this.exhbDt;
	}

	public void setExhbDt(Date exhbDt) {
		this.exhbDt = exhbDt;
	}

	public String getExhbYm() {
		return this.exhbYm;
	}

	public void setExhbYm(String exhbYm) {
		this.exhbYm = exhbYm;
	}

	public String getHllNm() {
		return this.hllNm;
	}

	public void setHllNm(String hllNm) {
		this.hllNm = hllNm;
	}

	public BigDecimal getInvAmt() {
		return this.invAmt;
	}

	public void setInvAmt(BigDecimal invAmt) {
		this.invAmt = invAmt;
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

	public String getKntnHllId() {
		return this.kntnHllId;
	}

	public void setKntnHllId(String kntnHllId) {
		this.kntnHllId = kntnHllId;
	}

	public String getKntnSts() {
		return this.kntnSts;
	}

	public void setKntnSts(String kntnSts) {
		this.kntnSts = kntnSts;
	}

	public BigDecimal getMnscrExpAmt() {
		return this.mnscrExpAmt;
	}

	public void setMnscrExpAmt(BigDecimal mnscrExpAmt) {
		this.mnscrExpAmt = mnscrExpAmt;
	}

	public String getPayNo() {
		return this.payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getPrdctId() {
		return this.prdctId;
	}

	public void setPrdctId(String prdctId) {
		this.prdctId = prdctId;
	}

	public String getPrdctNm() {
		return this.prdctNm;
	}

	public void setPrdctNm(String prdctNm) {
		this.prdctNm = prdctNm;
	}

	public String getPurordNo() {
		return this.purordNo;
	}

	public void setPurordNo(String purordNo) {
		this.purordNo = purordNo;
	}

	public String getRcvinspNo() {
		return this.rcvinspNo;
	}

	public void setRcvinspNo(String rcvinspNo) {
		this.rcvinspNo = rcvinspNo;
	}

	public BigDecimal getRecNo() {
		return this.recNo;
	}

	public void setRecNo(BigDecimal recNo) {
		this.recNo = recNo;
	}

	public String getSmry() {
		return this.smry;
	}

	public void setSmry(String smry) {
		this.smry = smry;
	}

	public String getSplrCd() {
		return this.splrCd;
	}

	public void setSplrCd(String splrCd) {
		this.splrCd = splrCd;
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

	public String getTlntNm() {
		return this.tlntNm;
	}

	public void setTlntNm(String tlntNm) {
		this.tlntNm = tlntNm;
	}

	public BigDecimal getTrnspExpAmt() {
		return this.trnspExpAmt;
	}

	public void setTrnspExpAmt(BigDecimal trnspExpAmt) {
		this.trnspExpAmt = trnspExpAmt;
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