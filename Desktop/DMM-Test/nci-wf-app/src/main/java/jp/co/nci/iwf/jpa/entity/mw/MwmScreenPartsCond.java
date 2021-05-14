package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_SCREEN_PARTS_COND database table.
 *
 */
@Entity
@Table(name="MWM_SCREEN_PARTS_COND", uniqueConstraints=@UniqueConstraint(columnNames={"SCREEN_ID", "PARTS_ID", "PARTS_CONDITION_TYPE"}))
@NamedQuery(name="MwmScreenPartsCond.findAll", query="SELECT m FROM MwmScreenPartsCond m")
public class MwmScreenPartsCond extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_PARTS_COND_ID")
	private long screenPartsCondId;

	@Column(name="PARTS_CONDITION_TYPE")
	private String partsConditionType;

	@Column(name="PARTS_ID")
	private Long partsId;

	@Column(name="SCREEN_ID")
	private Long screenId;

	@Column(name="CALLBACK_FUNCTION")
	private String callbackFunction;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmScreenPartsCond() {
	}

	public long getScreenPartsCondId() {
		return this.screenPartsCondId;
	}

	public void setScreenPartsCondId(long screenPartsCondId) {
		this.screenPartsCondId = screenPartsCondId;
	}

	public String getPartsConditionType() {
		return this.partsConditionType;
	}

	public void setPartsConditionType(String partsCondtionType) {
		this.partsConditionType = partsCondtionType;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

	public Long getScreenId() {
		return this.screenId;
	}

	public void setScreenId(Long screenId) {
		this.screenId = screenId;
	}

	public String getCallbackFunction() {
		return callbackFunction;
	}

	public void setCallbackFunction(String callbackFunction) {
		this.callbackFunction = callbackFunction;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}