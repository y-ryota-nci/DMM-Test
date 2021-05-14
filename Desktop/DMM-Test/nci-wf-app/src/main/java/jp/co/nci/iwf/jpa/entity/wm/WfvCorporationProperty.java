package jp.co.nci.iwf.jpa.entity.wm;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;


/**
 * The persistent class for the WFV_CORPORATION_PROPERTY database table.
 *
 */
@Entity
@Table(name="WFV_CORPORATION_PROPERTY")
@NamedQuery(name="WfvCorporationProperty.findAll", query="SELECT w FROM WfvCorporationProperty w")
public class WfvCorporationProperty extends BaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private long id;

	@Column(name="PROPERTY_NAME")
	private String propertyName;

	@Column(name="PROPERTY_VALUE")
	private String propertyValue;

	@Column(name="SORT_ORDER")
	private BigDecimal sortOrder;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="PROPERTY_CODE")
	private String propertyCode;

	public WfvCorporationProperty() {
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return this.propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public BigDecimal getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(BigDecimal sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getCorporationCode() {
		return corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getPropertyCode() {
		return propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}