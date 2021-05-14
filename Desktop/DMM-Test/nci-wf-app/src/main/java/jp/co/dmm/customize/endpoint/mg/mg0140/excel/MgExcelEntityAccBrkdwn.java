package jp.co.dmm.customize.endpoint.mg.mg0140.excel;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelPeriodEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityAccBrkdwn extends MgExcelPeriodEntity {

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;

	@Column(name="ACC_CD")
	public String accCd;

	@Column(name="ACC_BRKDWN_CD")
	public String accBrkdwnCd;

	@Column(name="SQNO")
	public Integer sqno;

	@Column(name="ACC_BRKDWN_NM")
	public String accBrkdwnNm;

	@Column(name="ACC_BRKDWN_NM_S")
	public String accBrkdwnNmS;

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
