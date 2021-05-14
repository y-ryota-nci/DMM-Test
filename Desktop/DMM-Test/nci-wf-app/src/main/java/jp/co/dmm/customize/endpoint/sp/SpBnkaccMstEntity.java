package jp.co.dmm.customize.endpoint.sp;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 銀行口座マスタEntity
 *
 */
@Entity
@Access(AccessType.FIELD)
public class SpBnkaccMstEntity extends BaseJpaEntity{
	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 口座コード */
	@Column(name="BNKACC_CD")
	public String bnkaccCd;
	/** 連番*/
	@Column(name="SQNO")
	public String sqno;
	/** 銀行口座名称 */
	@Column(name="BNKACC_NM")
	public String bnkaccNm;
}

