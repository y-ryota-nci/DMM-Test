package jp.co.nci.iwf.component.tray;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * トレイ設定エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class TrayConfig extends BaseJpaEntity {

	@Id
	@Column(name="TRAY_CONFIG_ID")
	public long trayConfigId;

	@Version
	@Column(name="VERSION")
	public Long version;

	@Column(name="PAGE_SIZE")
	public Integer pageSize;

	@Column(name="TRAY_CONFIG_CODE")
	public String trayConfigCode;

	@Column(name="TRAY_CONFIG_NAME")
	public String trayConfigName;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="USER_CODE")
	public String userCode;

	@Column(name="USER_CODE_CREATED")
	public String userCodeCreated;

	@Column(name="USER_NAME_CREATED")
	public String userNameCreated;

	@Column(name="SYSTEM_FLAG")
	public String systemFlag;

	@Column(name="SYSTEM_FLAG_NAME")
	public String systemFlagName;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Column(name="PERSONAL_USE_FLAG")
	public String personalUseFlag;
}
