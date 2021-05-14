package jp.co.dmm.customize.endpoint.mg.mg0270.excel;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityBgtItm extends MgExcelEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 予算科目コード */
	@Column(name="BGT_ITM_CD")
	public String bgtItmCd;
	/** 予算科目名称 */
	@Column(name="BGT_ITM_NM")
	public String bgtItmNm;
	/** BS/PL区分 */
	@Column(name="BS_PL_TP")
	public String bsPlTp;
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
