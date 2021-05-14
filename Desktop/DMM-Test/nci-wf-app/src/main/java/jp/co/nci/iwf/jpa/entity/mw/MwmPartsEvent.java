package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_EVENT database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_EVENT", uniqueConstraints=@UniqueConstraint(columnNames={"PARTS_ID", "EVENT_NAME"}))
@NamedQuery(name="MwmPartsEvent.findAll", query="SELECT m FROM MwmPartsEvent m")
public class MwmPartsEvent extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_EVENT_ID")
	private long partsEventId;

	@Column(name="EVENT_NAME")
	private String eventName;

	@Column(name="FUNCTION_NAME")
	private String functionName;

	@Column(name="FUNCTION_PARAMETER")
	private String functionParameter;

	@Column(name="PARTS_ID")
	private Long partsId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmPartsEvent() {
	}

	public long getPartsEventId() {
		return this.partsEventId;
	}

	public void setPartsEventId(long partsEventId) {
		this.partsEventId = partsEventId;
	}

	public String getEventName() {
		return this.eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getFunctionName() {
		return this.functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionParameter() {
		return this.functionParameter;
	}

	public void setFunctionParameter(String functionParameter) {
		this.functionParameter = functionParameter;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}