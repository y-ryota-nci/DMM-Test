package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the ITMEXPS_MST database table.
 *
 */
@Entity
@Table(name="ITMEXPS_MST")
@NamedQuery(name="ItmexpsMst.findAll", query="SELECT i FROM ItmexpsMst i")
public class ItmexpsMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ItmexpsMstPK id;

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

	@Column(name="ITMEXPS_NM")
	private String itmexpsNm;

	@Column(name="ITMEXPS_NM_S")
	private String itmexpsNmS;

	@Column(name="ITMEXPS_LEVEL")
	private BigDecimal itmexpsLevel;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Version
	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public ItmexpsMst() {
	}

	public ItmexpsMstPK getId() {
		return this.id;
	}

	public void setId(ItmexpsMstPK id) {
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

	public String getItmexpsNm() {
		return this.itmexpsNm;
	}

	public void setItmexpsNm(String itmexpsNm) {
		this.itmexpsNm = itmexpsNm;
	}

	public String getItmexpsNmS() {
		return this.itmexpsNmS;
	}

	public void setItmexpsNmS(String itmexpsNmS) {
		this.itmexpsNmS = itmexpsNmS;
	}

	public BigDecimal getItmexpsLevel() {
		return this.itmexpsLevel;
	}

	public void setItmexpsLevel(BigDecimal itmexpsLevel) {
		this.itmexpsLevel = itmexpsLevel;
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