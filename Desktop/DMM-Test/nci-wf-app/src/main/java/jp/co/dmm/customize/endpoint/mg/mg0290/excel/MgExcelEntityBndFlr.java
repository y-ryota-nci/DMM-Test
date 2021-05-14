package jp.co.dmm.customize.endpoint.mg.mg0290.excel;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityBndFlr extends MgExcelEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 結合フロアコード */
	@Column(name="BND_FLR_CD")
	public String bndFlrCd;
	/** 結合フロア名称 */
	@Column(name="BND_FLR_NM")
	public String bndFlrNm;
	/** ソート順 */
	@Column(name="SORT_ORDER")
	public String sortOrder;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 削除フラグ名称 */
	@Column(name="DLT_FG_NM")
	public String dltFgNm;

}
