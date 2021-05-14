package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_PARTS_OPTION database table.
 *
 */
@Entity
@Table(name="MWM_PARTS_OPTION", uniqueConstraints=@UniqueConstraint(columnNames={"PARTS_ID"}))
@NamedQuery(name="MwmPartsOption.findAll", query="SELECT m FROM MwmPartsOption m")
public class MwmPartsOption extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARTS_OPTION_ID")
	private long partsOptionId;

	@Column(name="OPTION_ID")
	private Long optionId;

	@Column(name="PARTS_ID")
	private Long partsId;

	public MwmPartsOption() {
	}

	public long getPartsOptionId() {
		return this.partsOptionId;
	}

	public void setPartsOptionId(long partsOptionId) {
		this.partsOptionId = partsOptionId;
	}

	public Long getOptionId() {
		return this.optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public Long getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Long partsId) {
		this.partsId = partsId;
	}

}