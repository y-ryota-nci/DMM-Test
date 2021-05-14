package jp.co.nci.iwf.endpoint.dc.dc0100.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class BinderInfoEntity extends BaseJpaEntity {

	@Id
	@Column(name="BINDER_ID")
	public Long binderId;
	@Column(name="DOC_ID")
	public Long docId;

}
