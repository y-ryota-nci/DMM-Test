package jp.co.nci.iwf.designer.service.tableInfo;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * テーブルのメタ情報
 */
@Entity
@Access(AccessType.FIELD)
public class TableMetaData extends BaseJpaEntity implements Serializable {
	@Id
	@Column(name="TABLE_NAME")
	public String tableName;

	@Column(name="COMMENT")
	public String comment;

	@Column(name="ENTITY_TYPE")
	public String entityType;
}
