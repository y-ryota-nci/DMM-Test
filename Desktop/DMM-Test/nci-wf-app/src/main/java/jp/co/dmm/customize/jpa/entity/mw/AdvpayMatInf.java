package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the ADVPAY_MAT_INF database table.
 * 
 */
@Entity
@Table(name="ADVPAY_MAT_INF")
@NamedQuery(name="AdvpayMatInf.findAll", query="SELECT a FROM AdvpayMatInf a")
public class AdvpayMatInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AdvpayMatInfPK id;

	@Column(name="ADD_RTO")
	private BigDecimal addRto;

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

	@Column(name="MAT_AMT_FC")
	private BigDecimal matAmtFc;

	@Column(name="MAT_AMT_JPY")
	private BigDecimal matAmtJpy;

	@Column(name="MAT_AMT_JPY_INCTAX")
	private BigDecimal matAmtJpyInctax;

	@Temporal(TemporalType.DATE)
	@Column(name="MAT_DT")
	private Date matDt;

	@Column(name="MNY_CD")
	private String mnyCd;

	@Column(name="PAY_DTL_NO")
	private BigDecimal payDtlNo;

	@Column(name="PAY_NO")
	private String payNo;

	@Column(name="RCVINSP_DTL_NO")
	private BigDecimal rcvinspDtlNo;

	@Column(name="RCVINSP_NO")
	private String rcvinspNo;

	@Column(name="SSGL_SND_NO")
	private String ssglSndNo;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public AdvpayMatInf() {
	}

	public AdvpayMatInfPK getId() {
		return this.id;
	}

	public void setId(AdvpayMatInfPK id) {
		this.id = id;
	}

	public BigDecimal getAddRto() {
		return this.addRto;
	}

	public void setAddRto(BigDecimal addRto) {
		this.addRto = addRto;
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

	public BigDecimal getMatAmtFc() {
		return this.matAmtFc;
	}

	public void setMatAmtFc(BigDecimal matAmtFc) {
		this.matAmtFc = matAmtFc;
	}

	public BigDecimal getMatAmtJpy() {
		return this.matAmtJpy;
	}

	public void setMatAmtJpy(BigDecimal matAmtJpy) {
		this.matAmtJpy = matAmtJpy;
	}

	public BigDecimal getMatAmtJpyInctax() {
		return this.matAmtJpyInctax;
	}

	public void setMatAmtJpyInctax(BigDecimal matAmtJpyInctax) {
		this.matAmtJpyInctax = matAmtJpyInctax;
	}

	public Date getMatDt() {
		return this.matDt;
	}

	public void setMatDt(Date matDt) {
		this.matDt = matDt;
	}

	public String getMnyCd() {
		return this.mnyCd;
	}

	public void setMnyCd(String mnyCd) {
		this.mnyCd = mnyCd;
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

	public BigDecimal getRcvinspDtlNo() {
		return this.rcvinspDtlNo;
	}

	public void setRcvinspDtlNo(BigDecimal rcvinspDtlNo) {
		this.rcvinspDtlNo = rcvinspDtlNo;
	}

	public String getRcvinspNo() {
		return this.rcvinspNo;
	}

	public void setRcvinspNo(String rcvinspNo) {
		this.rcvinspNo = rcvinspNo;
	}

	public String getSsglSndNo() {
		return this.ssglSndNo;
	}

	public void setSsglSndNo(String ssglSndNo) {
		this.ssglSndNo = ssglSndNo;
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