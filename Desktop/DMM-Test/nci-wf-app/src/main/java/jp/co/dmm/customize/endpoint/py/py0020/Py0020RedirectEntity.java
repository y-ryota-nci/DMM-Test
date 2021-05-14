package jp.co.dmm.customize.endpoint.py.py0020;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Py0020RedirectEntity extends BaseJpaEntity  {

	/**  */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PAY_NO")
	public String payNo;
	@Column(name="SCREEN_CODE")
	public String screenCode;
	@Column(name="ADVPAY_FG")
	public String advpayFg;

}
