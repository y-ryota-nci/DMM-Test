package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_TABLE database table.
 *
 */
@Entity
@Table(name="MWM_TABLE", uniqueConstraints=@UniqueConstraint(columnNames={"TABLE_NAME"}))
@NamedQuery(name="MwmTable.findAll", query="SELECT m FROM MwmTable m")
public class MwmTable extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TABLE_ID")
	private long tableId;

	@Column(name="LOGICAL_TABLE_NAME")
	private String logicalTableName;

	@Column(name="TABLE_NAME")
	private String tableName;

	@Column(name="ENTITY_TYPE")
	private String entityType;

	public MwmTable() {
	}

	public long getTableId() {
		return this.tableId;
	}

	public void setTableId(long tableId) {
		this.tableId = tableId;
	}

	public String getLogicalTableName() {
		return this.logicalTableName;
	}

	public void setLogicalTableName(String logicalTableName) {
		this.logicalTableName = logicalTableName;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

}