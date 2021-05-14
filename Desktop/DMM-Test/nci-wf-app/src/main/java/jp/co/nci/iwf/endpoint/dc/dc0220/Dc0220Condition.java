package jp.co.nci.iwf.endpoint.dc.dc0220;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 文書トレイ編集の文書管理トレイ設定検索条件
 */
@Entity
@Access(AccessType.FIELD)
public class Dc0220Condition extends BaseJpaEntity {

	@Id
	@Column(name="DOC_TRAY_CONFIG_CONDITION_ID")
	public long docTrayConfigConditionId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="DOC_BUSINESS_INFO_CODE")
	public String docBusinessInfoCode;

	@Column(name="DOC_BUSINESS_INFO_NAME")
	public String docBusinessInfoName;

	@Column(name="CONDITION_MATCH_TYPE")
	public String conditionMatchType;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Column(name="DOC_TRAY_CONFIG_ID")
	public Long docTrayConfigId;

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
