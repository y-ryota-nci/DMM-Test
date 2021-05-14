package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the TAX_MST database table.
 * 
 */
@Entity
@Table(name="TAX_MST")
@NamedQuery(name="TaxMst.findAll", query="SELECT t FROM TaxMst t")
public class TaxMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TaxMstPK id;

	@Column(name="ACC_BRKDWN_CD")
	private String accBrkdwnCd;

	@Column(name="ACC_CD")
	private String accCd;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DC_TP")
	private String dcTp;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="FRC_TP")
	private String frcTp;

	@Column(name="FRC_UNT")
	private String frcUnt;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="TAX_CD_SS")
	private String taxCdSs;

	@Column(name="TAX_NM")
	private String taxNm;

	@Column(name="TAX_NM_S")
	private String taxNmS;

	@Column(name="TAX_RTO")
	private BigDecimal taxRto;

	@Column(name="TAX_TP")
	private String taxTp;

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

	public TaxMst() {
	}

	public TaxMstPK getId() {
		return this.id;
	}

	public void setId(TaxMstPK id) {
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

	public String getFrcTp() {
		return this.frcTp;
	}

	public void setFrcTp(String frcTp) {
		this.frcTp = frcTp;
	}

	public String getFrcUnt() {
		return this.frcUnt;
	}

	public void setFrcUnt(String frcUnt) {
		this.frcUnt = frcUnt;
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

	public String getTaxNm() {
		return this.taxNm;
	}

	public void setTaxNm(String taxNm) {
		this.taxNm = taxNm;
	}

	public String getTaxNmS() {
		return this.taxNmS;
	}

	public void setTaxNmS(String taxNmS) {
		this.taxNmS = taxNmS;
	}

	public BigDecimal getTaxRto() {
		return this.taxRto;
	}

	public void setTaxRto(BigDecimal taxRto) {
		this.taxRto = taxRto;
	}

	public String getTaxTp() {
		return this.taxTp;
	}

	public void setTaxTp(String taxTp) {
		this.taxTp = taxTp;
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