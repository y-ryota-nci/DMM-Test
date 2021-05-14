package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the PRD_PURORD_MST database table.
 * 
 */
@Entity
@Table(name="PRD_PURORD_MST")
@NamedQuery(name="PrdPurordMst.findAll", query="SELECT p FROM PrdPurordMst p")
public class PrdPurordMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PrdPurordMstPK id;

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

	@Column(name="ML_ADD_TP")
	private String mlAddTp;

	@Column(name="PAY_END_TIME")
	private String payEndTime;

	@Column(name="PAY_SITE_CD")
	private String paySiteCd;

	@Column(name="PAY_START_TIME")
	private String payStartTime;

	@Column(name="PAYEE_BNKACC_CD")
	private String payeeBnkaccCd;

	@Column(name="PRD_PAY_DAY")
	private String prdPayDay;

	@Column(name="PRD_PAY_MTH")
	private String prdPayMth;

	@Column(name="PRD_PAY_TP")
	private String prdPayTp;

	@Column(name="PRD_PURORDR_CD")
	private String prdPurordrCd;

	@Column(name="PRD_PURORDR_DPT_CD")
	private String prdPurordrDptCd;

	@Column(name="PRD_PURORDR_DPT_NM")
	private String prdPurordrDptNm;

	@Column(name="PURORD_NO")
	private String purordNo;

	@Column(name="SBMT_DPT_CD")
	private String sbmtDptCd;

	@Column(name="SBMT_DPT_NM")
	private String sbmtDptNm;

	@Column(name="SBMTR_CD")
	private String sbmtrCd;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public PrdPurordMst() {
	}

	public PrdPurordMstPK getId() {
		return this.id;
	}

	public void setId(PrdPurordMstPK id) {
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

	public String getMlAddTp() {
		return this.mlAddTp;
	}

	public void setMlAddTp(String mlAddTp) {
		this.mlAddTp = mlAddTp;
	}

	public String getPayEndTime() {
		return this.payEndTime;
	}

	public void setPayEndTime(String payEndTime) {
		this.payEndTime = payEndTime;
	}

	public String getPaySiteCd() {
		return this.paySiteCd;
	}

	public void setPaySiteCd(String paySiteCd) {
		this.paySiteCd = paySiteCd;
	}

	public String getPayStartTime() {
		return this.payStartTime;
	}

	public void setPayStartTime(String payStartTime) {
		this.payStartTime = payStartTime;
	}

	public String getPayeeBnkaccCd() {
		return this.payeeBnkaccCd;
	}

	public void setPayeeBnkaccCd(String payeeBnkaccCd) {
		this.payeeBnkaccCd = payeeBnkaccCd;
	}

	public String getPrdPayDay() {
		return this.prdPayDay;
	}

	public void setPrdPayDay(String prdPayDay) {
		this.prdPayDay = prdPayDay;
	}

	public String getPrdPayMth() {
		return this.prdPayMth;
	}

	public void setPrdPayMth(String prdPayMth) {
		this.prdPayMth = prdPayMth;
	}

	public String getPrdPayTp() {
		return this.prdPayTp;
	}

	public void setPrdPayTp(String prdPayTp) {
		this.prdPayTp = prdPayTp;
	}

	public String getPrdPurordrCd() {
		return this.prdPurordrCd;
	}

	public void setPrdPurordrCd(String prdPurordrCd) {
		this.prdPurordrCd = prdPurordrCd;
	}

	public String getPrdPurordrDptCd() {
		return this.prdPurordrDptCd;
	}

	public void setPrdPurordrDptCd(String prdPurordrDptCd) {
		this.prdPurordrDptCd = prdPurordrDptCd;
	}

	public String getPrdPurordrDptNm() {
		return this.prdPurordrDptNm;
	}

	public void setPrdPurordrDptNm(String prdPurordrDptNm) {
		this.prdPurordrDptNm = prdPurordrDptNm;
	}

	public String getPurordNo() {
		return this.purordNo;
	}

	public void setPurordNo(String purordNo) {
		this.purordNo = purordNo;
	}

	public String getSbmtDptCd() {
		return this.sbmtDptCd;
	}

	public void setSbmtDptCd(String sbmtDptCd) {
		this.sbmtDptCd = sbmtDptCd;
	}

	public String getSbmtDptNm() {
		return this.sbmtDptNm;
	}

	public void setSbmtDptNm(String sbmtDptNm) {
		this.sbmtDptNm = sbmtDptNm;
	}

	public String getSbmtrCd() {
		return this.sbmtrCd;
	}

	public void setSbmtrCd(String sbmtrCd) {
		this.sbmtrCd = sbmtrCd;
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