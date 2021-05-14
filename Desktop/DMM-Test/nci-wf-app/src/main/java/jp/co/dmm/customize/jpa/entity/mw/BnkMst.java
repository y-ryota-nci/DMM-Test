package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the BNK_MST database table.
 * 
 */
@Entity
@Table(name="BNK_MST")
@NamedQuery(name="BnkMst.findAll", query="SELECT b FROM BnkMst b")
public class BnkMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BnkMstPK id;

	@Column(name="BNK_NM")
	private String bnkNm;

	@Column(name="BNK_NM_KN")
	private String bnkNmKn;

	@Column(name="BNK_NM_S")
	private String bnkNmS;

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

	public BnkMst() {
	}

	public BnkMstPK getId() {
		return this.id;
	}

	public void setId(BnkMstPK id) {
		this.id = id;
	}

	public String getBnkNm() {
		return this.bnkNm;
	}

	public void setBnkNm(String bnkNm) {
		this.bnkNm = bnkNm;
	}

	public String getBnkNmKn() {
		return this.bnkNmKn;
	}

	public void setBnkNmKn(String bnkNmKn) {
		this.bnkNmKn = bnkNmKn;
	}

	public String getBnkNmS() {
		return this.bnkNmS;
	}

	public void setBnkNmS(String bnkNmS) {
		this.bnkNmS = bnkNmS;
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