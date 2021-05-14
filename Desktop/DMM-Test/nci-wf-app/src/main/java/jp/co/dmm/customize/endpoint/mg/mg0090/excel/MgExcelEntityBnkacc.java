package jp.co.dmm.customize.endpoint.mg.mg0090.excel;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelPeriodEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityBnkacc extends MgExcelPeriodEntity {

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
	public Integer sqno;
	/** 銀行コード */
	@Column(name="BNK_CD")
	public String bnkCd;
	/** 銀行名称 */
	@Column(name="BNK_NM")
	public String bnkNm;
	/** 銀行支店コード */
	@Column(name="BNKBRC_CD")
	public String bnkbrcCd;
	/** 銀行支店名 */
	@Column(name="BNKBRC_NM")
	public String bnkbrcNm;
	/** 銀行口座種別 */
	@Column(name="BNKACC_TP")
	public String bnkaccTp;
	/** 銀行口座種別名称 */
	@Column(name="BNKACC_TP_NM")
	public String bnkaccTpNm;
	/** 銀行口座番号 */
	@Column(name="BNKACC_NO")
	public String bnkaccNo;
	/** 銀行口座名称 */
	@Column(name="BNKACC_NM")
	public String bnkaccNm;
	/** 銀行口座名称（カタカナ） */
	@Column(name="BNKACC_NM_KN")
	public String bnkaccNmKn;
	/** 有効開始日付 */
	@Column(name="VD_DT_S")
	public Date vdDtS;
	/** 有効終了日付 */
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
	/** 連番 文字列*/
	public String strSqno;
}
