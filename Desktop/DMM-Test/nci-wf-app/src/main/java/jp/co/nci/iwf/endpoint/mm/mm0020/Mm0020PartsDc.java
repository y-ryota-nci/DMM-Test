package jp.co.nci.iwf.endpoint.mm.mm0020;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 表示条件設定でのパーツ表示条件
 */
@Entity
@Access(AccessType.FIELD)
public class Mm0020PartsDc extends BaseJpaEntity {

	@Column(name="DC_ID")
	private Long dcId;

	@Column(name="DC_TYPE")
	private Integer dcType;

	@Id
	@Column(name="PARTS_DC_ID")
	private long partsDcId;

	@Column(name="PARTS_ID")
	private Long partsId;

	public Long getDcId() {
		return dcId;
	}

	public void setDcId(Long dcId) {
		this.dcId = dcId;
	}

	public Integer getDcType() {
		return dcType;
	}

	public void setDcType(Integer dcType) {
		this.dcType = dcType;
	}

	public long getPartsDcId() {
		return partsDcId;
	}

	public void setPartsDcId(long partsDcId) {
		this.partsDcId = partsDcId;
	}

	public Long getPartsId() {
		return partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}
}
