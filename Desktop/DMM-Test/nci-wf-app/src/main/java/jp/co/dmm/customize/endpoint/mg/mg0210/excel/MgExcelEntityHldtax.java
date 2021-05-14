package jp.co.dmm.customize.endpoint.mg.mg0210.excel;

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
public class MgExcelEntityHldtax extends MgExcelEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;

	/** 源泉税区分 */
	@Column(name="HLDTAX_TP")
	public String hldtaxTp;

	/** 源泉税名称 */
	@Column(name="HLDTAX_NM")
	public String hldtaxNm;

	/** 源泉税率1 */
	@Column(name="HLDTAX_RTO1")
	public BigDecimal hldtaxRto1;

	/** 源泉税率2 */
	@Column(name="HLDTAX_RTO2")
	public BigDecimal hldtaxRto2;

	/** 勘定科目コード */
	@Column(name="ACC_CD")
	public String accCd;

	/** 勘定科目補助コード */
	@Column(name="ACC_BRKDWN_CD")
	public String accBrkdwnCd;

	/** 有効開始日付 */
	@Column(name="VD_DT_S")
	public Date vdDtS;

	/** 有効終了日付 */
	@Column(name="VD_DT_E")
	public Date vdDtE;

	/** ソート順 */
	@Column(name="SORT_ORDER")
	public String sortOrder;

	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;

	/** 有効開始日付 文字列 */
	public String strVdDtS;
	/** 有効終了日付 文字列 */
	public String strVdDtE;
}
