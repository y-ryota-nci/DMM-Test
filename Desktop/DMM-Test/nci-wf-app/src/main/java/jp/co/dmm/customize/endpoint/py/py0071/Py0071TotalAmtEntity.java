package jp.co.dmm.customize.endpoint.py.py0071;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class Py0071TotalAmtEntity extends BaseJpaEntity {

	/** 疑似ID(=ROWNUM) */
	@Id
	@Column(name="ID")
	public long id;

	/** 前月残高邦貨金額 */
	@Column(name="PRV_TOTAL_AMT_JPY")
	public BigDecimal prvTotalAmtJpy;
	/** 借方邦貨金額 */
	@Column(name="DBT_TOTAL_AMT_JPY")
	public BigDecimal dbtTotalAmtJpy;
	/** 貸方邦貨金額 */
	@Column(name="CDT_TOTAL_AMT_JPY")
	public BigDecimal cdtTotalAmtJpy;
	/** 当月残高邦貨金額 */
	@Column(name="NXT_TOTAL_AMT_JPY")
	public BigDecimal nxtTotalAmtJpy;
}
