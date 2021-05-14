package jp.co.dmm.customize.endpoint.mg.mg0160.excel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelPeriodEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityTax extends MgExcelPeriodEntity {

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	@Column(name="TAX_CD")
	public String taxCd;
	/** 連番 */
	@Column(name="SQNO")
	public Integer sqno;
	@Column(name="TAX_NM")
	public String taxNm;
	@Column(name="TAX_NM_S")
	public String taxNmS;

	@Column(name="TAX_RTO")
	public BigDecimal taxRto;
	@Column(name="TAX_TP")
	public String taxTp;
	@Column(name="TAX_CD_SS")
	public String taxCdSs;

	@Column(name="FRC_UNT")
	public String frcUnt;
	@Column(name="FRC_TP")
	public String frcTp;

	@Column(name="ACC_CD")
	public String accCd;
	@Column(name="ACC_BRKDWN_CD")
	public String accBrkdwnCd;

	@Column(name="DC_TP")
	public String dcTp;

	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 削除フラグ名称 */
	@Column(name="DLT_FG_NM")
	public String dltFgNm;

	/** 有効開始日付 */
	@Column(name="VD_DT_S")
	public Date vdDtS;
	/** 有効終了日付 */
	@Column(name="VD_DT_E")
	public Date vdDtE;

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
