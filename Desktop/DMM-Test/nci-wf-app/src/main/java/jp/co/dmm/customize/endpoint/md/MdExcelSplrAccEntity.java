package jp.co.dmm.customize.endpoint.md;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.dmm.customize.jpa.entity.mw.PayeeBnkaccMstPK;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 取引先マスタインポート用口座明細エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class MdExcelSplrAccEntity extends BaseJpaEntity {

	@EmbeddedId
	private PayeeBnkaccMstPK id;

	/** 処理区分 */
	@Column(name="PROCESS_TYPE")
	public String processType;

	/** NO */
	@Column(name="NO")
	BigDecimal no;
	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 振込先銀行口座コード */
	@Column(name="PAYEE_BNKACC_CD")
	public String payeeBnkaccCd;
	/** 仕入先社員区分 */
	@Column(name="BUYEE_STF_TP")
	public String buyeeStfTp;
	/** 取引先コード */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 銀行コード */
	@Column(name="BNK_CD")
	public String bnkCd;
	/** 銀行名称 */
	@Column(name="BNK_NM")
	public String bnkNm;
	/** 銀行支店コード */
	@Column(name="BNKBRC_CD")
	public String bnkbrcCd;
	/** 銀行支店コード */
	@Column(name="BNKBRC_NM")
	public String bnkbrcNm;
	/** 銀行口座種別 */
	@Column(name="BNKACC_TP")
	public String bnkaccTp;
	/** 銀行口座種別 （名称）*/
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
	/** 振込手数料負担区分 */
	@Column(name="PAY_CMM_OBL_TP")
	public String payCmmOblTp;
	/** 振込手数料負担区分 （名称） */
	@Column(name="PAY_CMM_OBL_NM")
	public String payCmmOblNm;
	/** 休日処理区分 */
	@Column(name="HLD_TRT_TP")
	public String hldTrtTp;
	/** 休日処理区分 （名称） */
	@Column(name="HLD_TRT_NM")
	public String hldTrtNm;
	/** 振込元銀行口座コード */
	@Column(name="SRC_BNKACC_CD")
	public String srcBnkaccCd;
	/** 振込先銀行口座名称 */
	@Column(name="SCR_BNKACC_NM")
	public String srcBnkaccNm;
	/** 有効開始日付 */
	@Temporal(TemporalType.DATE)
	@Column(name="VD_DT_S")
	public Date vdDtS;
	/** 有効終了日付 */
	@Temporal(TemporalType.DATE)
	@Column(name="VD_DT_E")
	public Date vdDtE;
	/** 備考 */
	@Column(name="RMK")
	public String rmk;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 振込先銀行口座コード(SuperStream) */
	@Column(name="PAYEE_BNKACC_CD_SS")
	public String payeeBnkaccCdSs;

	/** エラー */
	public String errorText;

	/** 有効開始日付 文字列 */
	public String strVdDtS;
	/** 有効終了日付 文字列 */
	public String strVdDtE;
}
