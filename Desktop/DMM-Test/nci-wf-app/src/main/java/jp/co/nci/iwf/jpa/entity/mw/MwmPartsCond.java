package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_COND database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_COND", uniqueConstraints=@UniqueConstraint(columnNames={"PARTS_ID", "PARTS_CONDITION_TYPE"}))
@NamedQuery(name="MwmPartsCond.findAll", query="SELECT m FROM MwmPartsCond m")
public class MwmPartsCond extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_COND_ID")
	private long partsCondId;

	@Column(name="PARTS_CONDITION_TYPE")
	private String partsConditionType;

	@Column(name="PARTS_ID")
	private Long partsId;

	@Column(name="CALLBACK_FUNCTION")
	private String callbackFunction;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmPartsCond() {
	}

	public long getPartsCondId() {
		return this.partsCondId;
	}

	public void setPartsCondId(long partsCondId) {
		this.partsCondId = partsCondId;
	}

	public String getPartsConditionType() {
		return this.partsConditionType;
	}

	public void setPartsConditionType(String partsConditionType) {
		this.partsConditionType = partsConditionType;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
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