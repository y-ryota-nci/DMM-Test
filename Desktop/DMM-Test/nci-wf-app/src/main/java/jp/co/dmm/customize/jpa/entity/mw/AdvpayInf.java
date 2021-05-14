package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the ADVPAY_INF database table.
 *
 */
@Entity
@Table(name="ADVPAY_INF")
@NamedQuery(name="AdvpayInf.findAll", query="SELECT a FROM AdvpayInf a")
public class AdvpayInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AdvpayInfPK id;

	@Column(name="ADD_RTO")
	private BigDecimal addRto;

	@Column(name="ADVPAY_STS")
	private String advpaySts;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="JRNSLP_DTL_NO")
	private BigDecimal jrnslpDtlNo;

	@Column(name="JRNSLP_NO")
	private String jrnslpNo;

	@Column(name="MNY_CD")
	private String mnyCd;

	@Column(name="PAY_AMT_FC")
	private BigDecimal payAmtFc;

	@Column(name="PAY_AMT_JPY")
	private BigDecimal payAmtJpy;

	@Column(name="PAY_AMT_JPY_INCTAX")
	private BigDecimal payAmtJpyInctax;

	@Column(name="PAY_DTL_NO")
	private BigDecimal payDtlNo;

	@Column(name="PAY_NO")
	private String payNo;

	@Column(name="PAYEE_BNK_CD")
	private String payeeBnkCd;

	@Column(name="PAYEE_BNKACC_CD")
	private String payeeBnkaccCd;

	@Column(name="PAYEE_BNKACC_NM")
	private String payeeBnkaccNm;

	@Column(name="PAYEE_BNKACC_NM_KN")
	private String payeeBnkaccNmKn;

	@Column(name="PAYEE_BNKACC_NO")
	private String payeeBnkaccNo;

	@Column(name="PAYEE_BNKACC_TP")
	private String payeeBnkaccTp;

	@Column(name="PAYEE_BNKBRC_CD")
	private String payeeBnkbrcCd;

	@Column(name="RMN_AMT_FC")
	private BigDecimal rmnAmtFc;

	@Column(name="RMN_AMT_JPY")
	private BigDecimal rmnAmtJpy;

	@Column(name="SPLR_CD")
	private String splrCd;

	@Column(name="SPLR_NM_KJ")
	private String splrNmKj;

	@Column(name="SPLR_NM_KN")
	private String splrNmKn;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public AdvpayInf() {
	}

	public AdvpayInfPK getId() {
		return this.id;
	}

	public void setId(AdvpayInfPK id) {
		this.id = id;
	}

	public BigDecimal getAddRto() {
		return this.addRto;
	}

	public void setAddRto(BigDecimal addRto) {
		this.addRto = addRto;
	}

	public String getAdvpaySts() {
		return this.advpaySts;
	}

	public void setAdvpaySts(String advpaySts) {
		this.advpaySts = advpaySts;
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

	public String getMnyCd() {
		return this.mnyCd;
	}

	public void setMnyCd(String mnyCd) {
		this.mnyCd = mnyCd;
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

	public String getPayeeBnkCd() {
		return this.payeeBnkCd;
	}

	public void setPayeeBnkCd(String payeeBnkCd) {
		this.payeeBnkCd = payeeBnkCd;
	}

	public String getPayeeBnkaccCd() {
		return this.payeeBnkaccCd;
	}

	public void setPayeeBnkaccCd(String payeeBnkaccCd) {
		this.payeeBnkaccCd = payeeBnkaccCd;
	}

	public String getPayeeBnkaccNm() {
		return this.payeeBnkaccNm;
	}

	public void setPayeeBnkaccNm(String payeeBnkaccNm) {
		this.payeeBnkaccNm = payeeBnkaccNm;
	}

	public String getPayeeBnkaccNmKn() {
		return this.payeeBnkaccNmKn;
	}

	public void setPayeeBnkaccNmKn(String payeeBnkaccNmKn) {
		this.payeeBnkaccNmKn = payeeBnkaccNmKn;
	}

	public String getPayeeBnkaccNo() {
		return this.payeeBnkaccNo;
	}

	public void setPayeeBnkaccNo(String payeeBnkaccNo) {
		this.payeeBnkaccNo = payeeBnkaccNo;
	}

	public String getPayeeBnkaccTp() {
		return this.payeeBnkaccTp;
	}

	public void setPayeeBnkaccTp(String payeeBnkaccTp) {
		this.payeeBnkaccTp = payeeBnkaccTp;
	}

	public String getPayeeBnkbrcCd() {
		return this.payeeBnkbrcCd;
	}

	public void setPayeeBnkbrcCd(String payeeBnkbrcCd) {
		this.payeeBnkbrcCd = payeeBnkbrcCd;
	}

	public BigDecimal getRmnAmtFc() {
		return this.rmnAmtFc;
	}

	public void setRmnAmtFc(BigDecimal rmnAmtFc) {
		this.rmnAmtFc = rmnAmtFc;
	}

	public BigDecimal getRmnAmtJpy() {
		return this.rmnAmtJpy;
	}

	public void setRmnAmtJpy(BigDecimal rmnAmtJpy) {
		this.rmnAmtJpy = rmnAmtJpy;
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