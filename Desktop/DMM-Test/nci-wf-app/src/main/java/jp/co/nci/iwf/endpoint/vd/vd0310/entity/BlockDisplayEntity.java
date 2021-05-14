package jp.co.nci.iwf.endpoint.vd.vd0310.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class BlockDisplayEntity extends BaseJpaEntity {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	public long id;
	@Column(name="BLOCK_DISPLAY_ID")
	public Long blockDisplayId;
	@Column(name="BLOCK_ID")
	public Integer blockId;
	@Column(name="BLOCK_NAME")
	public String blockName;
	@Column(name="DISPLAY_FLAG")
	public String displayFlag;
	@Column(name="EXPANSION_FLAG")
	public String expansionFlag;

}
