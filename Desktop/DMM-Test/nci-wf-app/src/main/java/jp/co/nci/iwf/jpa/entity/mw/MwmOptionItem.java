package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_OPTION_ITEM database table.
 *
 */
@Entity
@Table(name="MWM_OPTION_ITEM", uniqueConstraints=@UniqueConstraint(columnNames={"OPTION_ID", "CODE"}))
@NamedQuery(name="MwmOptionItem.findAll", query="SELECT m FROM MwmOptionItem m")
public class MwmOptionItem extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OPTION_ITEM_ID")
	private long optionItemId;

	private String code;

	@Column(name="\"LABEL\"")
	private String label;

	@Column(name="OPTION_ID")
	private Long optionId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmOptionItem() {
	}

	public long getOptionItemId() {
		return this.optionItemId;
	}

	public void setOptionItemId(long optionItemId) {
		this.optionItemId = optionItemId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Long getOptionId() {
		return this.optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}