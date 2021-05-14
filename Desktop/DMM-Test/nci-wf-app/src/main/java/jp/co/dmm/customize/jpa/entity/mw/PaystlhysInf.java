package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the PAYSTLHYS_INF database table.
 * 
 */
@Entity
@Table(name="PAYSTLHYS_INF")
@NamedQuery(name="PaystlhysInf.findAll", query="SELECT p FROM PaystlhysInf p")
public class PaystlhysInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PaystlhysInfPK id;

	private String abst1;

	private String abst2;

	@Column(name="ADD_RTO")
	private BigDecimal addRto;

	@Column(name="ADVPAY_NO")
	private String advpayNo;

	@Column(name="BNKACC_CD")
	private String bnkaccCd;

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

	@Column(name="PAY_NO")
	private String payNo;

	@Column(name="PAYMAT_NO")
	private BigDecimal paymatNo;

	@Column(name="PAYSTL_AMT_FC")
	private BigDecimal paystlAmtFc;

	@Column(name="PAYSTL_AMT_JPY")
	private BigDecimal paystlAmtJpy;

	@Column(name="PAYSTL_TP")
	private String paystlTp;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public PaystlhysInf() {
	}

	public PaystlhysInfPK getId() {
		return this.id;
	}

	public void setId(PaystlhysInfPK id) {
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

	public String getBnkaccCd() {
		return this.bnkaccCd;
	}

	public void setBnkaccCd(String bnkaccCd) {
		this.bnkaccCd = bnkaccCd;
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

	public String getPayNo() {
		return this.payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public BigDecimal getPaymatNo() {
		return this.paymatNo;
	}

	public void setPaymatNo(BigDecimal paymatNo) {
		this.paymatNo = paymatNo;
	}

	public BigDecimal getPaystlAmtFc() {
		return this.paystlAmtFc;
	}

	public void setPaystlAmtFc(BigDecimal paystlAmtFc) {
		this.paystlAmtFc = paystlAmtFc;
	}

	public BigDecimal getPaystlAmtJpy() {
		return this.paystlAmtJpy;
	}

	public void setPaystlAmtJpy(BigDecimal paystlAmtJpy) {
		this.paystlAmtJpy = paystlAmtJpy;
	}

	public String getPaystlTp() {
		return this.paystlTp;
	}

	public void setPaystlTp(String paystlTp) {
		this.paystlTp = paystlTp;
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