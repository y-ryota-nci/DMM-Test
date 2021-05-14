package jp.co.dmm.customize.endpoint.mg.mg0100.excel;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelPeriodEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityBnk extends MgExcelPeriodEntity {
	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 銀行コード */
	@Column(name="BNK_CD")
	public String bnkCd;
	/** 銀行名称 */
	@Column(name="BNK_NM")
	public String bnkNm;
	/** 銀行名カナ */
	@Column(name="BNK_NM_KN")
	public String bnkNmKn;
	/** 銀行略称 */
	@Column(name="BNK_NM_S")
	public String bnkNmS;
	/** 有効期限（開始） */
	@Column(name="VD_DT_S")
	public Date vdDtS;
	/** 有効期限（終了） */
	@Column(name="VD_DT_E")
	public Date vdDtE;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 削除フラグ名称 */
	@Column(name="DLT_FG_NM")
	public String dltFgNm;


	/** 有効開始日付 文字列 */
	public String strVdDtS;
	/** 有効終了日付 文字列 */
	public String strVdDtE;
}
