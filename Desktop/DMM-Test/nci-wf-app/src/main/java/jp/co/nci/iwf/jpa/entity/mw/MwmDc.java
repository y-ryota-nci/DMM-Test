package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_DC database table.
 *
 */
@Entity
@Table(name="MWM_DC", uniqueConstraints=@UniqueConstraint(columnNames={"DC_ID"}))
@NamedQuery(name="MwmDc.findAll", query="SELECT m FROM MwmDc m")
public class MwmDc extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DC_ID")
	private long dcId;

	@Column(name="DC_NAME")
	private String dcName;

	@Column(name="SORT_ORDER")
	private Long sortOrder;

	public MwmDc() {
	}

	public long getDcId() {
		return this.dcId;
	}

	public void setDcId(long dcId) {
		this.dcId = dcId;
	}

	public String getDcName() {
		return this.dcName;
	}

	public void setDcName(String dcName) {
		this.dcName = dcName;
	}

	public Long getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}

}