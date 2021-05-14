package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the CRDCRD_BNKACC_MST database table.
 *
 */
@Entity
@Table(name="CRDCRD_BNKACC_MST")
@NamedQuery(name="CrdcrdBnkaccMst.findAll", query="SELECT c FROM CrdcrdBnkaccMst c")
public class CrdcrdBnkaccMst extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CrdcrdBnkaccMstPK id;

	@Column(name="BNKACC_CD")
	private String bnkaccCd;

	@Column(name="BNKACC_CHRG_DT")
	private String bnkaccChrgDt;

	@Column(name="CORPORATION_CODE_CREATED")
	private String corporationCodeCreated;

	@Column(name="CORPORATION_CODE_UPDATED")
	private String corporationCodeUpdated;

	@Column(name="CRD_COMPANY_NM")
	private String crdCompanyNm;

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

	public CrdcrdBnkaccMst() {
	}

	public CrdcrdBnkaccMstPK getId() {
		return this.id;
	}

	public void setId(CrdcrdBnkaccMstPK id) {
		this.id = id;
	}

	public String getBnkaccCd() {
		return this.bnkaccCd;
	}

	public void setBnkaccCd(String bnkaccCd) {
		this.bnkaccCd = bnkaccCd;
	}

	public String getBnkaccChrgDt() {
		return this.bnkaccChrgDt;
	}

	public void setBnkaccChrgDt(String bnkaccChrgDt) {
		this.bnkaccChrgDt = bnkaccChrgDt;
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

	public String getCrdCompanyNm() {
		return this.crdCompanyNm;
	}

	public void setCrdCompanyNm(String crdCompanyNm) {
		this.crdCompanyNm = crdCompanyNm;
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