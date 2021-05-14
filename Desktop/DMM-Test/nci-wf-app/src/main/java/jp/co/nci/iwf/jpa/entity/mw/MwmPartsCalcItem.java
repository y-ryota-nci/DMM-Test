package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_CALC_ITEM database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_CALC_ITEM", uniqueConstraints=@UniqueConstraint(columnNames={"PARTS_CALC_ID", "IDENTIFY_KEY"}))
@NamedQuery(name="MwmPartsCalcItem.findAll", query="SELECT m FROM MwmPartsCalcItem m")
public class MwmPartsCalcItem extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_CALC_ITEM_ID")
	private long partsCalcItemId;

	@Column(name="CALC_ITEM_TYPE")
	private String calcItemType;

	@Column(name="CALC_ITEM_VALUE")
	private String calcItemValue;

	@Column(name="IDENTIFY_KEY")
	private String identifyKey;

	@Column(name="PARTS_CALC_ID")
	private Long partsCalcId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	@Column(name="FORCE_CALC_FLAG")
	private String forceCalcFlag;

	public MwmPartsCalcItem() {
	}

	public long getPartsCalcItemId() {
		return this.partsCalcItemId;
	}

	public void setPartsCalcItemId(long partsCalcItemId) {
		this.partsCalcItemId = partsCalcItemId;
	}

	public String getCalcItemType() {
		return this.calcItemType;
	}

	public void setCalcItemType(String calcItemType) {
		this.calcItemType = calcItemType;
	}

	public String getCalcItemValue() {
		return this.calcItemValue;
	}

	public void setCalcItemValue(String calcItemValue) {
		this.calcItemValue = calcItemValue;
	}

	public String getIdentifyKey() {
		return identifyKey;
	}

	public void setIdentifyKey(String identifyKey) {
		this.identifyKey = identifyKey;
	}

	public Long getPartsCalcId() {
		return this.partsCalcId;
	}

	public void setPartsCalcId(Long partsCalcId) {
		this.partsCalcId = partsCalcId;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getForceCalcFlag() {
		return forceCalcFlag;
	}

	public void setForceCalcFlag(String forceCalcFlag) {
		this.forceCalcFlag = forceCalcFlag;
	}

}