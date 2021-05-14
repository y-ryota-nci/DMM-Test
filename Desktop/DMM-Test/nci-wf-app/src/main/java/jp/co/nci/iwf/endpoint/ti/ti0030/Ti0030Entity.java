package jp.co.nci.iwf.endpoint.ti.ti0030;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 汎用テーブル設定一覧の検索結果
 */
@Entity
@Access(AccessType.FIELD)
public class Ti0030Entity extends BaseJpaEntity {
	@Column(name="CATEGORY_ID")
	public Long categoryId;

	@Column(name="CATEGORY_NAME")
	public String categoryName;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="DELETE_FLAG")
	public String deleteFlag;

	@Column(name="ENTITY_TYPE")
	public String entityType;

	@Column(name="ENTITY_TYPE_NAME_TABLE")
	public String entityTypeNameTable;

	@Column(name="ENTITY_TYPE_NAME_VIEW")
	public String entityTypeNameView;

	@Id
	@Column(name="TABLE_ID")
	public Long tableId;

	@Column(name="TABLE_NAME")
	public String tableName;

	@Column(name="LOGICAL_TABLE_NAME")
	public String logicalTableName;

	@Version
	@Column(name="VERSION")
	public Long version;

	@Column(name="CONDITION_COUNT")
	public Integer conditionCount;

	@Column(name="CONTAINER_IN_USE")
	public Integer containerInUse;
}
