package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_TABLE_INFO database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_TABLE_INFO", uniqueConstraints=@UniqueConstraint(columnNames={"PARTS_ID", "TABLE_SEARCH_ID"}))
@NamedQuery(name="MwmPartsTableInfo.findAll", query="SELECT m FROM MwmPartsTableInfo m")
public class MwmPartsTableInfo extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_TABLE_INFO_ID")
	private long partsTableInfoId;

	@Column(name="PARTS_ID")
	private Long partsId;

	@Column(name="TABLE_ID")
	private Long tableId;

	@Column(name="TABLE_SEARCH_ID")
	private Long tableSearchId;

	public MwmPartsTableInfo() {
	}

	public long getPartsTableInfoId() {
		return this.partsTableInfoId;
	}

	public void setPartsTableInfoId(long partsTableInfoId) {
		this.partsTableInfoId = partsTableInfoId;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

	public Long getTableId() {
		return this.tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public Long getTableSearchId() {
		return this.tableSearchId;
	}

	public void setTableSearchId(Long tableSearchId) {
		this.tableSearchId = tableSearchId;
	}

}