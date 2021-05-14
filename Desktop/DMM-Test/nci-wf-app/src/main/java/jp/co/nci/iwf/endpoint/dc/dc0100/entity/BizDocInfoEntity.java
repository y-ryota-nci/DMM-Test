package jp.co.nci.iwf.endpoint.dc.dc0100.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class BizDocInfoEntity extends BaseJpaEntity {

	@Id
	@Column(name="ID")
	public Long id;
	@Column(name="BIZ_DOC_ID")
	public Long bizDocId;
	@Column(name="DOC_ID")
	public Long docId;
	@Column(name="SCREEN_DOC_ID")
	public Long screenDocId;
	@Column(name="TRAN_ID")
	public Long tranId;
	@Column(name="CORPORATION_CODE")
	public String corporationCode;
	@Column(name="SCREEN_DOC_NAME")
	public String screenDocName;
	@Column(name="SCREEN_ID")
	public Long screenId;
	@Column(name="SCREEN_NAME")
	public String screenName;
	@Column(name="SCREEN_PROCESS_CODE")
	public String screenProcessCode;

}
