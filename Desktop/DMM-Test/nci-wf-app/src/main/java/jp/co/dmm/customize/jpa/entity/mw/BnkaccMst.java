package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the BNKACC_MST database table.
 * 
 */
@Entity
@Table(name="BNKACC_MST")
@NamedQuery(name="BnkaccMst.findAll", query="SELECT b FROM BnkaccMst b")
public class BnkaccMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BnkaccMstPK id;

	@Column(name="BNK_CD")
	private String bnkCd;

	@Column(name="BNKACC_NM")
	private String bnkaccNm;

	@Column(name="BNKACC_NM_KN")
	private String bnkaccNmKn;

	@Column(name="BNKACC_NO")
	private String bnkaccNo;

	@Column(name="BNKACC_TP")
	private String bnkaccTp;

	@Column(name="BNKBRC_CD")
	private String bnkbrcCd;

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

	public BnkaccMst() {
	}

	public BnkaccMstPK getId() {
		return this.id;
	}

	public void setId(BnkaccMstPK id) {
		this.id = id;
	}

	public String getBnkCd() {
		return this.bnkCd;
	}

	public void setBnkCd(String bnkCd) {
		this.bnkCd = bnkCd;
	}

	public String getBnkaccNm() {
		return this.bnkaccNm;
	}

	public void setBnkaccNm(String bnkaccNm) {
		this.bnkaccNm = bnkaccNm;
	}

	public String getBnkaccNmKn() {
		return this.bnkaccNmKn;
	}

	public void setBnkaccNmKn(String bnkaccNmKn) {
		this.bnkaccNmKn = bnkaccNmKn;
	}

	public String getBnkaccNo() {
		return this.bnkaccNo;
	}

	public void setBnkaccNo(String bnkaccNo) {
		this.bnkaccNo = bnkaccNo;
	}

	public String getBnkaccTp() {
		return this.bnkaccTp;
	}

	public void setBnkaccTp(String bnkaccTp) {
		this.bnkaccTp = bnkaccTp;
	}

	public String getBnkbrcCd() {
		return this.bnkbrcCd;
	}

	public void setBnkbrcCd(String bnkbrcCd) {
		this.bnkbrcCd = bnkbrcCd;
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