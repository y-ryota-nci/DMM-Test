package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the MLORD_INF database table.
 * 
 */
@Entity
@Table(name="MLORD_INF")
@NamedQuery(name="MlordInf.findAll", query="SELECT m FROM MlordInf m")
public class MlordInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MlordInfPK id;

	private String abst;

	private BigDecimal amt;

	@Column(name="BUMON_CD")
	private String bumonCd;

	@Column(name="BUY_CD")
	private String buyCd;

	@Temporal(TemporalType.DATE)
	@Column(name="BUY_DT")
	private Date buyDt;

	@Column(name="BUY_NM_KJ")
	private String buyNmKj;

	@Column(name="BUY_NO")
	private String buyNo;

	@Column(name="CMMDT_CD")
	private String cmmdtCd;

	@Column(name="CMMDT_TTL")
	private String cmmdtTtl;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="IN_YM")
	private String inYm;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="ITMEXPS_CD1")
	private String itmexpsCd1;

	@Column(name="ITMEXPS_CD2")
	private String itmexpsCd2;

	@Column(name="LN_NO")
	private BigDecimal lnNo;

	@Column(name="PAY_NO")
	private String payNo;

	@Column(name="PRT_NO")
	private String prtNo;

	@Column(name="PURORD_NO")
	private String purordNo;

	private BigDecimal qnt;

	@Column(name="RCVINSP_NO")
	private String rcvinspNo;

	private String rmk;

	@Column(name="SLP_NO")
	private String slpNo;

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

	private BigDecimal uc;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public MlordInf() {
	}

	public MlordInfPK getId() {
		return this.id;
	}

	public void setId(MlordInfPK id) {
		this.id = id;
	}

	public String getAbst() {
		return this.abst;
	}

	public void setAbst(String abst) {
		this.abst = abst;
	}

	public BigDecimal getAmt() {
		return this.amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public String getBumonCd() {
		return this.bumonCd;
	}

	public void setBumonCd(String bumonCd) {
		this.bumonCd = bumonCd;
	}

	public String getBuyCd() {
		return this.buyCd;
	}

	public void setBuyCd(String buyCd) {
		this.buyCd = buyCd;
	}

	public Date getBuyDt() {
		return this.buyDt;
	}

	public void setBuyDt(Date buyDt) {
		this.buyDt = buyDt;
	}

	public String getBuyNmKj() {
		return this.buyNmKj;
	}

	public void setBuyNmKj(String buyNmKj) {
		this.buyNmKj = buyNmKj;
	}

	public String getBuyNo() {
		return this.buyNo;
	}

	public void setBuyNo(String buyNo) {
		this.buyNo = buyNo;
	}

	public String getCmmdtCd() {
		return this.cmmdtCd;
	}

	public void setCmmdtCd(String cmmdtCd) {
		this.cmmdtCd = cmmdtCd;
	}

	public String getCmmdtTtl() {
		return this.cmmdtTtl;
	}

	public void setCmmdtTtl(String cmmdtTtl) {
		this.cmmdtTtl = cmmdtTtl;
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

	public String getInYm() {
		return this.inYm;
	}

	public void setInYm(String inYm) {
		this.inYm = inYm;
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

	public BigDecimal getLnNo() {
		return this.lnNo;
	}

	public void setLnNo(BigDecimal lnNo) {
		this.lnNo = lnNo;
	}

	public String getPayNo() {
		return this.payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getPrtNo() {
		return this.prtNo;
	}

	public void setPrtNo(String prtNo) {
		this.prtNo = prtNo;
	}

	public String getPurordNo() {
		return this.purordNo;
	}

	public void setPurordNo(String purordNo) {
		this.purordNo = purordNo;
	}

	public BigDecimal getQnt() {
		return this.qnt;
	}

	public void setQnt(BigDecimal qnt) {
		this.qnt = qnt;
	}

	public String getRcvinspNo() {
		return this.rcvinspNo;
	}

	public void setRcvinspNo(String rcvinspNo) {
		this.rcvinspNo = rcvinspNo;
	}

	public String getRmk() {
		return this.rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getSlpNo() {
		return this.slpNo;
	}

	public void setSlpNo(String slpNo) {
		this.slpNo = slpNo;
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

	public BigDecimal getUc() {
		return this.uc;
	}

	public void setUc(BigDecimal uc) {
		this.uc = uc;
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