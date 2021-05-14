package jp.co.dmm.customize.endpoint.mg.mg0170.excel;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityMny extends MgExcelEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 通貨コード */
	@Column(name="MNY_CD")
	public String mnyCd;
	/** 通貨名称 */
	@Column(name="MNY_NM")
	public String mnyNm;
	/** 通貨記号 */
	@Column(name="MNY_MRK")
	public String mnyMrk;
	/** 小数点桁数 */
	@Column(name="RDXPNT_GDT")
	public String rdxpntGdt;
	/** ソート順 */
	@Column(name="SORT_ORDER")
	public String sortOrder;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;

}
