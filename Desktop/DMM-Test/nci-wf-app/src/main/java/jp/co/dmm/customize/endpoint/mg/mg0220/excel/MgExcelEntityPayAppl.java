package jp.co.dmm.customize.endpoint.mg.mg0220.excel;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityPayAppl extends MgExcelEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 支払業務コード */
	@Column(name="PAY_APPL_CD")
	public String payApplCd;
	/** 支払業務名称 */
	@Column(name="PAY_APPL_NM")
	public String payApplNm;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 削除フラグ名称 */
	@Column(name="DLT_FG_NM")
	public String dltFgNm;

}
