package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the PAYEE_BNKACC_MST database table.
 * 
 */
@Entity
@Table(name="PAYEE_BNKACC_MST")
@NamedQuery(name="PayeeBnkaccMst.findAll", query="SELECT p FROM PayeeBnkaccMst p")
public class PayeeBnkaccMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PayeeBnkaccMstPK id;

	@Column(name="BNK_CD")
	private String bnkCd;

	@Column(name="BNKACC_CD")
	private String bnkaccCd;

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

	@Column(name="BUYEE_STF_TP")
	private String buyeeStfTp;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="HLD_TRT_TP")
	private String hldTrtTp;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="PAY_CMM_OBL_TP")
	private String payCmmOblTp;

	@Column(name="PAYEE_BNKACC_CD_SS")
	private String payeeBnkaccCdSs;

	private String rmk;

	@Column(name="SPLR_CD")
	private String splrCd;

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

	public PayeeBnkaccMst() {
	}

	public PayeeBnkaccMstPK getId() {
		return this.id;
	}

	public void setId(PayeeBnkaccMstPK id) {
		this.id = id;
	}

	public String getBnkCd() {
		return this.bnkCd;
	}

	public void setBnkCd(String bnkCd) {
		this.bnkCd = bnkCd;
	}

	public String getBnkaccCd() {
		return this.bnkaccCd;
	}

	public void setBnkaccCd(String bnkaccCd) {
		this.bnkaccCd = bnkaccCd;
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

	public String getBuyeeStfTp() {
		return this.buyeeStfTp;
	}

	public void setBuyeeStfTp(String buyeeStfTp) {
		this.buyeeStfTp = buyeeStfTp;
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

	public String getHldTrtTp() {
		return this.hldTrtTp;
	}

	public void setHldTrtTp(String hldTrtTp) {
		this.hldTrtTp = hldTrtTp;
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

	public String getPayCmmOblTp() {
		return this.payCmmOblTp;
	}

	public void setPayCmmOblTp(String payCmmOblTp) {
		this.payCmmOblTp = payCmmOblTp;
	}

	public String getPayeeBnkaccCdSs() {
		return this.payeeBnkaccCdSs;
	}

	public void setPayeeBnkaccCdSs(String payeeBnkaccCdSs) {
		this.payeeBnkaccCdSs = payeeBnkaccCdSs;
	}

	public String getRmk() {
		return this.rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getSplrCd() {
		return this.splrCd;
	}

	public void setSplrCd(String splrCd) {
		this.splrCd = splrCd;
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