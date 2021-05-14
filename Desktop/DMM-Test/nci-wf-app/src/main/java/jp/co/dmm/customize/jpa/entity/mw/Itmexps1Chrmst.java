package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the ITMEXPS1_CHRMST database table.
 * 
 */
@Entity
@Table(name="ITMEXPS1_CHRMST")
@NamedQuery(name="Itmexps1Chrmst.findAll", query="SELECT i FROM Itmexps1Chrmst i")
public class Itmexps1Chrmst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private Itmexps1ChrmstPK id;

	@Column(name="ACC_BRKDWN_CD")
	private String accBrkdwnCd;

	@Column(name="ACC_CD")
	private String accCd;

	@Column(name="ASST_TP")
	private String asstTp;

	@Column(name="BDGTACC_CD")
	private String bdgtaccCd;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CST_TP")
	private String cstTp;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="JRN_CD")
	private String jrnCd;

	@Column(name="MNGACC_BRKDWN_CD")
	private String mngaccBrkdwnCd;

	@Column(name="MNGACC_CD")
	private String mngaccCd;

	@Column(name="SLP_GRP_GL")
	private String slpGrpGl;

	@Column(name="TAX_CD")
	private String taxCd;

	@Column(name="TAX_SBJ_TP")
	private String taxSbjTp;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public Itmexps1Chrmst() {
	}

	public Itmexps1ChrmstPK getId() {
		return this.id;
	}

	public void setId(Itmexps1ChrmstPK id) {
		this.id = id;
	}

	public String getAccBrkdwnCd() {
		return this.accBrkdwnCd;
	}

	public void setAccBrkdwnCd(String accBrkdwnCd) {
		this.accBrkdwnCd = accBrkdwnCd;
	}

	public String getAccCd() {
		return this.accCd;
	}

	public void setAccCd(String accCd) {
		this.accCd = accCd;
	}

	public String getAsstTp() {
		return this.asstTp;
	}

	public void setAsstTp(String asstTp) {
		this.asstTp = asstTp;
	}

	public String getBdgtaccCd() {
		return this.bdgtaccCd;
	}

	public void setBdgtaccCd(String bdgtaccCd) {
		this.bdgtaccCd = bdgtaccCd;
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

	public String getCstTp() {
		return this.cstTp;
	}

	public void setCstTp(String cstTp) {
		this.cstTp = cstTp;
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

	public String getJrnCd() {
		return this.jrnCd;
	}

	public void setJrnCd(String jrnCd) {
		this.jrnCd = jrnCd;
	}

	public String getMngaccBrkdwnCd() {
		return this.mngaccBrkdwnCd;
	}

	public void setMngaccBrkdwnCd(String mngaccBrkdwnCd) {
		this.mngaccBrkdwnCd = mngaccBrkdwnCd;
	}

	public String getMngaccCd() {
		return this.mngaccCd;
	}

	public void setMngaccCd(String mngaccCd) {
		this.mngaccCd = mngaccCd;
	}

	public String getSlpGrpGl() {
		return this.slpGrpGl;
	}

	public void setSlpGrpGl(String slpGrpGl) {
		this.slpGrpGl = slpGrpGl;
	}

	public String getTaxCd() {
		return this.taxCd;
	}

	public void setTaxCd(String taxCd) {
		this.taxCd = taxCd;
	}

	public String getTaxSbjTp() {
		return this.taxSbjTp;
	}

	public void setTaxSbjTp(String taxSbjTp) {
		this.taxSbjTp = taxSbjTp;
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