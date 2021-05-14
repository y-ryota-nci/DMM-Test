package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_TRAY_CONFIG_RESULT database table.
 *
 */
@Entity
@Table(name="MWM_TRAY_CONFIG_RESULT", uniqueConstraints=@UniqueConstraint(columnNames={"TRAY_CONFIG_ID","CORPORATION_CODE","BUSINESS_INFO_CODE"}))
@NamedQuery(name="MwmTrayConfigResult.findAll", query="SELECT m FROM MwmTrayConfigResult m")
public class MwmTrayConfigResult extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TRAY_CONFIG_RESULT_ID")
	private long trayConfigResultId;

	@Column(name="ALIGN_TYPE")
	private String alignType;

	@Column(name="BUSINESS_INFO_CODE")
	private String businessInfoCode;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="COL_WIDTH")
	private Integer colWidth;

	@Column(name="LINK_FLAG")
	private String linkFlag;

	@Column(name="INITIAL_SORT_DESC_FLAG")
	private String initialSortDescFlag;

	@Column(name="INITIAL_SORT_FLAG")
	private String initialSortFlag;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="TRAY_CONFIG_ID")
	private Long trayConfigId;

	public MwmTrayConfigResult() {
	}

	public long getTrayConfigResultId() {
		return this.trayConfigResultId;
	}

	public void setTrayConfigResultId(long trayConfigResultId) {
		this.trayConfigResultId = trayConfigResultId;
	}

	public String getAlignType() {
		return alignType;
	}

	public void setAlignType(String alignType) {
		this.alignType = alignType;
	}

	public String getBusinessInfoCode() {
		return businessInfoCode;
	}

	public void setBusinessInfoCode(String businessInfoCode) {
		this.businessInfoCode = businessInfoCode;
	}

	public String getCorporationCode() {
		return corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Integer getColWidth() {
		return colWidth;
	}

	public void setColWidth(Integer colWidth) {
		this.colWidth = colWidth;
	}

	public String getLinkFlag() {
		return linkFlag;
	}

	public void setLinkFlag(String linkFlag) {
		this.linkFlag = linkFlag;
	}

	public String getInitialSortDescFlag() {
		return this.initialSortDescFlag;
	}

	public void setInitialSortDescFlag(String initialSortDescFlag) {
		this.initialSortDescFlag = initialSortDescFlag;
	}

	public String getInitialSortFlag() {
		return this.initialSortFlag;
	}

	public void setInitialSortFlag(String initialSortFlag) {
		this.initialSortFlag = initialSortFlag;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Long getTrayConfigId() {
		return this.trayConfigId;
	}

	public void setTrayConfigId(Long trayConfigId) {
		this.trayConfigId = trayConfigId;
	}

}