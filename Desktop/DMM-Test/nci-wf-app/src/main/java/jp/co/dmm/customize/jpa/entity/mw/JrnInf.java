package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the JRN_INF database table.
 * 
 */
@Entity
@Table(name="JRN_INF")
@NamedQuery(name="JrnInf.findAll", query="SELECT j FROM JrnInf j")
public class JrnInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private JrnInfPK id;

	private String abst1;

	private String abst2;

	@Column(name="ACC_BRKDWN_CD")
	private String accBrkdwnCd;

	@Column(name="ACC_BRKDWN_X")
	private BigDecimal accBrkdwnX;

	@Column(name="ACC_CD")
	private String accCd;

	@Column(name="ACC_DPT_CD")
	private String accDptCd;

	@Column(name="ACC_DPT_X")
	private BigDecimal accDptX;

	@Column(name="ACC_X")
	private BigDecimal accX;

	@Temporal(TemporalType.DATE)
	@Column(name="ADD_DT")
	private Date addDt;

	@Column(name="ADD_RTO")
	private BigDecimal addRto;

	@Column(name="ADVPAY_NO")
	private String advpayNo;

	@Column(name="AMT_FC")
	private BigDecimal amtFc;

	@Column(name="AMT_JPY")
	private BigDecimal amtJpy;

	@Column(name="BUY_DTL_NO")
	private BigDecimal buyDtlNo;

	@Column(name="BUY_NO")
	private String buyNo;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DC_TP")
	private String dcTp;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="JRN_SUB_NO")
	private String jrnSubNo;

	@Column(name="JRN_SUB_TP")
	private String jrnSubTp;

	@Column(name="JRN_TP")
	private String jrnTp;

	@Column(name="JRNSLP_STS")
	private String jrnslpSts;

	@Column(name="MK_SYS")
	private String mkSys;

	@Column(name="MNY_CD")
	private String mnyCd;

	@Column(name="MT_COMP_TP")
	private String mtCompTp;

	@Column(name="ORD_CD")
	private String ordCd;

	@Column(name="PAY_DTL_NO")
	private BigDecimal payDtlNo;

	@Column(name="PAY_NO")
	private String payNo;

	@Column(name="PRTN_ACC_BRKDWN_CD")
	private String prtnAccBrkdwnCd;

	@Column(name="PRTN_ACC_BRKDWN_X")
	private BigDecimal prtnAccBrkdwnX;

	@Column(name="PRTN_ACC_CD")
	private String prtnAccCd;

	@Column(name="PRTN_ACC_X")
	private BigDecimal prtnAccX;

	@Column(name="RB_TP")
	private String rbTp;

	@Column(name="SAV_AMT_JPY")
	private BigDecimal savAmtJpy;

	@Column(name="SPLR_CD")
	private String splrCd;

	@Column(name="SPLR_NM_KJ")
	private String splrNmKj;

	@Column(name="SPLR_NM_KN")
	private String splrNmKn;

	@Column(name="SPLR_RMT_CD")
	private String splrRmtCd;

	@Column(name="SPLR_RMT_X")
	private BigDecimal splrRmtX;

	@Column(name="STF_CD")
	private String stfCd;

	@Column(name="STF_X")
	private BigDecimal stfX;

	@Column(name="TAX_AMT")
	private BigDecimal taxAmt;

	@Column(name="TAX_CD")
	private String taxCd;

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

	public JrnInf() {
	}

	public JrnInfPK getId() {
		return this.id;
	}

	public void setId(JrnInfPK id) {
		this.id = id;
	}

	public String getAbst1() {
		return this.abst1;
	}

	public void setAbst1(String abst1) {
		this.abst1 = abst1;
	}

	public String getAbst2() {
		return this.abst2;
	}

	public void setAbst2(String abst2) {
		this.abst2 = abst2;
	}

	public String getAccBrkdwnCd() {
		return this.accBrkdwnCd;
	}

	public void setAccBrkdwnCd(String accBrkdwnCd) {
		this.accBrkdwnCd = accBrkdwnCd;
	}

	public BigDecimal getAccBrkdwnX() {
		return this.accBrkdwnX;
	}

	public void setAccBrkdwnX(BigDecimal accBrkdwnX) {
		this.accBrkdwnX = accBrkdwnX;
	}

	public String getAccCd() {
		return this.accCd;
	}

	public void setAccCd(String accCd) {
		this.accCd = accCd;
	}

	public String getAccDptCd() {
		return this.accDptCd;
	}

	public void setAccDptCd(String accDptCd) {
		this.accDptCd = accDptCd;
	}

	public BigDecimal getAccDptX() {
		return this.accDptX;
	}

	public void setAccDptX(BigDecimal accDptX) {
		this.accDptX = accDptX;
	}

	public BigDecimal getAccX() {
		return this.accX;
	}

	public void setAccX(BigDecimal accX) {
		this.accX = accX;
	}

	public Date getAddDt() {
		return this.addDt;
	}

	public void setAddDt(Date addDt) {
		this.addDt = addDt;
	}

	public BigDecimal getAddRto() {
		return this.addRto;
	}

	public void setAddRto(BigDecimal addRto) {
		this.addRto = addRto;
	}

	public String getAdvpayNo() {
		return this.advpayNo;
	}

	public void setAdvpayNo(String advpayNo) {
		this.advpayNo = advpayNo;
	}

	public BigDecimal getAmtFc() {
		return this.amtFc;
	}

	public void setAmtFc(BigDecimal amtFc) {
		this.amtFc = amtFc;
	}

	public BigDecimal getAmtJpy() {
		return this.amtJpy;
	}

	public void setAmtJpy(BigDecimal amtJpy) {
		this.amtJpy = amtJpy;
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

	public String getJrnSubNo() {
		return this.jrnSubNo;
	}

	public void setJrnSubNo(String jrnSubNo) {
		this.jrnSubNo = jrnSubNo;
	}

	public String getJrnSubTp() {
		return this.jrnSubTp;
	}

	public void setJrnSubTp(String jrnSubTp) {
		this.jrnSubTp = jrnSubTp;
	}

	public String getJrnTp() {
		return this.jrnTp;
	}

	public void setJrnTp(String jrnTp) {
		this.jrnTp = jrnTp;
	}

	public String getJrnslpSts() {
		return this.jrnslpSts;
	}

	public void setJrnslpSts(String jrnslpSts) {
		this.jrnslpSts = jrnslpSts;
	}

	public String getMkSys() {
		return this.mkSys;
	}

	public void setMkSys(String mkSys) {
		this.mkSys = mkSys;
	}

	public String getMnyCd() {
		return this.mnyCd;
	}

	public void setMnyCd(String mnyCd) {
		this.mnyCd = mnyCd;
	}

	public String getMtCompTp() {
		return this.mtCompTp;
	}

	public void setMtCompTp(String mtCompTp) {
		this.mtCompTp = mtCompTp;
	}

	public String getOrdCd() {
		return this.ordCd;
	}

	public void setOrdCd(String ordCd) {
		this.ordCd = ordCd;
	}

	public BigDecimal getPayDtlNo() {
		return this.payDtlNo;
	}

	public void setPayDtlNo(BigDecimal payDtlNo) {
		this.payDtlNo = payDtlNo;
	}

	public String getPayNo() {
		return this.payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getPrtnAccBrkdwnCd() {
		return this.prtnAccBrkdwnCd;
	}

	public void setPrtnAccBrkdwnCd(String prtnAccBrkdwnCd) {
		this.prtnAccBrkdwnCd = prtnAccBrkdwnCd;
	}

	public BigDecimal getPrtnAccBrkdwnX() {
		return this.prtnAccBrkdwnX;
	}

	public void setPrtnAccBrkdwnX(BigDecimal prtnAccBrkdwnX) {
		this.prtnAccBrkdwnX = prtnAccBrkdwnX;
	}

	public String getPrtnAccCd() {
		return this.prtnAccCd;
	}

	public void setPrtnAccCd(String prtnAccCd) {
		this.prtnAccCd = prtnAccCd;
	}

	public BigDecimal getPrtnAccX() {
		return this.prtnAccX;
	}

	public void setPrtnAccX(BigDecimal prtnAccX) {
		this.prtnAccX = prtnAccX;
	}

	public String getRbTp() {
		return this.rbTp;
	}

	public void setRbTp(String rbTp) {
		this.rbTp = rbTp;
	}

	public BigDecimal getSavAmtJpy() {
		return this.savAmtJpy;
	}

	public void setSavAmtJpy(BigDecimal savAmtJpy) {
		this.savAmtJpy = savAmtJpy;
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

	public String getSplrRmtCd() {
		return this.splrRmtCd;
	}

	public void setSplrRmtCd(String splrRmtCd) {
		this.splrRmtCd = splrRmtCd;
	}

	public BigDecimal getSplrRmtX() {
		return this.splrRmtX;
	}

	public void setSplrRmtX(BigDecimal splrRmtX) {
		this.splrRmtX = splrRmtX;
	}

	public String getStfCd() {
		return this.stfCd;
	}

	public void setStfCd(String stfCd) {
		this.stfCd = stfCd;
	}

	public BigDecimal getStfX() {
		return this.stfX;
	}

	public void setStfX(BigDecimal stfX) {
		this.stfX = stfX;
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