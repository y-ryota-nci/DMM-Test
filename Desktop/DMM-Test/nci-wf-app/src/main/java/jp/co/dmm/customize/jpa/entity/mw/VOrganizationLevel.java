package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the V_ORGANIZATION_LEVEL database table.
 *
 */
@Entity
@Table(name="V_ORGANIZATION_LEVEL")
@NamedQuery(name="VOrganizationLevel.findAll", query="SELECT v FROM VOrganizationLevel v")
public class VOrganizationLevel extends jp.co.nci.iwf.jpa.entity.BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DELETE_FLAG")
	private String deleteFlag;

	@Id
	private long id;

	@Column(name="LOCALE_CODE")
	private String localeCode;

	@Column(name="ORGANIZATION_CODE")
	private String organizationCode;

	@Column(name="ORGANIZATION_CODE_1")
	private String organizationCode1;

	@Column(name="ORGANIZATION_CODE_2")
	private String organizationCode2;

	@Column(name="ORGANIZATION_CODE_3")
	private String organizationCode3;

	@Column(name="ORGANIZATION_CODE_4")
	private String organizationCode4;

	@Column(name="ORGANIZATION_CODE_5")
	private String organizationCode5;

	@Column(name="ORGANIZATION_CODE_UP")
	private String organizationCodeUp;

	@Column(name="ORGANIZATION_LEVEL")
	private BigDecimal organizationLevel;

	@Column(name="ORGANIZATION_LEVEL_1")
	private String organizationLevel1;

	@Column(name="ORGANIZATION_LEVEL_2")
	private String organizationLevel2;

	@Column(name="ORGANIZATION_LEVEL_3")
	private String organizationLevel3;

	@Column(name="ORGANIZATION_LEVEL_4")
	private String organizationLevel4;

	@Column(name="ORGANIZATION_LEVEL_5")
	private String organizationLevel5;

	@Column(name="ORGANIZATION_NAME")
	private String organizationName;

	@Column(name="ORGANIZATION_NAME_1")
	private String organizationName1;

	@Column(name="ORGANIZATION_NAME_2")
	private String organizationName2;

	@Column(name="ORGANIZATION_NAME_3")
	private String organizationName3;

	@Column(name="ORGANIZATION_NAME_4")
	private String organizationName4;

	@Column(name="ORGANIZATION_NAME_5")
	private String organizationName5;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	private Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	private Date validStartDate;

	public VOrganizationLevel() {
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getDeleteFlag() {
		return this.deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocaleCode() {
		return this.localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getOrganizationCode() {
		return this.organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getOrganizationCode1() {
		return this.organizationCode1;
	}

	public void setOrganizationCode1(String organizationCode1) {
		this.organizationCode1 = organizationCode1;
	}

	public String getOrganizationCode2() {
		return this.organizationCode2;
	}

	public void setOrganizationCode2(String organizationCode2) {
		this.organizationCode2 = organizationCode2;
	}

	public String getOrganizationCode3() {
		return this.organizationCode3;
	}

	public void setOrganizationCode3(String organizationCode3) {
		this.organizationCode3 = organizationCode3;
	}

	public String getOrganizationCode4() {
		return this.organizationCode4;
	}

	public void setOrganizationCode4(String organizationCode4) {
		this.organizationCode4 = organizationCode4;
	}

	public String getOrganizationCode5() {
		return this.organizationCode5;
	}

	public void setOrganizationCode5(String organizationCode5) {
		this.organizationCode5 = organizationCode5;
	}

	public String getOrganizationCodeUp() {
		return this.organizationCodeUp;
	}

	public void setOrganizationCodeUp(String organizationCodeUp) {
		this.organizationCodeUp = organizationCodeUp;
	}

	public BigDecimal getOrganizationLevel() {
		return this.organizationLevel;
	}

	public void setOrganizationLevel(BigDecimal organizationLevel) {
		this.organizationLevel = organizationLevel;
	}

	public String getOrganizationLevel1() {
		return this.organizationLevel1;
	}

	public void setOrganizationLevel1(String organizationLevel1) {
		this.organizationLevel1 = organizationLevel1;
	}

	public String getOrganizationLevel2() {
		return this.organizationLevel2;
	}

	public void setOrganizationLevel2(String organizationLevel2) {
		this.organizationLevel2 = organizationLevel2;
	}

	public String getOrganizationLevel3() {
		return this.organizationLevel3;
	}

	public void setOrganizationLevel3(String organizationLevel3) {
		this.organizationLevel3 = organizationLevel3;
	}

	public String getOrganizationLevel4() {
		return this.organizationLevel4;
	}

	public void setOrganizationLevel4(String organizationLevel4) {
		this.organizationLevel4 = organizationLevel4;
	}

	public String getOrganizationLevel5() {
		return this.organizationLevel5;
	}

	public void setOrganizationLevel5(String organizationLevel5) {
		this.organizationLevel5 = organizationLevel5;
	}

	public String getOrganizationName() {
		return this.organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationName1() {
		return this.organizationName1;
	}

	public void setOrganizationName1(String organizationName1) {
		this.organizationName1 = organizationName1;
	}

	public String getOrganizationName2() {
		return this.organizationName2;
	}

	public void setOrganizationName2(String organizationName2) {
		this.organizationName2 = organizationName2;
	}

	public String getOrganizationName3() {
		return this.organizationName3;
	}

	public void setOrganizationName3(String organizationName3) {
		this.organizationName3 = organizationName3;
	}

	public String getOrganizationName4() {
		return this.organizationName4;
	}

	public void setOrganizationName4(String organizationName4) {
		this.organizationName4 = organizationName4;
	}

	public String getOrganizationName5() {
		return this.organizationName5;
	}

	public void setOrganizationName5(String organizationName5) {
		this.organizationName5 = organizationName5;
	}

	public Date getValidEndDate() {
		return this.validEndDate;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	public Date getValidStartDate() {
		return this.validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

}