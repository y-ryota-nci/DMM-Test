package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_DOC_TRAY_CONFIG database table.
 *
 */
@Entity
@Table(name="MWM_DOC_TRAY_CONFIG", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "DOC_TRAY_CONFIG_CODE"}))
@NamedQuery(name="MwmDocTrayConfig.findAll", query="SELECT m FROM MwmDocTrayConfig m")
public class MwmDocTrayConfig extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_TRAY_CONFIG_ID")
	private long docTrayConfigId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DOC_TRAY_CONFIG_CODE")
	private String docTrayConfigCode;

	@Column(name="DOC_TRAY_CONFIG_NAME")
	private String docTrayConfigName;

	@Column(name="PAGE_SIZE")
	private Integer pageSize;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="SYSTEM_FLAG")
	private String systemFlag;

	@Column(name="PERSONAL_USE_FLAG")
	private String personalUseFlag;

	public MwmDocTrayConfig() {
	}

	public long getDocTrayConfigId() {
		return this.docTrayConfigId;
	}

	public void setDocTrayConfigId(long docTrayConfigId) {
		this.docTrayConfigId = docTrayConfigId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getDocTrayConfigCode() {
		return this.docTrayConfigCode;
	}

	public void setDocTrayConfigCode(String docTrayConfigCode) {
		this.docTrayConfigCode = docTrayConfigCode;
	}

	public String getDocTrayConfigName() {
		return this.docTrayConfigName;
	}

	public void setDocTrayConfigName(String docTrayConfigName) {
		this.docTrayConfigName = docTrayConfigName;
	}

	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSystemFlag() {
		return this.systemFlag;
	}

	public void setSystemFlag(String systemFlag) {
		this.systemFlag = systemFlag;
	}

	public String getPersonalUseFlag() {
		return personalUseFlag;
	}

	public void setPersonalUseFlag(String personalUseFlag) {
		this.personalUseFlag = personalUseFlag;
	}

}