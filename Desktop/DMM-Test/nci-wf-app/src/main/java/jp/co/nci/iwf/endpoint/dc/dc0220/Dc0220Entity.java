package jp.co.nci.iwf.endpoint.dc.dc0220;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 文書管理トレイ編集の文書管理トレイ設定エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Dc0220Entity extends BaseJpaEntity {

	@Id
	@Column(name="DOC_TRAY_CONFIG_ID")
	public long docTrayConfigId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="PAGE_SIZE")
	public Integer pageSize;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Column(name="SYSTEM_FLAG")
	public String systemFlag;

	@Column(name="DOC_TRAY_CONFIG_CODE")
	public String docTrayConfigCode;

	@Column(name="DOC_TRAY_CONFIG_NAME")
	public String docTrayConfigName;

	@Column(name="VERSION")
	public Long version;

	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	@Column(name="PERSONAL_USE_FLAG")
	public String personalUseFlag;
}
