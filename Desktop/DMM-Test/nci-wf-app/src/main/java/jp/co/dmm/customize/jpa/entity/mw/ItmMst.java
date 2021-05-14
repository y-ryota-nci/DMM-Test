package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the ITM_MST database table.
 * 
 */
@Entity
@Table(name="ITM_MST")
@NamedQuery(name="ItmMst.findAll", query="SELECT i FROM ItmMst i")
public class ItmMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ItmMstPK id;

	private BigDecimal amt;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CTGRY_CD")
	private String ctgryCd;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="ITM_NM")
	private String itmNm;

	@Column(name="ITM_RMK")
	private String itmRmk;

	@Column(name="ITM_VRSN")
	private BigDecimal itmVrsn;

	@Column(name="MAKER_MDL_NO")
	private String makerMdlNo;

	@Column(name="MAKER_NM")
	private String makerNm;

	@Column(name="PRC_FLD_TP")
	private String prcFldTp;

	@Column(name="SPLR_CD")
	private String splrCd;

	@Column(name="SPLR_NM_KJ")
	private String splrNmKj;

	@Column(name="SPLR_NM_KN")
	private String splrNmKn;

	@Column(name="STCK_TP")
	private String stckTp;

	@Column(name="TAX_CD")
	private String taxCd;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="UNT_CD")
	private String untCd;

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

	public ItmMst() {
	}

	public ItmMstPK getId() {
		return this.id;
	}

	public void setId(ItmMstPK id) {
		this.id = id;
	}

	public BigDecimal getAmt() {
		return this.amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
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

	public String getCtgryCd() {
		return this.ctgryCd;
	}

	public void setCtgryCd(String ctgryCd) {
		this.ctgryCd = ctgryCd;
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

	public String getItmNm() {
		return this.itmNm;
	}

	public void setItmNm(String itmNm) {
		this.itmNm = itmNm;
	}

	public String getItmRmk() {
		return this.itmRmk;
	}

	public void setItmRmk(String itmRmk) {
		this.itmRmk = itmRmk;
	}

	public BigDecimal getItmVrsn() {
		return this.itmVrsn;
	}

	public void setItmVrsn(BigDecimal itmVrsn) {
		this.itmVrsn = itmVrsn;
	}

	public String getMakerMdlNo() {
		return this.makerMdlNo;
	}

	public void setMakerMdlNo(String makerMdlNo) {
		this.makerMdlNo = makerMdlNo;
	}

	public String getMakerNm() {
		return this.makerNm;
	}

	public void setMakerNm(String makerNm) {
		this.makerNm = makerNm;
	}

	public String getPrcFldTp() {
		return this.prcFldTp;
	}

	public void setPrcFldTp(String prcFldTp) {
		this.prcFldTp = prcFldTp;
	}

	public String getSplrCd() {
		return this.splrCd;
	}

	public void setSplrCd(String splrCd) {
		this.splrCd = splrCd;
	}

	public String getSplrNmKj() {
		return this.splrNmKj;
	}

	public void setSplrNmKj(String splrNmKj) {
		this.splrNmKj = splrNmKj;
	}

	public String getSplrNmKn() {
		return this.splrNmKn;
	}

	public void setSplrNmKn(String splrNmKn) {
		this.splrNmKn = splrNmKn;
	}

	public String getStckTp() {
		return this.stckTp;
	}

	public void setStckTp(String stckTp) {
		this.stckTp = stckTp;
	}

	public String getTaxCd() {
		return this.taxCd;
	}

	public void setTaxCd(String taxCd) {
		this.taxCd = taxCd;
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

	public String getUntCd() {
		return this.untCd;
	}

	public void setUntCd(String untCd) {
		this.untCd = untCd;
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