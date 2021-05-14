package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the BGT_ITM_MST database table.
 * 
 */
@Entity
@Table(name="BGT_ITM_MST")
@NamedQuery(name="BgtItmMst.findAll", query="SELECT b FROM BgtItmMst b")
public class BgtItmMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BgtItmMstPK id;

	@Column(name="BGT_ITM_NM")
	private String bgtItmNm;

	@Column(name="BS_PL_TP")
	private String bsPlTp;

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

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="TIMESTAMP_CREATED")
	private Timestamp timestampCreated;

	@Column(name="TIMESTAMP_UPDATED")
	private Timestamp timestampUpdated;

	@Column(name="USER_CODE_CREATED")
	private String userCodeCreated;

	@Column(name="USER_CODE_UPDATED")
	private String userCodeUpdated;

	public BgtItmMst() {
	}

	public BgtItmMstPK getId() {
		return this.id;
	}

	public void setId(BgtItmMstPK id) {
		this.id = id;
	}

	public String getBgtItmNm() {
		return this.bgtItmNm;
	}

	public void setBgtItmNm(String bgtItmNm) {
		this.bgtItmNm = bgtItmNm;
	}

	public String getBsPlTp() {
		return this.bsPlTp;
	}

	public void setBsPlTp(String bsPlTp) {
		this.bsPlTp = bsPlTp;
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

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Object getTimestampCreated() {
		return this.timestampCreated;
	}

	public void setTimestampCreated(Timestamp timestampCreated) {
		this.timestampCreated = timestampCreated;
	}

	public Object getTimestampUpdated() {
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