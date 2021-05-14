package jp.co.dmm.customize.endpoint.mg.mg0010.excel;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelPeriodEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityItm extends MgExcelPeriodEntity {

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 組織コード */
	@Column(name="ORGNZ_CD")
	public String orgnzCd;
	/** 組織名称 */
	@Column(name="ORGNZ_NM")
	public String orgnzNm;
	/** 品目コード */
	@Column(name="ITM_CD")
	public String itmCd;
	/** 連番 */
	@Column(name="SQNO")
	public Integer sqno;
	/** 品目名称 */
	@Column(name="ITM_NM")
	public String itmNm;
	/** カテゴリコード */
	@Column(name="CTGRY_CD")
	public String ctgryCd;
	/** 在庫区分 */
	@Column(name="STCK_TP")
	public String stckTp;
	/** 在庫区分名称 */
	@Column(name="STCK_TP_NM")
	public String stckTpNm;
	/** 単位 */
	@Column(name="UNT_CD")
	public String untCd;
	/** 金額 */
	@Column(name="AMT")
	public String amt;
	/** メーカー名称 */
	@Column(name="MAKER_NM")
	public String makerNm;
	/** メーカー型式 */
	@Column(name="MAKER_MDL_NO")
	public String makerMdlNo;
	/** 品目備考 */
	@Column(name="ITM_RMK")
	public String itmRmk;
	/** 調達部門区分フラグ */
	@Column(name="PRC_FLD_TP")
	public String prcFldTp;
	/** 調達部門区分フラグ名称 */
	@Column(name="PRC_FLD_TP_NM")
	public String prcFldTpNm;
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

	@Column(name="SPLR_CD")
	public String splrCd;
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	@Column(name="SPLR_NM_KN")
	public String splrNmKn;
	@Column(name="TAX_CD")
	public String taxCd;
	@Column(name="ITM_VRSN")
	public String itmVrsn;
	@Column(name="TAX_NM")
	public String taxNm;

	@Id
	@Column(name="ID")
	public long id;

	/** 有効開始日付 文字列 */
	public String strVdDtS;
	/** 有効終了日付 文字列 */
	public String strVdDtE;
	/** 連番 文字列*/
	public String strSqno;
}
