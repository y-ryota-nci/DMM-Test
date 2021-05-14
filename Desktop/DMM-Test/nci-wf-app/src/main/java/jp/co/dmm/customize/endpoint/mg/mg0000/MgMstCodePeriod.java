package jp.co.dmm.customize.endpoint.mg.mg0000;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

@Entity
@Access(AccessType.FIELD)
public class MgMstCodePeriod extends BaseJpaEntity {

	/** コード値 */
	@Id
	@Column(name="CODE_VALUE")
	public String codeValue;

	/** 有効開始日付 */
	@Column(name="VD_DT_S")
	public Date vdDtS;

	/** 有効終了日付 */
	@Column(name="VD_DT_E")
	public Date vdDtE;

	@Column(name="SQNO")
	public int sqno;

}
