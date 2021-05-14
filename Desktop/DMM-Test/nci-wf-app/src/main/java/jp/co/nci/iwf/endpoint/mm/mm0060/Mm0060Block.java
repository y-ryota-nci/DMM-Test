package jp.co.nci.iwf.endpoint.mm.mm0060;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * ブロック表示順設定でのブロックエンティティ
 */
@Entity
public class Mm0060Block extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BLOCK_ID")
	private Integer blockId;

	@Column(name="BLOCK_NAME")
	private String blockName;

	@Column(name="SORT_ORDER")
	private Integer sortOrder;

	public Integer getBlockId() {
		return blockId;
	}
	public void setBlockId(Integer blockId) {
		this.blockId = blockId;
	}

	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}
