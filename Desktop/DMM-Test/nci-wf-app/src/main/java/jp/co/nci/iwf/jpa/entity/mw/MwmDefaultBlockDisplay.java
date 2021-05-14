package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_DEFAULT_BLOCK_DISPLAY database table.
 *
 */
@Entity
@Table(name="MWM_DEFAULT_BLOCK_DISPLAY", uniqueConstraints=@UniqueConstraint(columnNames={"DC_ID", "CORPORATION_CODE", "BLOCK_ID", "DC_ID"}))
@NamedQuery(name="MwmDefaultBlockDisplay.findAll", query="SELECT m FROM MwmDefaultBlockDisplay m")
public class MwmDefaultBlockDisplay extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DEFAULT_BLOCK_DISPLAY_ID")
	private long defaultBlockDisplayId;

	@Column(name="BLOCK_ID")
	private Integer blockId;

	@Column(name="CORPORATION_CODE")
	private String corporationCode;

	@Column(name="DC_ID")
	private Long dcId;

	@Column(name="DISPLAY_FLAG")
	private String displayFlag;

	@Column(name="EXPANSION_FLAG")
	private String expansionFlag;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public MwmDefaultBlockDisplay() {
	}

	public long getDefaultBlockDisplayId() {
		return this.defaultBlockDisplayId;
	}

	public void setDefaultBlockDisplayId(long defaultBlockDisplayId) {
		this.defaultBlockDisplayId = defaultBlockDisplayId;
	}

	public Integer getBlockId() {
		return this.blockId;
	}

	public void setBlockId(Integer blockId) {
		this.blockId = blockId;
	}

	public String getCorporationCode() {
		return this.corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	public Long getDcId() {
		return this.dcId;
	}

	public void setDcId(Long dcId) {
		this.dcId = dcId;
	}

	public String getDisplayFlag() {
		return this.displayFlag;
	}

	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}

	public String getExpansionFlag() {
		return this.expansionFlag;
	}

	public void setExpansionFlag(String expansionFlag) {
		this.expansionFlag = expansionFlag;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}