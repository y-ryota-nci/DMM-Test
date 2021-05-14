package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_COLUMN database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_COLUMN", uniqueConstraints=@UniqueConstraint(columnNames={"CONTAINER_ID", "COLUMN_NAME"}))
@NamedQuery(name="MwmPartsColumn.findAll", query="SELECT m FROM MwmPartsColumn m")
public class MwmPartsColumn extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_COLUMN_ID")
	private long partsColumnId;

	@Column(name="COLUMN_NAME")
	private String columnName;

	@Column(name="COLUMN_SIZE")
	private Integer columnSize;

	@Column(name="COLUMN_TYPE")
	private Integer columnType;

	private String comments;

	@Column(name="CONTAINER_ID")
	private Long containerId;

	@Column(name="DECIMAL_POINT")
	private Integer decimalPoint;

	@Column(name="PARTS_ID")
	private Long partsId;

	@Column(name="ROLE_CODE")
	private String roleCode;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmPartsColumn() {
	}

	public long getPartsColumnId() {
		return this.partsColumnId;
	}

	public void setPartsColumnId(long partsColumnId) {
		this.partsColumnId = partsColumnId;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getColumnSize() {
		return this.columnSize;
	}

	public void setColumnSize(Integer columnSize) {
		this.columnSize = columnSize;
	}

	public Integer getColumnType() {
		return this.columnType;
	}

	public void setColumnType(Integer columnType) {
		this.columnType = columnType;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getContainerId() {
		return this.containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public Integer getDecimalPoint() {
		return this.decimalPoint;
	}

	public void setDecimalPoint(Integer decimalPoint) {
		this.decimalPoint = decimalPoint;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}