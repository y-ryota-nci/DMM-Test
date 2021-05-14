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
 * The persistent class for the BAL_INF database table.
 *
 */
@Entity
@Table(name="BAL_INF")
@NamedQuery(name="BalInf.findAll", query="SELECT b FROM BalInf b")
public class BalInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BalInfPK id;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CR_AMT_FC")
	private BigDecimal crAmtFc;

	@Column(name="CR_AMT_JPY")
	private BigDecimal crAmtJpy;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="DR_AMT_FC")
	private BigDecimal drAmtFc;

	@Column(name="DR_AMT_JPY")
	private BigDecimal drAmtJpy;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="NXT_CRROVR_AMT_FC")
	private BigDecimal nxtCrrovrAmtFc;

	@Column(name="NXT_CRROVR_AMT_JPY")
	private BigDecimal nxtCrrovrAmtJpy;

	@Column(name="PRV_CRROVR_AMT_FC")
	private BigDecimal prvCrrovrAmtFc;

	@Column(name="PRV_CRROVR_AMT_JPY")
	private BigDecimal prvCrrovrAmtJpy;

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

	public BalInf() {
	}

	public BalInfPK getId() {
		return this.id;
	}

	public void setId(BalInfPK id) {
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

	public BigDecimal getCrAmtFc() {
		return this.crAmtFc;
	}

	public void setCrAmtFc(BigDecimal crAmtFc) {
		this.crAmtFc = crAmtFc;
	}

	public BigDecimal getCrAmtJpy() {
		return this.crAmtJpy;
	}

	public void setCrAmtJpy(BigDecimal crAmtJpy) {
		this.crAmtJpy = crAmtJpy;
	}

	public String getDltFg() {
		return this.dltFg;
	}

	public void setDltFg(String dltFg) {
		this.dltFg = dltFg;
	}

	public BigDecimal getDrAmtFc() {
		return this.drAmtFc;
	}

	public void setDrAmtFc(BigDecimal drAmtFc) {
		this.drAmtFc = drAmtFc;
	}

	public BigDecimal getDrAmtJpy() {
		return this.drAmtJpy;
	}

	public void setDrAmtJpy(BigDecimal drAmtJpy) {
		this.drAmtJpy = drAmtJpy;
	}

	public String getIpCreated() {
		return this.ipCreated;
	}

	public void setIpCreated(String payCondChgrsn) {
		this.ipCreated = payCondChgrsn;
	}

	public String getIpUpdated() {
		return this.ipUpdated;
	}

	public void setIpUpdated(String ipUpdated) {
		this.ipUpdated = ipUpdated;
	}

	public BigDecimal getNxtCrrovrAmtFc() {
		return this.nxtCrrovrAmtFc;
	}

	public void setNxtCrrovrAmtFc(BigDecimal nxtCrrovrAmtFc) {
		this.nxtCrrovrAmtFc = nxtCrrovrAmtFc;
	}

	public BigDecimal getNxtCrrovrAmtJpy() {
		return this.nxtCrrovrAmtJpy;
	}

	public void setNxtCrrovrAmtJpy(BigDecimal nxtCrrovrAmtJpy) {
		this.nxtCrrovrAmtJpy = nxtCrrovrAmtJpy;
	}

	public BigDecimal getPrvCrrovrAmtFc() {
		return this.prvCrrovrAmtFc;
	}

	public void setPrvCrrovrAmtFc(BigDecimal prvCrrovrAmtFc) {
		this.prvCrrovrAmtFc = prvCrrovrAmtFc;
	}

	public BigDecimal getPrvCrrovrAmtJpy() {
		return this.prvCrrovrAmtJpy;
	}

	public void setPrvCrrovrAmtJpy(BigDecimal prvCrrovrAmtJpy) {
		this.prvCrrovrAmtJpy = prvCrrovrAmtJpy;
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