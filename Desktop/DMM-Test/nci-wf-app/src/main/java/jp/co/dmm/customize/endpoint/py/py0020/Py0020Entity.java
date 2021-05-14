package jp.co.dmm.customize.endpoint.py.py0020;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Py0020Entity extends BaseJpaEntity  {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PROCESS_ID")
	public Long processId;

	@Column(name="SUBJECT")
	public String subject;
	@Column(name="APPLICATION_NO")
	public String applicationNo;

}
