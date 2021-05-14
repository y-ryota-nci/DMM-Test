package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the ORG_CRM_INF database table.
 *
 */
@Entity
@Table(name="ORG_CRM_INF")
@NamedQuery(name="OrgCrmInf.findAll", query="SELECT o FROM OrgCrmInf o")
public class OrgCrmInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private OrgCrmInfPK id;

	@Column(name="BRTH_DT")
	private String brthDt;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="GND_TP")
	private String gndTp;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="LND_CD")
	private String lndCd;

	@Column(name="MTCH_NM")
	private String mtchNm;

	private String peid;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public OrgCrmInf() {
	}

	public OrgCrmInfPK getId() {
		return this.id;
	}

	public void setId(OrgCrmInfPK id) {
		this.id = id;
	}

	public String getBrthDt() {
		return this.brthDt;
	}

	public void setBrthDt(String brthDt) {
		this.brthDt = brthDt;
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

	public String getGndTp() {
		return this.gndTp;
	}

	public void setGndTp(String gndTp) {
		this.gndTp = gndTp;
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

	public String getLndCd() {
		return this.lndCd;
	}

	public void setLndCd(String lndCd) {
		this.lndCd = lndCd;
	}

	public String getMtchNm() {
		return this.mtchNm;
	}

	public void setMtchNm(String mtchNm) {
		this.mtchNm = mtchNm;
	}

	public String getPeid() {
		return this.peid;
	}

	public void setPeid(String peid) {
		this.peid = peid;
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