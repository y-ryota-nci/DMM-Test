package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 汎用テーブルエンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class MwmTableEx extends BaseJpaEntity {

	@Id
	@Column(name="TABLE_ID")
	public long tableId;

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
}
