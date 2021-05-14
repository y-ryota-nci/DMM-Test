package jp.co.nci.iwf.endpoint.wl.wl0011;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Wl0011Entity extends BaseJpaEntity {

	@Id
	@Column(name="TRAY_CONFIG_ID")
	public long trayConfigId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="PAGE_SIZE")
	public Integer pageSize;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Column(name="SYSTEM_FLAG")
	public String systemFlag;

	@Column(name="TRAY_CONFIG_CODE")
	public String trayConfigCode;

	@Column(name="TRAY_CONFIG_NAME")
	public String trayConfigName;

	@Column(name="VERSION")
	public Long version;

	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	@Column(name="PERSONAL_USE_FLAG")
	public String personalUseFlag;
}
