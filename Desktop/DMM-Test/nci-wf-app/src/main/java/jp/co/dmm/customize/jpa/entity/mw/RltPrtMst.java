package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the RLT_PRT_MST database table.
 *
 */
@Entity
@Table(name="RLT_PRT_MST")
@NamedQuery(name="RltPrtMst.findAll", query="SELECT r FROM RltPrtMst r")
public class RltPrtMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RltPrtMstPK id;

	@Temporal(TemporalType.DATE)
	@Column(name="BRTH_DT")
	private Date brthDt;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CRP_PRS_TP")
	private String crpPrsTp;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="JDG_TP")
	private String jdgTp;

	@Column(name="LND_CD")
	private String lndCd;

	@Column(name="MTCH_CNT")
	private Integer mtchCnt;

	@Column(name="MTCH_PEID")
	private String mtchPeid;

	@Column(name="RLT_PRT_NM")
	private String rltPrtNm;

	@Column(name="RLT_PRT_RMK")
	private String rltPrtRmk;

	@Column(name="RLT_PRT_TP")
	private String rltPrtTp;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public RltPrtMst() {
	}

	public RltPrtMstPK getId() {
		return this.id;
	}

	public void setId(RltPrtMstPK id) {
		this.id = id;
	}

	public Date getBrthDt() {
		return this.brthDt;
	}

	public void setBrthDt(Date brthDt) {
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

	public String getCrpPrsTp() {
		return this.crpPrsTp;
	}

	public void setCrpPrsTp(String crpPrsTp) {
		this.crpPrsTp = crpPrsTp;
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

	public String getJdgTp() {
		return this.jdgTp;
	}

	public void setJdgTp(String jdgTp) {
		this.jdgTp = jdgTp;
	}

	public String getLndCd() {
		return this.lndCd;
	}

	public void setLndCd(String lndCd) {
		this.lndCd = lndCd;
	}

	public Integer getMtchCnt() {
		return this.mtchCnt;
	}

	public void setMtchCnt(Integer mtchCnt) {
		this.mtchCnt = mtchCnt;
	}

	public String getMtchPeid() {
		return this.mtchPeid;
	}

	public void setMtchPeid(String mtchPeid) {
		this.mtchPeid = mtchPeid;
	}

	public String getRltPrtNm() {
		return this.rltPrtNm;
	}

	public void setRltPrtNm(String rltPrtNm) {
		this.rltPrtNm = rltPrtNm;
	}

	public String getRltPrtRmk() {
		return this.rltPrtRmk;
	}

	public void setRltPrtRmk(String rltPrtRmk) {
		this.rltPrtRmk = rltPrtRmk;
	}

	public String getRltPrtTp() {
		return this.rltPrtTp;
	}

	public void setRltPrtTp(String rltPrtTp) {
		this.rltPrtTp = rltPrtTp;
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