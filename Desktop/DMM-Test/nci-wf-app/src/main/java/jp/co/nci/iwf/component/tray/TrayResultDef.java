package jp.co.nci.iwf.component.tray;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * トレイ設定検索結果定義エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class TrayResultDef extends BaseJpaEntity {

	@Id
	@Column(name="TRAY_CONFIG_RESULT_ID")
	public long trayConfigResultId;

	@Column(name="ALIGN_TYPE")
	public String alignType;

	@Column(name="BUSINESS_INFO_CODE")
	public String businessInfoCode;

	@Column(name="BUSINESS_INFO_NAME")
	public String businessInfoName;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="COL_WIDTH")
	public Integer colWidth;

	@Column(name="INITIAL_SORT_DESC_FLAG")
	public String initialSortDescFlag;

	@Column(name="INITIAL_SORT_FLAG")
	public String initialSortFlag;

	@Column(name="LINK_FLAG")
	public String linkFlag;

	@Column(name="DATA_TYPE")
	public String dataType;
}
