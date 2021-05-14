package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_DOC_TRAY_CONFIG_RESULT database table.
 *
 */
@Entity
@Table(name="MWM_DOC_TRAY_CONFIG_RESULT", uniqueConstraints=@UniqueConstraint(columnNames={"DOC_TRAY_CONFIG_ID", "CORPORATION_CODE", "DOC_BUSINESS_INFO_CODE"}))
@NamedQuery(name="MwmDocTrayConfigResult.findAll", query="SELECT m FROM MwmDocTrayConfigResult m")
public class MwmDocTrayConfigResult extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_TRAY_CONFIG_RESULT_ID")
	private long docTrayConfigResultId;

	@Column(name="ALIGN_TYPE")
	private String alignType;

	@Column(name="COL_WIDTH")
	private Integer colWidth;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DOC_BUSINESS_INFO_CODE")
	private String docBusinessInfoCode;

	@Column(name="DOC_TRAY_CONFIG_ID")
	private Long docTrayConfigId;

	@Column(name="INITIAL_SORT_DESC_FLAG")
	private String initialSortDescFlag;

	@Column(name="INITIAL_SORT_FLAG")
	private String initialSortFlag;

	@Column(name="LINK_FLAG")
	private String linkFlag;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmDocTrayConfigResult() {
	}

	public long getDocTrayConfigResultId() {
		return this.docTrayConfigResultId;
	}

	public void setDocTrayConfigResultId(long docTrayConfigResultId) {
		this.docTrayConfigResultId = docTrayConfigResultId;
	}

	public String getAlignType() {
		return this.alignType;
	}

	public void setAlignType(String alignType) {
		this.alignType = alignType;
	}

	public Integer getColWidth() {
		return this.colWidth;
	}

	public void setColWidth(Integer colWidth) {
		this.colWidth = colWidth;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getDocBusinessInfoCode() {
		return this.docBusinessInfoCode;
	}

	public void setDocBusinessInfoCode(String docBusinessInfoCode) {
		this.docBusinessInfoCode = docBusinessInfoCode;
	}

	public Long getDocTrayConfigId() {
		return this.docTrayConfigId;
	}

	public void setDocTrayConfigId(Long docTrayConfigId) {
		this.docTrayConfigId = docTrayConfigId;
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

	public String getLinkFlag() {
		return this.linkFlag;
	}

	public void setLinkFlag(String linkFlag) {
		this.linkFlag = linkFlag;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}