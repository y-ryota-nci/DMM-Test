package jp.co.nci.iwf.endpoint.vd.vd0060;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 選択肢を参照しているコンテナ
 */
@Entity(name="MWM_CONTAINER")
@Access(AccessType.FIELD)
public class Vd0060Container extends BaseJpaEntity {
	@Id
	@Column(name="ID")
	public long id;

	@Column(name="CONTAINER_ID")
	public long containerId;

	@Column(name="CONTAINER_CODE")
	public String containerCode;

	@Column(name="CONTAINER_NAME")
	public String containerName;

	@Column(name="OPTION_ID")
	public long optionId;
}
