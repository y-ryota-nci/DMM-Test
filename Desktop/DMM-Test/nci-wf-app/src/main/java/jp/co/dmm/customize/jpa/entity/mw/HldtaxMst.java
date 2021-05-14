package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the HLDTAX_MST database table.
 *
 */
@Entity
@Table(name="HLDTAX_MST")
@NamedQuery(name="HldtaxMst.findAll", query="SELECT h FROM HldtaxMst h")
public class HldtaxMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private HldtaxMstPK id;

	@Column(name="ACC_CD")
	private String accCd;

	@Column(name="ACC_BRKDWN_CD")
	private String accBrkdwnCd;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="HLDTAX_NM")
	private String hldtaxNm;

	@Column(name="HLDTAX_RTO1")
	private BigDecimal hldtaxRto1;

	@Column(name="HLDTAX_RTO2")
	private BigDecimal hldtaxRto2;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="SORT_ORDER")
	private BigDecimal sortOrder;

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

	public HldtaxMst() {
	}

	public HldtaxMstPK getId() {
		return this.id;
	}

	public void setId(HldtaxMstPK id) {
		this.id = id;
	}

	public String getAccCd() {
		return accCd;
	}

	public void setAccCd(String accCd) {
		this.accCd = accCd;
	}

	public String getAccBrkdwnCd() {
		return accBrkdwnCd;
	}

	public void setAccBrkdwnCd(String accBrkdwnCd) {
		this.accBrkdwnCd = accBrkdwnCd;
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

	public String getHldtaxNm() {
		return this.hldtaxNm;
	}

	public void setHldtaxNm(String hldtaxNm) {
		this.hldtaxNm = hldtaxNm;
	}

	public BigDecimal getHldtaxRto1() {
		return this.hldtaxRto1;
	}

	public void setHldtaxRto1(BigDecimal hldtaxRto1) {
		this.hldtaxRto1 = hldtaxRto1;
	}

	public BigDecimal getHldtaxRto2() {
		return this.hldtaxRto2;
	}

	public void setHldtaxRto2(BigDecimal hldtaxRto2) {
		this.hldtaxRto2 = hldtaxRto2;
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

	public BigDecimal getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(BigDecimal sortOrder) {
		this.sortOrder = sortOrder;
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