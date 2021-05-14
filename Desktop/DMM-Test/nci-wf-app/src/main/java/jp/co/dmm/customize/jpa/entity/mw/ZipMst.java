package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the ZIP_MST database table.
 * 
 */
@Entity
@Table(name="ZIP_MST")
@NamedQuery(name="ZipMst.findAll", query="SELECT z FROM ZipMst z")
public class ZipMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ZipMstPK id;

	@Column(name="ADR_PRF")
	private String adrPrf;

	@Column(name="ADR_PRF_CD")
	private String adrPrfCd;

	@Column(name="ADR_PRF_KN")
	private String adrPrfKn;

	private String adr1;

	@Column(name="ADR1_KN")
	private String adr1Kn;

	private String adr2;

	@Column(name="ADR2_KN")
	private String adr2Kn;

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

	public ZipMst() {
	}

	public ZipMstPK getId() {
		return this.id;
	}

	public void setId(ZipMstPK id) {
		this.id = id;
	}

	public String getAdrPrf() {
		return this.adrPrf;
	}

	public void setAdrPrf(String adrPrf) {
		this.adrPrf = adrPrf;
	}

	public String getAdrPrfCd() {
		return this.adrPrfCd;
	}

	public void setAdrPrfCd(String adrPrfCd) {
		this.adrPrfCd = adrPrfCd;
	}

	public String getAdrPrfKn() {
		return this.adrPrfKn;
	}

	public void setAdrPrfKn(String adrPrfKn) {
		this.adrPrfKn = adrPrfKn;
	}

	public String getAdr1() {
		return this.adr1;
	}

	public void setAdr1(String adr1) {
		this.adr1 = adr1;
	}

	public String getAdr1Kn() {
		return this.adr1Kn;
	}

	public void setAdr1Kn(String adr1Kn) {
		this.adr1Kn = adr1Kn;
	}

	public String getAdr2() {
		return this.adr2;
	}

	public void setAdr2(String adr2) {
		this.adr2 = adr2;
	}

	public String getAdr2Kn() {
		return this.adr2Kn;
	}

	public void setAdr2Kn(String adr2Kn) {
		this.adr2Kn = adr2Kn;
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

}