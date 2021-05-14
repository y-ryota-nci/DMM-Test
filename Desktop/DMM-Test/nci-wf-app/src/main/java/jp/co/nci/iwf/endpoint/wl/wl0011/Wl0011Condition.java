package jp.co.nci.iwf.endpoint.wl.wl0011;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Wl0011Condition extends BaseJpaEntity {

	@Id
	@Column(name="TRAY_CONFIG_CONDITION_ID")
	public long trayConfigConditionId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="BUSINESS_INFO_CODE")
	public String businessInfoCode;

	@Column(name="BUSINESS_INFO_NAME")
	public String businessInfoName;

	@Column(name="CONDITION_MATCH_TYPE")
	public String conditionMatchType;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Column(name="TRAY_CONFIG_ID")
	public Long trayConfigId;

	@Column(name="TRAY_INIT_VALUE1")
	public String trayInitValue1;

	@Column(name="TRAY_INIT_VALUE2")
	public String trayInitValue2;

	@Column(name="TRAY_INIT_VALUE3")
	public String trayInitValue3;

	@Column(name="TRAY_INIT_VALUE4")
	public String trayInitValue4;

	@Column(name="VERSION")
	public Long version;
}
