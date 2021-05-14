package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_SCREEN_PARTS_COND_ITEM database table.
 *
 */
@Entity
@Table(name="MWM_SCREEN_PARTS_COND_ITEM", uniqueConstraints=@UniqueConstraint(columnNames={"SCREEN_PARTS_COND_ID", "IDENTIFY_KEY"}))
@NamedQuery(name="MwmScreenPartsCondItem.findAll", query="SELECT m FROM MwmScreenPartsCondItem m")
public class MwmScreenPartsCondItem extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SCREEN_PARTS_COND_ITEM_ID")
	private long screenPartsCondItemId;

	@Column(name="ITEM_CLASS")
	private String itemClass;

	@Column(name="OPERATOR")
	private String operator;

	@Column(name="COND_TYPE")
	private String condType;

	@Column(name="IDENTIFY_KEY")
	private String identifyKey;

	@Column(name="NUMERIC_FLAG")
	private String numericFlag;

	@Column(name="SCREEN_PARTS_COND_ID")
	private Long screenPartsCondId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="TARGET_LITERAL_VAL")
	private String targetLiteralVal;

	@Column(name="TARGET_PARTS_ID")
	private Long targetPartsId;

	public MwmScreenPartsCondItem() {
	}

	public long getScreenPartsCondItemId() {
		return this.screenPartsCondItemId;
	}

	public void setScreenPartsCondItemId(long screenPartsCondItemId) {
		this.screenPartsCondItemId = screenPartsCondItemId;
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

	public Long getScreenPartsCondId() {
		return this.screenPartsCondId;
	}

	public void setScreenPartsCondId(Long screenPartsCondId) {
		this.screenPartsCondId = screenPartsCondId;
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