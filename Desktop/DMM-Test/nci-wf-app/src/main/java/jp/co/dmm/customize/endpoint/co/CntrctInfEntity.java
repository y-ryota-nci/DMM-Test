package jp.co.dmm.customize.endpoint.co;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import jp.co.dmm.customize.jpa.entity.mw.CntrctInfPK;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 契約一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class CntrctInfEntity extends BaseJpaEntity {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CntrctInfPK id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 契約No */
	@Column(name="CNTRCT_NO")
	public String cntrctNo;
	/** 契約ステータス */
	@Column(name="CNTRCT_STS")
	public String cntrctSts;
	/** 契約件名 */
	@Column(name="CNTRCT_NM")
	public String cntrctNm;
	/** 契約者コード */
	@Column(name="CNTRCTR_CD")
	public String cntrctrCd;
	/** 契約者名*/
	@Column(name="CNTRCTR_NM")
	public String cntrctrNm;
	/** 契約部署コード */
	@Column(name="CNTRCTR_DPT_CD")
	public String cntrctrDptCd;
	/** 契約部署名称 */
	@Column(name="")
	public String cntrctrDptNm;
	/** 契約部署コード（組織付加コード） */
	@Column(name="CNTRCTR_DPT_ADDED_INFO")
	public String cntrctrDptAddedInfo;
	/** 契約期間（開始） */
	@Column(name="CNTRCT_PRD_S_DT")
	public Date cntrctPrdSDt;
	/** 契約期間（終了） */
	@Column(name="CNTRCT_PRD_E_DT")
	public Date cntrctPrdEDt;
	/** 契約形態 */
	@Column(name="CNTRCT_TP")
	public String cntrctTp;
	/** 依頼種別(契約書書式) */
	@Column(name="CNTRCTSHT_FRMT")
	public String cntrctshtFrmt;
	/** 依頼種別(契約書書式) */
	@Column(name="CNTRCTSHT_FRMT_NM")
	public String cntrctshtFrmtNm;
	/** 契約日 */
	@Column(name="CNTRCT_DT")
	public Date cntrctDt;
	/** 申請者コード */
	@Column(name="SBMTR_CD")
	public String sbmtrCd;
	/** 申請者名 */
	@Column(name="SBMTR_NM")
	public String sbmtrNm;
	/** 申請部署コード */
	@Column(name="SBMT_DPT_CD")
	public String sbmtDptCd;
	/** 申請部署名称 */
	@Column(name="SBMT_DPT_NM")
	public String sbmtDptNm;
	/** 申請日 */
	@Column(name="SBMT_DPT_DT")
	public Date sbmtDptDt;
	/** 所在地 */
	@Column(name="SBMTR_ADDR")
	public String sbmtrAddr;
	/** 経常支払区分 */
	@Column(name="RTN_PAY_TP")
	public String rtnPayTp;
	/** 経常支払No */
	@Column(name="RTN_PAY_NO")
	public BigDecimal rtnPayNo;
	/** 契約内容・条件 */
	@Column(name="CNTRCT_DTLCND")
	public String cntrctDtlcnd;
	/** 対価の発生有無 */
	@Column(name="CMPN_OCC_PRS")
	public String cmpnOccPrs;
	/** 取引想定金額（税抜） */
	@Column(name="TRD_EST_AMT_EXCTAX")
	public BigDecimal trdEstAmtExctax;
	/** 取引想定金額（コメント） */
	@Column(name="TRD_EST_AMT_RMK")
	public String trdEstAmtRmk;
	/** 支払サイクル（予定） */
	@Column(name="PAY_CYC_PLN_TP")
	public String payCycPlnTp;
	/** 支払サイクル（コメント） */
	@Column(name="PAY_CYC_PLN_RMK")
	public String payCycPlnRmk;
	/** 更新有無 */
	@Column(name="RNW_PRS")
	public String rnwPrs;
	/** 更新内容・条件 */
	@Column(name="RNW_DTLCND")
	public String rnwDtlcnd;
	/** 製本担当区分 */
	@Column(name="BKBND_CHG_TP")
	public String bkbndChgTp;
	/** 製本担当区分 */
	@Column(name="BKBND_CHG_TP_NM")
	public String bkbndChgTpNm;
	/** 印章指定区分 */
	@Column(name="STMP_APP_TP")
	public String stmpAppTp;
	/** 備考 */
	@Column(name="RMK")
	public String rmk;
	/** 法務担当者コード */
	@Column(name="LGLR_CD")
	public String lglrCd;
	/** 法務担当者名 */
	@Column(name="LGLR_NM")
	public String lglrNm;
	/** 契約書名称 */
	@Column(name="CNTRCT_SHT_NM")
	public String cntrctShtNm;
	/** 契約締結日 */
	@Column(name="CNTRCT_CNCL_DT")
	public Date cntrctCnclDt;
	/** 契約期間（開始） */
	@Column(name="CNTRCT_DT_S")
	public Date cntrctDtS;
	/** 契約期間（終了） */
	@Column(name="CNTRCT_DT_E")
	public Date cntrctDtE;
	/** 印紙有無 */
	@Column(name="RVNSTMP_PRS")
	public String rvnstmpPrs;
	/** 印紙額 */
	@Column(name="RVNSTMP_AMT")
	public BigDecimal rvnstmpAmt;

	/** 更新方法区分（自動更新） */
	@Column(name="RNW_MTH_TP_AUTO")
	public String rnwMthTpAuto;
	/** 更新方法区分（自動更新）(通知期間） */
	@Column(name="RNW_MTH_TP_AUTO_INF_MT")
	public BigDecimal rnwMthTpAutoInfMt;
	/** 更新方法区分（自動更新）(延長期間） */
	@Column(name="RNW_MTH_TP_AUTO_PSTP_MT")
	public BigDecimal rnwMthTpAutoPstpMt;
	/** 更新方法区分（協議更新） */
	@Column(name="RNW_MTH_TP_NEGO")
	public String rnwMthTpNego;
	/** 更新方法区分（協議更新）(通知期間） */
	@Column(name="RNW_MTH_TP_NEGO_INF_MT")
	public BigDecimal rnwMthTpNegoInfMt;
	/** 更新方法区分（期間満了） */
	@Column(name="RNW_MTH_TP_EXPR")
	public BigDecimal rnwMthTpExpr;
	/** 更新方法区分（無期限） */
	@Column(name="RNW_MTH_TP_INDF")
	public BigDecimal rnwMthTpIndf;
	/** 更新方法区分（更新なし） */
	@Column(name="RNW_MTH_TP")
	public BigDecimal rnwMthTp;
	/** 法務用備考 */
	@Column(name="LGL_RMK")
	public String lglRmk;
	/** 支払サイトコード */
	@Column(name="PAY_SITE_CD")
	public String paySiteCd;
	/** 通貨コード */
	@Column(name="MNY_CD")
	public String mnyCd;
	/** 通貨コード(名称) */
	@Column(name="MNY_NM")
	public String mnyNm;
	/** 競業避止義務区分 */
	@Column(name="N_CMPT_OBLG_TP")
	public String nCmptOblgTp;
	/** 決裁者宛コメント */
	@Column(name="TO_APPRV_RMK")
	public String toApprvRmk;
	/** 契約書依頼区分 */
	@Column(name="CNTRCTSHT_RQST_TP")
	public String cntrctshtRqstTp;
	/** 郵便要否*/
	@Column(name="MAIL_TP")
	public String mailTp;
	/** 郵便要否*/
	@Column(name="MAIL_TP_NM")
	public String mailTpNm;
	/** 郵便コメント*/
	@Column(name="MAIL_RMK")
	public String mailRmk;
	/** 送付先郵便番号 */
	@Column(name="SHT_TO_ZIP_CD")
	public String shtToZipCd;
	/** 送付先住所（都道府県） */
	@Column(name="SHT_ADR_PRF_CD")
	public String shtAdrPrfCd;
	/** 送付先住所（都道府県） */
	@Column(name="SHT_ADR_PRF")
	public String shtAdrPrf;
	/** 送付先住所（市区町村） */
	@Column(name="SHT_ADR1")
	public String shtAdr1;
	/** 送付先住所（町名／番地） */
	@Column(name="SHT_ADR2")
	public String shtAdr2;
	/** 送付先住所（建物名） */
	@Column(name="SHT_ADR3")
	public String shtAdr3;
	/** 削除フラグ*/
	@Column(name="DLT_FG")
	public String dltFg;

	/** 経常支払い *****************************/
	/** 経常支払区分 */
	@Column(name="PAYEE_RTN_PAY_TP")
	public String payeeRtnPayTp;
	/** 経常支払方法 */
	@Column(name="RTN_PAY_MTH")
	public String rtnPayMth;
	/** 振込先銀行口座コード */
	@Column(name="PAYEE_BNKACC_CD")
	public String payeeBnkaccCd;
	/** 銀行口座コード */
	@Column(name="BNKACC_CD")
	public String bnkaccCd;
	/** 銀行口座名 */
	@Column(name="BNKACC_NM")
	public String bnkaccNm;
	/** 引落銀行口座コード */
	@Column(name="CHRG_BNKACC_CD")
	public String chrgBnkaccCd;
	/** 引落銀行口座名 */
	@Column(name="CHRG_BNKACC_NM")
	public String chrgBnkaccNm;
	/** 支払開始年月 */
	@Column(name="PAY_START_TIME")
	public String payStartTime;
	/** 支払終了年月 */
	@Column(name="PAY_END_TIME")
	public String payEndTime;
	/** 経常支払金額（邦貨）*/
	@Column(name="RTN_PAY_AMT_JPY")
	public BigDecimal rtnPayAmtJpy;
	/** 支払サイトコード*/
	@Column(name="RTN_PAY_SITE_CD")
	public String rtnPaySiteCd;
	/** 支払サイト名称*/
	@Column(name="RTN_PAY_SITE_NM")
	public String rtnPaySiteNm;
	/** 通貨区分 */
	@Column(name="MNY_TP")
	public String mnyTp;
	/** 通貨コード(経常支払) */
	@Column(name="PAYEE_MNY_CD")
	public String payeeMnyCd;
	/** 通貨コード名称(経常支払) */
	@Column(name="PAYEE_MNY_NM")
	public String payeeMnyNm;
	/** 組織コード */
	@Column(name="ORGNZ_CD")
	public String orgnzCd;
	/** 組織名称 */
	@Column(name="ORGNZ_NM")
	public String orgnzNm;
	/** 計上レート */
	@Column(name="ADD_RTO")
	public BigDecimal addRto;
	/** 経常支払金額（外貨） */
	@Column(name="RTN_PAY_AMT_FC")
	public BigDecimal rtnPayAmtFc;
	/** 支払業務コード */
	@Column(name="PAY_APPL_CD")
	public String payApplCd;
	/** 支払業務名称 */
	@Column(name="PAY_APPL_NM")
	public String payApplNm;
	/** 消費税処理単位 */
	@Column(name="TAX_UNT")
	public String taxUnt;
	/** 消費税処理単位(名称) */
	@Column(name="TAX_UNT_NM")
	public String taxUntNm;
    /** 消費税コード */
	@Column(name="TAX_CD")
    public String TaxCd;
    /** 消費税名称 */
	@Column(name="TAX_NM")
    public String TaxNm;
    /** 税率 */
	@Column(name="TAX_RTO")
    public BigDecimal taxRto;
    /** 消費税端数処理単位 */
	@Column(name="FRC_UNT")
    public String frcUnt;
    /** 消費税端数処理区分 */
	@Column(name="FRC_TP")
    public String frcTp;
    /** 源泉税区分 */
	@Column(name="HLDTAX_TP")
    public String hldtaxTp;
    /** 源泉税区分(名称) */
	@Column(name="HLDTAX_NM")
    public String hldtaxNm;
    /** 源泉税率 */
	@Column(name="HLDTAX_RTO")
    public BigDecimal hldtaxRto;
    /** 源泉税対象額 */
	@Column(name="HLDTAX_SBJ_AMT")
    public BigDecimal hldtaxSbjAmt;
    /** 源泉徴収税額*/
	@Column(name="HLDTAX_AMT")
    public BigDecimal hldtaxAmt;
    /** 輸入消費税区分 */
	@Column(name="IMP_TAX_TP")
    public String impTaxTp;
	/** 振込先銀行コード */
	@Column(name="PAYEE_BNK_CD")
	public String payeeBnkCd;
	/** 振込先銀行名 */
	@Column(name="PAYEE_BNK_NM")
	public String payeeBnkNm;
	/** 振込先支店コード */
	@Column(name="PAYEE_BNKBRC_CD")
	public String payeeBnkbrcCd;
	/** 振込先支店名 */
	@Column(name="PAYEE_BNKBRC_NM")
	public String payeeBnkbrcNm;
	/** 振込先口座種別 */
	@Column(name="PAYEE_BNKACC_TP")
	public String payeeBnkaccTp;
	/** 振込先口座種別 */
	@Column(name="PAYEE_BNKACC_TP_NM")
	public String payeeBnkaccTpNm;
	/** 振込先口座番号 */
	@Column(name="PAYEE_BNKACC_NO")
	public String payeeBnkaccNo;
	/** 振込先口座名称*/
	@Column(name="PAYEE_BNKACC_NM")
	public String payeeBnkaccNm;
	/** 振込手数料負担区分 */
	@Column(name="PAY_CMM_OBL_TP")
	public String payCmmOblTp;
	/** 休日処理区分 */
	@Column(name="HLD_TRT_TP")
	public String hldTrtTp;

	/** 取引先コード */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 取引先名（漢字） */
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	/** 取引先名（カナ） */
	@Column(name="SPLR_NM_KN")
	public String splrNmKn;
	/** 契約先マスタ連番 */
	@Column(name="CNTRCT_SPLR_SQNO")
	public Long cntrctSplrSqno;

	/** 法人・個人区分 */
	@Column(name="CRP_PRS_TP")
	public String crpPrsTp;
	/** 国内・海外区分 */
	@Column(name="DMS_ABR_TP")
	public String dmsAbrTp;
	/** 前払区分 */
	@Column(name="ADVPAY_TP")
	public String advpayTp;
	/** 支払通知書フラグ */
	@Column(name="PAY_APPL_TP")
	public String payApplTp;
	/** 経常支払金額（税抜）*/
	@Column(name="RTN_PAY_AMT_EXCTAX")
	public BigDecimal rtnPayAmtExctax;
	/** 経常支払金額（税込）*/
	@Column(name="RTN_PAY_AMT_INCTAX")
	public BigDecimal rtnPayAmtInctax;

	/** 消費税フラグ */
	@Column(name="TAX_FG")
	public String taxFg;
	/** 消費税フラグ（名称） */
	@Column(name="TAX_FG_NM")
	public String taxFgNm;

	/** 契約期間（開始） */
	public String getCntrctDtSStr() {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
		return this.cntrctPrdSDt!= null ? sd.format(((java.util.Date) this.cntrctPrdSDt)) : null;
	}

	/** 契約期間（終了） */
	public String getCntrctDtEStr() {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
		return this.cntrctPrdEDt != null ? sd.format(((java.util.Date) this.cntrctPrdEDt)) : null;
	}

	/** 経常支払金額*/
	public BigDecimal getRtnPayAmt() {
		//金額計算
		if ("JPY".equals(this.mnyCd) || this.mnyCd == null) {
			return this.rtnPayAmtJpy;
		} else {
			return this.rtnPayAmtFc;
		}
	}
}
