package jp.co.nci.iwf.endpoint.ti.ti0050;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 汎用テーブル検索設定一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Ti0050Entity extends BaseJpaEntity {

	@Id
	@Column(name="TABLE_SEARCH_ID")
	public long tableSearchId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="DEFAULT_SEARCH_FLAG")
	public String defaultSearchFlag;

	@Column(name="DEFAULT_SEARCH_FLAG_FLAG")
	public String defaultSearchFlagName;

	@Column(name="TABLE_ID")
	public Long tableId;

	@Column(name="TABLE_SEARCH_CODE")
	public String tableSearchCode;

	@Column(name="TABLE_SEARCH_NAME")
	public String tableSearchName;

	@Column(name="LOGICAL_TABLE_NAME")
	public String logicalTableName;

	@Column(name="TABLE_NAME")
	public String tableName;

	@Column(name="ENTITY_TYPE")
	public String entityType;

	@Column(name="ENTITY_TYPE_NAME_TABLE")
	public String entityTypeNameTable;

	@Column(name="ENTITY_TYPE_NAME_VIEW")
	public String entityTypeNameView;

	@Column(name="VERSION")
	public Long version;

	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	@Column(name="DELETE_FLAG_NAME")
	public String deleteFlagName;

	@Column(name="DISPLAY_NAME")
	public String displayName;

	@Transient
	public String containerNames;
}
