package jp.co.nci.iwf.endpoint.mm.mm0060;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * ブロック表示順設定でのブロック表示条件マスタ
 */
@Entity
public class Mm0060BlockDisplay extends BaseJpaEntity {

	/**  */
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

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public long getBlockDisplayId() {
		return blockDisplayId;
	}
	public void setBlockDisplayId(long blockDisplayId) {
		this.blockDisplayId = blockDisplayId;
	}

	public Integer getBlockId() {
		return blockId;
	}
	public void setBlockId(Integer blockId) {
		this.blockId = blockId;
	}

	public Long getDcId() {
		return dcId;
	}
	public void setDcId(Long dcId) {
		this.dcId = dcId;
	}

	public String getDisplayFlag() {
		return displayFlag;
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

	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}
