package jp.co.dmm.customize.endpoint.ss.ss0010.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * SS-AP_送信情報(支払明細)
 */
@Entity
@Access(AccessType.FIELD)
public class Ss0010SsapSndInfPd extends BaseJpaEntity {

	/** 疑似ID(=ROWNUM) */
	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** SSAP送信No */
	@Column(name="SSAP_SND_NO")
	public String ssapSndNo;
	/** SSAP送信明細No. */
	@Column(name="SSAP_SND_DTL_NO")
	public Long ssapSndDtlNo;
	/** レコード区分 */
	@Column(name="RCRD_TP")
	public String rcrdTp;
	/** 会社コード */
	@Column(name="SS_COMPANY_CD")
	public String ssCompanyCd;
	/** 伝票グループ */
	@Column(name="SLP_GRP")
	public String slpGrp;
	/** 伝票番号 */
	@Column(name="SLP_NO")
	public String slpNo;
	/** 伝票日付 */
	@Column(name="SLP_DT")
	public Date slpDt;
	/** 支払行番号 */
	@Column(name="PAY_LNNO")
	public String payLnno;
	/** 支払方法コード */
	@Column(name="PAY_MTH_CD")
	public String payMthCd;
	/** 期日指定予定日 */
	@Column(name="DT_APP_PLN_DT")
	public Date dtAppPlnDt;
	/** 支払予定日 */
	@Column(name="PAY_PLN_DT")
	public Date payPlnDt;
	/** 支払金額 */
	@Column(name="PAY_AMT")
	public BigDecimal payAmt;
	/** 現金手渡部門コード */
	@Column(name="CSHDLV_DPT_CD")
	public String cshdlvDptCd;
	/** 振込元口座管理コード */
	@Column(name="PAYER_BNKACC_CD")
	public String payerBnkaccCd;
	/** 振込情報コード */
	@Column(name="TRSFND_INF_CD")
	public String trsfndInfCd;
	/** 振出年月日 */
	@Column(name="ISS_DT")
	public Date issDt;
	/** 手形満期日 */
	@Column(name="BLL_TRM_DT")
	public Date bllTrmDt;
	/** 手形手渡部門コード */
	@Column(name="BLLDLV_DPT_CD")
	public String blldlvDptCd;
	/** 預り金有無フラグ */
	@Column(name="DPST_PRS_FG")
	public String dpstPrsFg;
	/** 預り金科目コード */
	@Column(name="DPST_ACC_CD")
	public String dpstAccCd;
	/** 預り金補助科目コード */
	@Column(name="DPST_ACC_BRKDWN_CD")
	public String dpstAccBrkdwnCd;
	/** 預り金部門コード */
	@Column(name="DPST_DPT_CD")
	public String dpstDptCd;
	/** 預り金機能コード1 */
	@Column(name="DPST_FNC_CD1")
	public String dpstFncCd1;
	/** 預り金機能コード2 */
	@Column(name="DPST_FNC_CD2")
	public String dpstFncCd2;
	/** 預り金機能コード3 */
	@Column(name="DPST_FNC_CD3")
	public String dpstFncCd3;
	/** 預り金機能コード4 */
	@Column(name="DPST_FNC_CD4")
	public String dpstFncCd4;
	/** 預り金プロジェクトコード */
	@Column(name="DPST_PRJ_CD")
	public String dpstPrjCd;
	/** 預り金取引先・社員区分 */
	@Column(name="DPST_SPLR_PRS_TP")
	public String dpstSplrPrsTp;
	/** 預り金取引先コード */
	@Column(name="DPST_SPLR_CD")
	public String dpstSplrCd;
	/** 預り金税抜金額 */
	@Column(name="DPST_AMT_EXCTAX")
	public BigDecimal dpstAmtExctax;
	/** 預り金税額 */
	@Column(name="DPST_TAX_AMT")
	public BigDecimal dpstTaxAmt;
	/** 預り金税処理コード */
	@Column(name="DPST_TAX_CD")
	public String dpstTaxCd;
	/** 預り金税入力区分 */
	@Column(name="DPST_TAX_IPT_TP")
	public String dpstTaxIptTp;
	/** 預り金摘要1 */
	@Column(name="DPST_SMRY1")
	public String dpstSmry1;
	/** 預り金摘要2 */
	@Column(name="DPST_SMRY2")
	public String dpstSmry2;
	/** 預り金対象金額 */
	@Column(name="DPST_AMT")
	public BigDecimal dpstAmt;
	/** 期日振替科目コード */
	@Column(name="DT_TRS_ACC_CD")
	public String dtTrsAccCd;
	/** 期日振替補助科目コード */
	@Column(name="DT_TRS_ACC_BRKDWN_CD")
	public String dtTrsAccBrkdwnCd;
	/** 期日振替部門コード */
	@Column(name="DT_TRS_DPT_CD")
	public String dtTrsDptCd;
	/** 期日振替機能コード１ */
	@Column(name="DT_TRS_FNC_CD1")
	public String dtTrsFncCd1;
	/** 期日振替機能コード２ */
	@Column(name="DT_TRS_FNC_CD2")
	public String dtTrsFncCd2;
	/** 期日振替機能コード３ */
	@Column(name="DT_TRS_FNC_CD3")
	public String dtTrsFncCd3;
	/** 期日振替機能コード４ */
	@Column(name="DT_TRS_FNC_CD4")
	public String dtTrsFncCd4;
	/** 期日振替プロジェクトコード */
	@Column(name="DT_TRS_PRJ_CD")
	public String dtTrsPrjCd;
	/** 期日振替摘要1 */
	@Column(name="DT_TRS_SMRY1")
	public String dtTrsSmry1;
	/** 期日振替摘要2 */
	@Column(name="DT_TRS_SMRY2")
	public String dtTrsSmry2;
	/** 外貨金額 */
	@Column(name="AMT_FC")
	public BigDecimal amtFc;
	/** 予備文字項目1 */
	@Column(name="SPR_CHR_ITM1")
	public String sprChrItm1;
	/** 予備文字項目2 */
	@Column(name="SPR_CHR_ITM2")
	public String sprChrItm2;
	/** 予備文字項目3 */
	@Column(name="SPR_CHR_ITM3")
	public String sprChrItm3;
	/** 予備文字項目4 */
	@Column(name="SPR_CHR_ITM4")
	public String sprChrItm4;
	/** 予備文字項目5 */
	@Column(name="SPR_CHR_ITM5")
	public String sprChrItm5;
	/** 予備文字項目6 */
	@Column(name="SPR_CHR_ITM6")
	public String sprChrItm6;
	/** 予備文字項目7 */
	@Column(name="SPR_CHR_ITM7")
	public String sprChrItm7;
	/** 予備文字項目8 */
	@Column(name="SPR_CHR_ITM8")
	public String sprChrItm8;
	/** 予備数字項目1 */
	@Column(name="SPR_NMR_ITM1")
	public BigDecimal sprNmrItm1;
	/** 予備数字項目2 */
	@Column(name="SPR_NMR_ITM2")
	public BigDecimal sprNmrItm2;
	/** 予備数字項目3 */
	@Column(name="SPR_NMR_ITM3")
	public BigDecimal sprNmrItm3;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 作成会社コード */
	@Column(name="CORPORATION_CODE_CREATED")
	public String corporationCodeCreated;
	/** 作成ユーザコード */
	@Column(name="USER_CODE_CREATED")
	public String userCodeCreated;
	/** 作成ユーザIPアドレス */
	@Column(name="IP_CREATED")
	public String ipCreated;
	/** 作成日時 */
	@Column(name="TIMESTAMP_CREATED")
	public Timestamp timestampCreated;
	/** 更新会社コード */
	@Column(name="CORPORATION_CODE_UPDATED")
	public String corporationCodeUpdated;
	/** 更新ユーザコード */
	@Column(name="USER_CODE_UPDATED")
	public String userCodeUpdated;
	/** 更新ユーザIPアドレス */
	@Column(name="IP_UPDATED")
	public String ipUpdated;
	/** 更新日時 */
	@Column(name="TIMESTAMP_UPDATED")
	public Timestamp timestampUpdated;

}
