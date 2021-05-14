package jp.co.dmm.customize.endpoint.mg.mg0180.excel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityInRto extends MgExcelEntity {

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
	/** 連番*/
	@Column(name="SQNO")
	public Integer sqno;
	/** 社内レート */
	@Column(name="IN_RTO")
	public BigDecimal inRto;
	/** レートタイプ */
	@Column(name="RTO_TP")
	public String rtoTp;
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
	/** 社内レート 文字列*/
	public String strInRto;
}
