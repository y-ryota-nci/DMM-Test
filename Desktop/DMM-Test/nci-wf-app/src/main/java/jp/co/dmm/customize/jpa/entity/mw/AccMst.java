package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the ACC_MST database table.
 * 
 */
@Entity
@Table(name="ACC_MST")
@NamedQuery(name="AccMst.findAll", query="SELECT a FROM AccMst a")
public class AccMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AccMstPK id;

	@Column(name="ACC_BRKDWN_TP")
	private String accBrkdwnTp;

	@Column(name="ACC_NM")
	private String accNm;

	@Column(name="ACC_NM_S")
	private String accNmS;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DC_TP")
	private String dcTp;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="TAX_CD_SS")
	private String taxCdSs;

	@Column(name="TAX_IPT_TP")
	private String taxIptTp;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	@Temporal(TemporalType.DATE)
	@Column(name="VD_DT_E")
	private Date vdDtE;

	@Temporal(TemporalType.DATE)
	@Column(name="VD_DT_S")
	private Date vdDtS;

	public AccMst() {
	}

	public AccMstPK getId() {
		return this.id;
	}

	public void setId(AccMstPK id) {
		this.id = id;
	}

	public String getAccBrkdwnTp() {
		return this.accBrkdwnTp;
	}

	public void setAccBrkdwnTp(String accBrkdwnTp) {
		this.accBrkdwnTp = accBrkdwnTp;
	}

	public String getAccNm() {
		return this.accNm;
	}

	public void setAccNm(String accNm) {
		this.accNm = accNm;
	}

	public String getAccNmS() {
		return this.accNmS;
	}

	public void setAccNmS(String accNmS) {
		this.accNmS = accNmS;
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

	public String getDcTp() {
		return this.dcTp;
	}

	public void setDcTp(String dcTp) {
		this.dcTp = dcTp;
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

	public String getTaxCdSs() {
		return this.taxCdSs;
	}

	public void setTaxCdSs(String taxCdSs) {
		this.taxCdSs = taxCdSs;
	}

	public String getTaxIptTp() {
		return this.taxIptTp;
	}

	public void setTaxIptTp(String taxIptTp) {
		this.taxIptTp = taxIptTp;
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

	public Date getVdDtE() {
		return this.vdDtE;
	}

	public void setVdDtE(Date vdDtE) {
		this.vdDtE = vdDtE;
	}

	public Date getVdDtS() {
		return this.vdDtS;
	}

	public void setVdDtS(Date vdDtS) {
		this.vdDtS = vdDtS;
	}

}