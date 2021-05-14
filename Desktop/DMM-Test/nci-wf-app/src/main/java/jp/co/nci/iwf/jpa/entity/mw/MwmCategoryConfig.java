package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_CATEGORY_CONFIG database table.
 *
 */
@Entity
@Table(name="MWM_CATEGORY_CONFIG", uniqueConstraints={
		@UniqueConstraint(columnNames={"CORPORATION_CODE", "TABLE_ID"}),
		@UniqueConstraint(columnNames={"CATEGORY_ID", "TABLE_ID"})
})
@NamedQuery(name="MwmCategoryConfig.findAll", query="SELECT m FROM MwmCategoryConfig m")
public class MwmCategoryConfig extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CATEGORY_CONFIG_ID")
	private long categoryConfigId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="CATEGORY_ID")
	private Long categoryId;

	@Column(name="TABLE_ID")
	private Long tableId;

	public MwmCategoryConfig() {
	}

	public long getCategoryConfigId() {
		return this.categoryConfigId;
	}

	public void setCategoryConfigId(long categoryConfigId) {
		this.categoryConfigId = categoryConfigId;
	}

	public String getCorporationCode() {
		return corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Long getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getTableId() {
		return this.tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

}