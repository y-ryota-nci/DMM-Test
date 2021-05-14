package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_DOC_TRAY_CONFIG_CONDITION database table.
 *
 */
@Entity
@Table(name="MWM_DOC_TRAY_CONFIG_CONDITION", uniqueConstraints=@UniqueConstraint(columnNames={"DOC_TRAY_CONFIG_ID", "CORPORATION_CODE", "DOC_BUSINESS_INFO_CODE"}))
@NamedQuery(name="MwmDocTrayConfigCondition.findAll", query="SELECT m FROM MwmDocTrayConfigCondition m")
public class MwmDocTrayConfigCondition extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_TRAY_CONFIG_CONDITION_ID")
	private long docTrayConfigConditionId;

	@Column(name="CONDITION_MATCH_TYPE")
	private String conditionMatchType;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DOC_BUSINESS_INFO_CODE")
	private String docBusinessInfoCode;

	@Column(name="DOC_TRAY_CONFIG_ID")
	private Long docTrayConfigId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="TRAY_INIT_VALUE1")
	private String trayInitValue1;

	@Column(name="TRAY_INIT_VALUE2")
	private String trayInitValue2;

	@Column(name="TRAY_INIT_VALUE3")
	private String trayInitValue3;

	@Column(name="TRAY_INIT_VALUE4")
	private String trayInitValue4;

	public MwmDocTrayConfigCondition() {
	}

	public long getDocTrayConfigConditionId() {
		return this.docTrayConfigConditionId;
	}

	public void setDocTrayConfigConditionId(long docTrayConfigConditionId) {
		this.docTrayConfigConditionId = docTrayConfigConditionId;
	}

	public String getConditionMatchType() {
		return this.conditionMatchType;
	}

	public void setConditionMatchType(String conditionMatchType) {
		this.conditionMatchType = conditionMatchType;
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

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getTrayInitValue1() {
		return this.trayInitValue1;
	}

	public void setTrayInitValue1(String trayInitValue1) {
		this.trayInitValue1 = trayInitValue1;
	}

	public String getTrayInitValue2() {
		return this.trayInitValue2;
	}

	public void setTrayInitValue2(String trayInitValue2) {
		this.trayInitValue2 = trayInitValue2;
	}

	public String getTrayInitValue3() {
		return this.trayInitValue3;
	}

	public void setTrayInitValue3(String trayInitValue3) {
		this.trayInitValue3 = trayInitValue3;
	}

	public String getTrayInitValue4() {
		return this.trayInitValue4;
	}

	public void setTrayInitValue4(String trayInitValue4) {
		this.trayInitValue4 = trayInitValue4;
	}

}