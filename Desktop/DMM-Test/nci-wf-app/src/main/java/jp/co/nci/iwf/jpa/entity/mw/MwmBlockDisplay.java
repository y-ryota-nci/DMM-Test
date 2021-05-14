package jp.co.nci.iwf.jpa.entity.mw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the MWM_BLOCK_DISPLAY database table.
 *
 */
@Entity
@Table(name="MWM_BLOCK_DISPLAY", uniqueConstraints=@UniqueConstraint(columnNames={"DC_ID", "SCREEN_PROCESS_ID", "BLOCK_ID"}))
@NamedQuery(name="MwmBlockDisplay.findAll", query="SELECT m FROM MwmBlockDisplay m")
public class MwmBlockDisplay extends MwmBaseJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BLOCK_DISPLAY_ID")
	private long blockDisplayId;

	@Column(name="BLOCK_ID")
	private Integer blockId;

	@Column(name="DC_ID")
	private Long dcId;

	@Column(name="DISPLAY_FLAG")
	private String displayFlag;

	@Column(name="EXPANSION_FLAG")
	private String expansionFlag;

	@Column(name="SCREEN_PROCESS_ID")
	private Long screenProcessId;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;


	public long getBlockDisplayId() {
		return this.blockDisplayId;
	}

	public void setBlockDisplayId(long blockDisplayId) {
		this.blockDisplayId = blockDisplayId;
	}

	public Integer getBlockId() {
		return this.blockId;
	}

	public void setBlockId(Integer blockId) {
		this.blockId = blockId;
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
		return expansionFlag;
	}

	public void setExpansionFlag(String expansionFlag) {
		this.expansionFlag = expansionFlag;
	}

	public Long getScreenProcessId() {
		return this.screenProcessId;
	}

	public void setScreenProcessId(Long screenProcessId) {
		this.screenProcessId = screenProcessId;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}