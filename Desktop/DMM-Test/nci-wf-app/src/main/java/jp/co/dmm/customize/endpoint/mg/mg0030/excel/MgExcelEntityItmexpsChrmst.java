package jp.co.dmm.customize.endpoint.mg.mg0030.excel;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.dmm.customize.endpoint.mg.mg0000.MgExcelEntity;

@Entity
@Access(AccessType.FIELD)
public class MgExcelEntityItmexpsChrmst extends MgExcelEntity {

	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;

	/** 組織コード */
	@Column(name="ORGNZ_CD")
	public String orgnzCd;

	/** 費目コード（１） */
	@Column(name="ITMEXPS_CD1")
	public String itmexpsCd1;

	/** 費目コード（２） */
	@Column(name="ITMEXPS_CD2")
	public String itmexpsCd2;

	/** 仕訳コード */
	@Column(name="JRN_CD")
	public String jrnCd;

	/** 勘定科目コード */
	@Column(name="ACC_CD")
	public String accCd;

	/** 勘定科目補助コード */
	@Column(name="ACC_BRKDWN_CD")
	public String accBrkdwnCd;

	/** 管理科目コード */
	@Column(name="MNGACC_CD")
	public String mngaccCd;

	/** 管理科目補助コード */
	@Column(name="MNGACC_BRKDWN_CD")
	public String mngaccBrkdwnCd;

	/** 予算科目コード */
	@Column(name="BDGTACC_CD")
	public String bdgtaccCd;

	/** 資産区分 */
	@Column(name="ASST_TP")
	public String asstTp;

	/** 消費税コード */
	@Column(name="TAX_CD")
	public String taxCd;

	/** 伝票グループ（GL） */
	@Column(name="SLP_GRP_GL")
	public String slpGrpGl;

	/** 経費区分 */
	@Column(name="CST_TP")
	public String cstTp;

	@Column(name="TAX_SBJ_TP")
	/** 課税対象区分 */
	public String taxSbjTp;

	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
}
