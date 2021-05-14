package jp.co.nci.iwf.jpa.entity.mw;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_DC database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_DC", uniqueConstraints=@UniqueConstraint(columnNames={"PARTS_ID", "DC_ID"}))
@NamedQuery(name="MwmPartsDc.findAll", query="SELECT m FROM MwmPartsDc m")
public class MwmPartsDc extends MwmBaseJpaEntity  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_DC_ID")
	private long partsDcId;

	@Column(name="DC_ID")
	private Long dcId;

	@Column(name="DC_TYPE")
	private Integer dcType;

	@Column(name="PARTS_ID")
	private Long partsId;


	public long getPartsDcId() {
		return this.partsDcId;
	}

	public void setPartsDcId(long partsDcId) {
		this.partsDcId = partsDcId;
	}


	public Long getDcId() {
		return this.dcId;
	}

	public void setDcId(Long dcId) {
		this.dcId = dcId;
	}

	public Integer getDcType() {
		return this.dcType;
	}

	public void setDcType(Integer dcType) {
		this.dcType = dcType;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

}