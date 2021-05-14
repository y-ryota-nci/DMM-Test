package jp.co.nci.iwf.component.tray;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * トレイ設定検索条件定義エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class TrayConditionDef extends BaseJpaEntity {

	@Id
	@Column(name="TRAY_CONFIG_CONDITION_ID")
	public long trayConfigConditionId;

	@Column(name="BUSINESS_INFO_NAME")
	public String businessInfoName;

	@Column(name="BUSINESS_INFO_CODE")
	public String businessInfoCode;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="CONDITION_MATCH_TYPE")
	public String conditionMatchType;

	@Column(name="TRAY_INIT_VALUE1")
	public String trayInitValue1;

	@Column(name="TRAY_INIT_VALUE2")
	public String trayInitValue2;

	@Column(name="TRAY_INIT_VALUE3")
	public String trayInitValue3;

	@Column(name="TRAY_INIT_VALUE4")
	public String trayInitValue4;

	@Column(name="DATA_TYPE")
	public String dataType;
}
