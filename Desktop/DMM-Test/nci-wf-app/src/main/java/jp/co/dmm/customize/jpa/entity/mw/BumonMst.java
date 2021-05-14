package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the BUMON_MST database table.
 * 
 */
@Entity
@Table(name="BUMON_MST")
@NamedQuery(name="BumonMst.findAll", query="SELECT b FROM BumonMst b")
public class BumonMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BumonMstPK id;

	@Column(name="AREA_CD")
	private String areaCd;

	@Column(name="BUMON_NM")
	private String bumonNm;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="DLT_FG")
	private String dltFg;

	@Column(name="ENTRP_CD")
	private String entrpCd;

	@Column(name="ENTRP_TP_CD")
	private String entrpTpCd;

	@Column(name="IP_CREATED")
	private String ipCreated;

	@Column(name="IP_UPDATED")
	private String ipUpdated;

	@Column(name="SITE_CD")
	private String siteCd;

	@Column(name="TAB_CD")
	private String tabCd;

	@Column(name="TAX_KND_CD")
	private String taxKndCd;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Version
	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="TP_CD")
	private String tpCd;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public BumonMst() {
	}

	public BumonMstPK getId() {
		return this.id;
	}

	public void setId(BumonMstPK id) {
		this.id = id;
	}

	public String getAreaCd() {
		return this.areaCd;
	}

	public void setAreaCd(String areaCd) {
		this.areaCd = areaCd;
	}

	public String getBumonNm() {
		return this.bumonNm;
	}

	public void setBumonNm(String bumonNm) {
		this.bumonNm = bumonNm;
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

	public String getEntrpCd() {
		return this.entrpCd;
	}

	public void setEntrpCd(String entrpCd) {
		this.entrpCd = entrpCd;
	}

	public String getEntrpTpCd() {
		return this.entrpTpCd;
	}

	public void setEntrpTpCd(String entrpTpCd) {
		this.entrpTpCd = entrpTpCd;
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

	public String getSiteCd() {
		return this.siteCd;
	}

	public void setSiteCd(String siteCd) {
		this.siteCd = siteCd;
	}

	public String getTabCd() {
		return this.tabCd;
	}

	public void setTabCd(String tabCd) {
		this.tabCd = tabCd;
	}

	public String getTaxKndCd() {
		return this.taxKndCd;
	}

	public void setTaxKndCd(String taxKndCd) {
		this.taxKndCd = taxKndCd;
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

	public String getTpCd() {
		return this.tpCd;
	}

	public void setTpCd(String tpCd) {
		this.tpCd = tpCd;
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