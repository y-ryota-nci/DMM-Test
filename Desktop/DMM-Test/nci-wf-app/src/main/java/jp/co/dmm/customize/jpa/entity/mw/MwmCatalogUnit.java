package jp.co.dmm.customize.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import jp.co.nci.iwf.jpa.entity.mw.MwmBaseJpaEntity;


/**
 * The persistent class for the MWM_UNIT database table.
 *
 */
@Entity
@Table(name="MWM_CATALOG_UNIT", uniqueConstraints=@UniqueConstraint(columnNames={"CORPORATION_CODE", "CATALOG_UNIT_CODE"}))
@NamedQuery(name="MwmCatalogUnit.findAll", query="SELECT m FROM MwmCatalogUnit m")
public class MwmCatalogUnit extends MwmBaseJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CATALOG_UNIT_ID")
	private long catalogUnitId;
	@Column(name="CORPORATION_CODE")
	private String corporationCode;
	@Column(name="CATALOG_UNIT_CODE")
	private String catalogUnitCode;
	@Column(name="CATALOG_UNIT_NAME")
	private String catalogUnitName;

	public MwmCatalogUnit() {
	}

	public long getCatalogUnitId() {
		return catalogUnitId;
	}
	public void setCatalogUnitId(long catalogUnitId) {
		this.catalogUnitId = catalogUnitId;
	}

	public String getCorporationCode() {
		return corporationCode;
	}
	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public String getCatalogUnitCode() {
		return catalogUnitCode;
	}
	public void setCatalogUnitCode(String catalogUnitCode) {
		this.catalogUnitCode = catalogUnitCode;
	}

	public String getCatalogUnitName() {
		return catalogUnitName;
	}
	public void setCatalogUnitName(String catalogUnitName) {
		this.catalogUnitName = catalogUnitName;
	}


}