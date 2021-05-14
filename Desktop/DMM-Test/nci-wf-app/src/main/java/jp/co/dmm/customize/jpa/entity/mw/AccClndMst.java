package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the ACC_CLND_MST database table.
 *
 */
@Entity
@Table(name="ACC_CLND_MST")
@NamedQuery(name="AccClndMst.findAll", query="SELECT a FROM AccClndMst a")
public class AccClndMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AccClndMstPK id;

	@Column(name="BNK_HLDAY_TP")
	private String bnkHldayTp;

	@Column(name="CLND_DAY")
	private String clndDay;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="HLDAY_TP")
	private String hldayTp;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="ML_CLS_TM")
	private String mlClsTm;

	@Column(name="STL_TP_FNCAFF")
	private String stlTpFncaff;

	@Column(name="STL_TP_FNCOBL")
	private String stlTpFncobl;

	@Column(name="STL_TP_PUR")
	private String stlTpPur;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Version
	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public AccClndMst() {
	}

	public AccClndMstPK getId() {
		return this.id;
	}

	public void setId(AccClndMstPK id) {
		this.id = id;
	}

	public String getBnkHldayTp() {
		return this.bnkHldayTp;
	}

	public void setBnkHldayTp(String bnkHldayTp) {
		this.bnkHldayTp = bnkHldayTp;
	}

	public String getClndDay() {
		return this.clndDay;
	}

	public void setClndDay(String clndDay) {
		this.clndDay = clndDay;
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

	public String getHldayTp() {
		return this.hldayTp;
	}

	public void setHldayTp(String hldayTp) {
		this.hldayTp = hldayTp;
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

	public String getMlClsTm() {
		return this.mlClsTm;
	}

	public void setMlClsTm(String mlClsTm) {
		this.mlClsTm = mlClsTm;
	}

	public String getStlTpFncaff() {
		return this.stlTpFncaff;
	}

	public void setStlTpFncaff(String stlTpFncaff) {
		this.stlTpFncaff = stlTpFncaff;
	}

	public String getStlTpFncobl() {
		return this.stlTpFncobl;
	}

	public void setStlTpFncobl(String stlTpFncobl) {
		this.stlTpFncobl = stlTpFncobl;
	}

	public String getStlTpPur() {
		return this.stlTpPur;
	}

	public void setStlTpPur(String stlTpPur) {
		this.stlTpPur = stlTpPur;
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