package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the PRD_PURORD_PLN_MST database table.
 *
 */
@Entity
@Table(name="PRD_PURORD_PLN_MST")
@NamedQuery(name="PrdPurordPlnMst.findAll", query="SELECT p FROM PrdPurordPlnMst p")
public class PrdPurordPlnMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PrdPurordPlnMstPK id;

	@Column(name="PURORD_NO")
	private String purordNo;

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

	@Temporal(TemporalType.DATE)
	@Column(name="PRD_IOV_DT_P")
	private Date prdIovDtP;

	@Temporal(TemporalType.DATE)
	@Column(name="PRD_IOV_DT_R")
	private Date prdIovDtR;

	@Column(name="PRD_PAY_CNT")
	private BigDecimal prdPayCnt;

	@Temporal(TemporalType.DATE)
	@Column(name="PRD_PAY_DT")
	private Date prdPayDt;

	@Column(name="PRD_PAY_STS")
	private String prdPaySts;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public PrdPurordPlnMst() {
	}

	public PrdPurordPlnMstPK getId() {
		return this.id;
	}

	public void setId(PrdPurordPlnMstPK id) {
		this.id = id;
	}

	public String getPurordNo() {
		return this.purordNo;
	}
	public void setPurordNo(String purordNo) {
		this.purordNo = purordNo;
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

	public Date getPrdIovDtP() {
		return this.prdIovDtP;
	}

	public void setPrdIovDtP(Date prdIovDtP) {
		this.prdIovDtP = prdIovDtP;
	}

	public Date getPrdIovDtR() {
		return this.prdIovDtR;
	}

	public void setPrdIovDtR(Date prdIovDtR) {
		this.prdIovDtR = prdIovDtR;
	}

	public BigDecimal getPrdPayCnt() {
		return this.prdPayCnt;
	}

	public void setPrdPayCnt(BigDecimal prdPayCnt) {
		this.prdPayCnt = prdPayCnt;
	}

	public Date getPrdPayDt() {
		return this.prdPayDt;
	}

	public void setPrdPayDt(Date prdPayDt) {
		this.prdPayDt = prdPayDt;
	}

	public String getPrdPaySts() {
		return this.prdPaySts;
	}

	public void setPrdPaySts(String prdPaySts) {
		this.prdPaySts = prdPaySts;
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