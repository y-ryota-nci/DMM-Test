package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_TRAY_CONFIG database table.
 *
 */
@Entity
@Table(name="MWM_TRAY_CONFIG", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "TRAY_CONFIG_CODE"}))
@NamedQuery(name="MwmTrayConfig.findAll", query="SELECT m FROM MwmTrayConfig m")
public class MwmTrayConfig extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TRAY_CONFIG_ID")
	private long trayConfigId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="PAGE_SIZE")
	private Integer pageSize;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="SYSTEM_FLAG")
	private String systemFlag;

	@Column(name="TRAY_CONFIG_CODE")
	private String trayConfigCode;

	@Column(name="TRAY_CONFIG_NAME")
	private String trayConfigName;

	@Column(name="PERSONAL_USE_FLAG")
	private String personalUseFlag;

	public MwmTrayConfig() {
	}

	public long getTrayConfigId() {
		return this.trayConfigId;
	}

	public void setTrayConfigId(long trayConfigId) {
		this.trayConfigId = trayConfigId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
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

	public String getSystemFlag() {
		return systemFlag;
	}

	public void setSystemFlag(String systemFlag) {
		this.systemFlag = systemFlag;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getTrayConfigCode() {
		return trayConfigCode;
	}

	public void setTrayConfigCode(String trayConfigCode) {
		this.trayConfigCode = trayConfigCode;
	}

	public String getTrayConfigName() {
		return this.trayConfigName;
	}

	public void setTrayConfigName(String trayConfigName) {
		this.trayConfigName = trayConfigName;
	}

	public String getPersonalUseFlag() {
		return personalUseFlag;
	}

	public void setPersonalUseFlag(String personalUseFlag) {
		this.personalUseFlag = personalUseFlag;
	}
}