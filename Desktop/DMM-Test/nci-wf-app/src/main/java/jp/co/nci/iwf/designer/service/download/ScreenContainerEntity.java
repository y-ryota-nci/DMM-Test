package jp.co.nci.iwf.designer.service.download;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 画面が参照しているコンテナ
 */
@Entity
@Access(AccessType.FIELD)
public class ScreenContainerEntity extends BaseJpaEntity {

	@Id
	@Column(name="CONTAINER_ID")
	public long containerId;

	@Column(name="SCREEN_ID")
	public Long screenId;

	@Column(name="NEST_LEVEL")
	public Long nestLevel;

	@Column(name="SORT_ORDER")
	public Integer sortOrder;

	@Column(name="CHILD_CONTAINER_ID")
	public Long childContainerId;
}
