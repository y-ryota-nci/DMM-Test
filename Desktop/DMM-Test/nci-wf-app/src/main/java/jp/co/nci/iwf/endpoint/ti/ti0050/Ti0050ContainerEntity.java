package jp.co.nci.iwf.endpoint.ti.ti0050;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Ti0050ContainerEntity extends BaseJpaEntity {

	@Id
	@Column(name="ID")
	public long id;

	@Column(name="CONTAINER_ID")
	public Long containerId;

	@Column(name="TABLE_SEARCH_ID")
	public long tableSearchId;

	@Column(name="CORPORATION_CODE")
	public String corporationCode;

	@Column(name="CONTAINER_CODE")
	public String containerCode;

	@Column(name="CONTAINER_NAME")
	public String containerName;

	@Column(name="LOCALE_CODE")
	public String localeCode;
}
