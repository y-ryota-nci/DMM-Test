package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_COND_ITEM database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_COND_ITEM", uniqueConstraints=@UniqueConstraint(columnNames={"PARTS_COND_ID", "IDENTIFY_KEY"}))
@NamedQuery(name="MwmPartsCondItem.findAll", query="SELECT m FROM MwmPartsCondItem m")
public class MwmPartsCondItem extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_COND_ITEM_ID")
	private long partsCondItemId;

	@Column(name="ITEM_CLASS")
	private String itemClass;

	@Column(name="COND_TYPE")
	private String condType;

	@Column(name="OPERATOR")
	private String operator;

	@Column(name="IDENTIFY_KEY")
	private String identifyKey;

	@Column(name="NUMERIC_FLAG")
	private String numericFlag;

	@Column(name="PARTS_COND_ID")
	private Long partsCondId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="TARGET_LITERAL_VAL")
	private String targetLiteralVal;

	@Column(name="TARGET_PARTS_ID")
	private Long targetPartsId;

	public MwmPartsCondItem() {
	}

	public long getPartsCondItemId() {
		return this.partsCondItemId;
	}

	public void setPartsCondItemId(long partsCondItemId) {
		this.partsCondItemId = partsCondItemId;
	}

	public String getItemClass() {
		return this.itemClass;
	}

	public void setItemClass(String itemClass) {
		this.itemClass = itemClass;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCondType() {
		return this.condType;
	}

	public void setCondType(String condType) {
		this.condType = condType;
	}

	public String getIdentifyKey() {
		return this.identifyKey;
	}

	public void setIdentifyKey(String identifyKey) {
		this.identifyKey = identifyKey;
	}

	public String getNumericFlag() {
		return this.numericFlag;
	}

	public void setNumericFlag(String numericFlag) {
		this.numericFlag = numericFlag;
	}

	public Long getPartsCondId() {
		return this.partsCondId;
	}

	public void setPartsCondId(Long partsCondId) {
		this.partsCondId = partsCondId;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getTargetLiteralVal() {
		return this.targetLiteralVal;
	}

	public void setTargetLiteralVal(String targetLiteralVal) {
		this.targetLiteralVal = targetLiteralVal;
	}

	public Long getTargetPartsId() {
		return this.targetPartsId;
	}

	public void setTargetPartsId(Long targetPartsId) {
		this.targetPartsId = targetPartsId;
	}

}