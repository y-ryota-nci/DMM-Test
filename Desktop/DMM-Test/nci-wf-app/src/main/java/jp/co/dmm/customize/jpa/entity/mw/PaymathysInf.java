package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the PAYMATHYS_INF database table.
 * 
 */
@Entity
@Table(name="PAYMATHYS_INF")
@NamedQuery(name="PaymathysInf.findAll", query="SELECT p FROM PaymathysInf p")
public class PaymathysInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PaymathysInfPK id;

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

	@Column(name="PAY_NO")
	private String payNo;

	@Column(name="PAYMAT_AMT_FC")
	private BigDecimal paymatAmtFc;

	@Column(name="PAYMAT_AMT_JPY")
	private BigDecimal paymatAmtJpy;

	@Column(name="PAYSTL_ADD_RTO")
	private BigDecimal paystlAddRto;

	@Column(name="PAYSTL_NO")
	private BigDecimal paystlNo;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public PaymathysInf() {
	}

	public PaymathysInfPK getId() {
		return this.id;
	}

	public void setId(PaymathysInfPK id) {
		this.id = id;
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

	public String getPayNo() {
		return this.payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public BigDecimal getPaymatAmtFc() {
		return this.paymatAmtFc;
	}

	public void setPaymatAmtFc(BigDecimal paymatAmtFc) {
		this.paymatAmtFc = paymatAmtFc;
	}

	public BigDecimal getPaymatAmtJpy() {
		return this.paymatAmtJpy;
	}

	public void setPaymatAmtJpy(BigDecimal paymatAmtJpy) {
		this.paymatAmtJpy = paymatAmtJpy;
	}

	public BigDecimal getPaystlAddRto() {
		return this.paystlAddRto;
	}

	public void setPaystlAddRto(BigDecimal paystlAddRto) {
		this.paystlAddRto = paystlAddRto;
	}

	public BigDecimal getPaystlNo() {
		return this.paystlNo;
	}

	public void setPaystlNo(BigDecimal paystlNo) {
		this.paystlNo = paystlNo;
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