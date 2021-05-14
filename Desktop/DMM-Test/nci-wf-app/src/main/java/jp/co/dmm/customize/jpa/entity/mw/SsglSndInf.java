package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the SSGL_SND_INF database table.
 * 
 */
@Entity
@Table(name="SSGL_SND_INF")
@NamedQuery(name="SsglSndInf.findAll", query="SELECT s FROM SsglSndInf s")
public class SsglSndInf extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SsglSndInfPK id;

	@Column(name="ADVPAY_NO")
	private String advpayNo;

	@Column(name="BUY_NO")
	private String buyNo;

	@Column(name="CNCL_TP")
	private String cnclTp;

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

	@Column(name="JRNSLP_NO")
	private String jrnslpNo;

	@Temporal(TemporalType.DATE)
	@Column(name="MAK_DT")
	private Date makDt;

	@Column(name="MAK_SYS")
	private String makSys;

	@Column(name="MAK_TM")
	private String makTm;

	@Column(name="PAYHYS_NO")
	private String payhysNo;

	@Temporal(TemporalType.DATE)
	@Column(name="SLP_DT")
	private Date slpDt;

	@Column(name="SLP_GRP")
	private String slpGrp;

	@Column(name="SLP_NO")
	private String slpNo;

	@Temporal(TemporalType.DATE)
	@Column(name="SND_DT")
	private Date sndDt;

	@Column(name="SND_STS")
	private String sndSts;

	@Column(name="SND_TM")
	private String sndTm;

	@Column(name="SSGL_SND_NO_O")
	private String ssglSndNoO;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public SsglSndInf() {
	}

	public SsglSndInfPK getId() {
		return this.id;
	}

	public void setId(SsglSndInfPK id) {
		this.id = id;
	}

	public String getAdvpayNo() {
		return this.advpayNo;
	}

	public void setAdvpayNo(String advpayNo) {
		this.advpayNo = advpayNo;
	}

	public String getBuyNo() {
		return this.buyNo;
	}

	public void setBuyNo(String buyNo) {
		this.buyNo = buyNo;
	}

	public String getCnclTp() {
		return this.cnclTp;
	}

	public void setCnclTp(String cnclTp) {
		this.cnclTp = cnclTp;
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

	public String getJrnslpNo() {
		return this.jrnslpNo;
	}

	public void setJrnslpNo(String jrnslpNo) {
		this.jrnslpNo = jrnslpNo;
	}

	public Date getMakDt() {
		return this.makDt;
	}

	public void setMakDt(Date makDt) {
		this.makDt = makDt;
	}

	public String getMakSys() {
		return this.makSys;
	}

	public void setMakSys(String makSys) {
		this.makSys = makSys;
	}

	public String getMakTm() {
		return this.makTm;
	}

	public void setMakTm(String makTm) {
		this.makTm = makTm;
	}

	public String getPayhysNo() {
		return this.payhysNo;
	}

	public void setPayhysNo(String payhysNo) {
		this.payhysNo = payhysNo;
	}

	public Date getSlpDt() {
		return this.slpDt;
	}

	public void setSlpDt(Date slpDt) {
		this.slpDt = slpDt;
	}

	public String getSlpGrp() {
		return this.slpGrp;
	}

	public void setSlpGrp(String slpGrp) {
		this.slpGrp = slpGrp;
	}

	public String getSlpNo() {
		return this.slpNo;
	}

	public void setSlpNo(String slpNo) {
		this.slpNo = slpNo;
	}

	public Date getSndDt() {
		return this.sndDt;
	}

	public void setSndDt(Date sndDt) {
		this.sndDt = sndDt;
	}

	public String getSndSts() {
		return this.sndSts;
	}

	public void setSndSts(String sndSts) {
		this.sndSts = sndSts;
	}

	public String getSndTm() {
		return this.sndTm;
	}

	public void setSndTm(String sndTm) {
		this.sndTm = sndTm;
	}

	public String getSsglSndNoO() {
		return this.ssglSndNoO;
	}

	public void setSsglSndNoO(String ssglSndNoO) {
		this.ssglSndNoO = ssglSndNoO;
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