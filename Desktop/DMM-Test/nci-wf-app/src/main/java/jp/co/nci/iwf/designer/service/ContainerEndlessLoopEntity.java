package jp.co.nci.iwf.designer.service;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * コンテナ循環参照チェック用エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class ContainerEndlessLoopEntity extends BaseJpaEntity {
	@Id
	@Column(name="CONTAINER_ID")
	public long containerId;

	@Column(name="PARTS_ID")
	public long partsId;

	@Column(name="PARTS_CODE")
	public String partsCode;

	@Column(name="CHILD_CONTAINER_ID")
	public long childContainerId;
}
