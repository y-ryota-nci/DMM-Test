package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_MAIL_CONFIG database table.
 *
 */
@Entity
@Table(name="MWM_MAIL_CONFIG", uniqueConstraints=@UniqueConstraint(columnNames={"CONFIG_CODE"}))
@NamedQuery(name="MwmMailConfig.findAll", query="SELECT m FROM MwmMailConfig m")
public class MwmMailConfig extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MAIL_CONFIG_ID")
	private long mailConfigId;

	@Column(name="CONFIG_CODE")
	private String configCode;

	@Column(name="CONFIG_NAME")
	private String configName;

	@Column(name="CONFIG_VALUE")
	private String configValue;

	private String remarks;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmMailConfig() {
	}

	public long getMailConfigId() {
		return this.mailConfigId;
	}

	public void setMailConfigId(long mailConfigId) {
		this.mailConfigId = mailConfigId;
	}

	public String getConfigCode() {
		return this.configCode;
	}

	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}

	public String getConfigName() {
		return this.configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigValue() {
		return this.configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}