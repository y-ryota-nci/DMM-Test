package jp.co.nci.iwf.endpoint.dc.dc0220;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 文書管理トレイ編集の文書管理トレイ設定検索結果
 */
@Entity
@Access(AccessType.FIELD)
public class Dc0220Result extends BaseJpaEntity {

	@Id
	@Column(name="DOC_TRAY_CONFIG_RESULT_ID")
	public long docTrayConfigResultId;

	@Column(name="ALIGN_TYPE")
	public String alignType;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="DOC_BUSINESS_INFO_CODE")
	public String docBusinessInfoCode;

	@Column(name="DOC_BUSINESS_INFO_NAME")
	public String docBusinessInfoName;

	@Column(name="COL_WIDTH")
	public Integer colWidth;

	@Column(name="INITIAL_SORT_DESC_FLAG")
	public String initialSortDescFlag;

	@Column(name="INITIAL_SORT_FLAG")
	public String initialSortFlag;

	@Column(name="LINK_FLAG")
	public String linkFlag;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Column(name="VERSION")
	public Long version;
}
