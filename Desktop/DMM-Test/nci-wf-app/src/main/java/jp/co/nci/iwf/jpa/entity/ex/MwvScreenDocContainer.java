package jp.co.nci.iwf.jpa.entity.ex;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class MwvScreenDocContainer  extends BaseJpaEntity {
	@Id
	@Column(name="ROW_NUM")
	public long rowNum;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="SCREEN_DOC_ID")
	public Long screenDocId;

	@Column(name="SCREEN_ID")
	public Long screenId;

	@Column(name="CONTAINER_ID")
	public Long containerId;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Column(name="CHILD_CONTAINER_ID")
	public Long childContainerId;

	@Column(name="NEST_LEVEL")
	public Integer nestedLevel;
}
