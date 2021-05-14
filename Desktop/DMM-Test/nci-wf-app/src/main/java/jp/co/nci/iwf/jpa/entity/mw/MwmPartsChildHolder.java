package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_CHILD_HOLDER database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_CHILD_HOLDER", uniqueConstraints=@UniqueConstraint(columnNames={"PARTS_ID"}))
@NamedQuery(name="MwmPartsChildHolder.findAll", query="SELECT m FROM MwmPartsChildHolder m")
public class MwmPartsChildHolder extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_CHILD_HOLDER_ID")
	private long partsChildHolderId;

	@Column(name="CHILD_CONTAINER_ID")
	private Long childContainerId;

	@Column(name="INIT_ROW_COUNT")
	private Integer initRowCount;

	@Column(name="MIN_ROW_COUNT")
	private Integer minRowCount;

	@Column(name="PAGE_SIZE")
	private Integer pageSize;

	@Column(name="PARTS_ID")
	private Long partsId;

	public MwmPartsChildHolder() {
	}

	public long getPartsChildHolderId() {
		return this.partsChildHolderId;
	}

	public void setPartsChildHolderId(long partsChildHolderId) {
		this.partsChildHolderId = partsChildHolderId;
	}

	public Long getChildContainerId() {
		return this.childContainerId;
	}

	public void setChildContainerId(Long childContainerId) {
		this.childContainerId = childContainerId;
	}

	public Integer getInitRowCount() {
		return this.initRowCount;
	}

	public void setInitRowCount(Integer initRowCount) {
		this.initRowCount = initRowCount;
	}

	public Integer getMinRowCount() {
		return this.minRowCount;
	}

	public void setMinRowCount(Integer minRowCount) {
		this.minRowCount = minRowCount;
	}

	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

}