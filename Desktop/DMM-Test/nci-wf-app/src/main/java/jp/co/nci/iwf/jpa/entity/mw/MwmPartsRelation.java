package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_RELATION database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_RELATION", uniqueConstraints=@UniqueConstraint(columnNames={"PARTS_ID", "TARGET_PARTS_ID", "COLUMN_NAME"}))
@NamedQuery(name="MwmPartsRelation.findAll", query="SELECT m FROM MwmPartsRelation m")
public class MwmPartsRelation extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_RELATION_ID")
	private long partsRelationId;

	@Column(name="PARTS_ID")
	private Long partsId;

	@Column(name="PARTS_IO_TYPE")
	private String partsIoType;

	@Column(name="COLUMN_NAME")
	private String columnName;

	@Column(name="NO_CHANGE_EVENT_FLAG")
	private String noChangeEventFlag;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="TARGET_PARTS_ID")
	private Long targetPartsId;

	@Column(name="WIDTH")
	private Integer width;

	public MwmPartsRelation() {
	}

	public long getPartsRelationId() {
		return this.partsRelationId;
	}

	public void setPartsRelationId(long partsRelationId) {
		this.partsRelationId = partsRelationId;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

	public String getPartsIoType() {
		return partsIoType;
	}

	public void setPartsIoType(String partsIoType) {
		this.partsIoType = partsIoType;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getNoChangeEventFlag() {
		return noChangeEventFlag;
	}

	public void setNoChangeEventFlag(String noChangeEventFlag) {
		this.noChangeEventFlag = noChangeEventFlag;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Long getTargetPartsId() {
		return this.targetPartsId;
	}

	public void setTargetPartsId(Long targetPartsId) {
		this.targetPartsId = targetPartsId;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

}