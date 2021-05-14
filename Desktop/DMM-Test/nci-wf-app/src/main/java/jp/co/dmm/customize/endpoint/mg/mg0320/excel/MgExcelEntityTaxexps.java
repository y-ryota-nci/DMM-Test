package jp.co.dmm.customize.endpoint.mg.mg0320.excel;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityTaxexps extends MgExcelEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;

	/** 消費税種類コード */
	@Column(name="TAX_KND_CD")
	public String taxKndCd;
	/** 消費税種別 */
	@Column(name="TAX_SPC")
	public String taxSpc;

	/** 消費税コード */
	@Column(name="TAX_CD")
	public String taxCd;

	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 削除フラグ名称 */
	@Column(name="DLT_FG_NM")
	public String dltFgNm;

}
