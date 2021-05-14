package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_CATEGORY database table.
 *
 */
@Entity
@Table(name="MWM_CATEGORY", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "CATEGORY_CODE"}))
@NamedQuery(name="MwmCategory.findAll", query="SELECT m FROM MwmCategory m")
public class MwmCategory extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CATEGORY_ID")
	private long categoryId;

	@Column(name="CATEGORY_CODE")
	private String categoryCode;

	@Column(name="CATEGORY_NAME")
	private String categoryName;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	public MwmCategory() {
	}

	public long getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryCode() {
		return this.categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

}