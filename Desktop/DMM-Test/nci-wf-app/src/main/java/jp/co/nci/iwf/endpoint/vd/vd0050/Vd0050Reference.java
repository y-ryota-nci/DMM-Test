package jp.co.nci.iwf.endpoint.vd.vd0050;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Vd0050Reference extends BaseJpaEntity implements Serializable {
	@Id
	@Column(name="ID")
	public long id;

	@Column(name="FILE_NAME")
	public String fileName;

	@Column(name="SCREEN_CODE")
	public String screenCode;

	@Column(name="SCREEN_NAME")
	public String screenName;

	@Column(name="CONTAINER_CODE")
	public String containerCode;

	@Column(name="CONTAINER_NAME")
	public String containerName;
}
