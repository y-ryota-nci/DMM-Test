package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_SCREEN_PROCESS_DEF database table.
 *
 */
@Entity
@Table(name="MWM_SCREEN_PROCESS_DEF", uniqueConstraints={
		@UniqueConstraint(columnNames={"CORPORATION_CODE", "PROCESS_DEF_CODE", "PROCESS_DEF_DETAIL_CODE", "SCREEN_ID"}),
		@UniqueConstraint(columnNames={"CORPORATION_CODE", "SCREEN_PROCESS_CODE"})
})
@NamedQuery(name="MwmScreenProcessDef.findAll", query="SELECT m FROM MwmScreenProcessDef m")
public class MwmScreenProcessDef extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_PROCESS_ID")
	private long screenProcessId;

	@Column(name="SCREEN_PROCESS_CODE")
	private String screenProcessCode;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	private String description;

	@Column(name="PROCESS_DEF_CODE")
	private String processDefCode;

	@Column(name="PROCESS_DEF_DETAIL_CODE")
	private String processDefDetailCode;

	@Column(name="SCREEN_ID")
	private Long screenId;

	@Column(name="SCREEN_PROCESS_LEVEL_ID")
	private Long screenProcessLevelId;

	@Column(name="SCREEN_PROCESS_NAME")
	private String screenProcessName;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_END_DATE")
	private Date validEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="VALID_START_DATE")
	private Date validStartDate;

	public MwmScreenProcessDef() {
	}

	public long getScreenProcessId() {
		return this.screenProcessId;
	}

	public void setScreenProcessId(long screenProcessId) {
		this.screenProcessId = screenProcessId;
	}

	public String getScreenProcessCode() {
		return screenProcessCode;
	}

	public void setScreenProcessCode(String screenProcessCode) {
		this.screenProcessCode = screenProcessCode;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProcessDefCode() {
		return this.processDefCode;
	}

	public void setProcessDefCode(String processDefCode) {
		this.processDefCode = processDefCode;
	}

	public String getProcessDefDetailCode() {
		return this.processDefDetailCode;
	}

	public void setProcessDefDetailCode(String processDefDetailCode) {
		this.processDefDetailCode = processDefDetailCode;
	}

	public Long getScreenId() {
		return this.screenId;
	}

	public void setScreenId(Long screenId) {
		this.screenId = screenId;
	}

	public Long getScreenProcessLevelId() {
		return this.screenProcessLevelId;
	}

	public void setScreenProcessLevelId(Long screenProcessLevelId) {
		this.screenProcessLevelId = screenProcessLevelId;
	}

	public String getScreenProcessName() {
		return this.screenProcessName;
	}

	public void setScreenProcessName(String screenProcessName) {
		this.screenProcessName = screenProcessName;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Date getValidEndDate() {
		return this.validEndDate;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	public Date getValidStartDate() {
		return this.validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

}