package jp.co.dmm.customize.endpoint.mg.mg0000;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * コード値取得用エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class MgMstCode extends BaseJpaEntity {

	/** コード値 */
	@Id
	@Column(name="CODE_VALUE")
	public String codeValue;
}
