package jp.co.dmm.customize.endpoint.co;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import jp.co.dmm.customize.jpa.entity.mw.RtnPayMstPK;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 経常支払マスタ一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class RtnPayMstEntity extends BaseJpaEntity {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtnPayMstPK id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 経常支払No */
	@Column(name="RTN_PAY_NO")
	public BigDecimal rtnPayNo;
	/** 契約No */
	@Column(name="CNTRCT_NO")
	public String cntrctNo;
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
	/** 取引先コード */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 取引先名（漢字） */
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	/** 取引先名（カナ） */
	@Column(name="SPLR_NM_KN")
	public String splrNmKn;
	/** 法人・個人区分 */
	@Column(name="CRP_PRS_TP")
	public String crpPrsTp;
	/** 国内・海外区分 */
	@Column(name="DMS_ABR_TP")
	public String dmsAbrTp;
	/** 経常支払区分 */
	@Column(name="RTN_PAY_TP")
	public String rtnPayTp;
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
	/** 経常支払金額（税抜）*/
	@Column(name="RTN_PAY_AMT_EXCTAX")
	public BigDecimal rtnPayAmtExctax;
	/** 経常支払金額（税込）*/
	@Column(name="RTN_PAY_AMT_INCTAX")
	public BigDecimal rtnPayAmtInctax;
	/** 支払サイトコード*/
	@Column(name="PAY_SITE_CD")
	public String rtnPaySiteCd;
	/** 支払サイト名称*/
	@Column(name="RTN_PAY_SITE_NM")
	public String rtnPaySiteNm;
	/** 通貨区分 */
	@Column(name="MNY_TP")
	public String mnyTp;
	/** 通貨コード(経常支払) */
	@Column(name="MNY_CD")
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
	/** 前払区分 */
	@Column(name="ADVPAY_TP")
	public String advpayTp;
	/** 支払通知書フラグ */
	@Column(name="PAY_APPL_TP")
	public String payApplTp;
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
    public String taxCd;
    /** 消費税名称 */
	@Column(name="TAX_NM")
    public String taxNm;
	/** 消費税名称 */
	@Column(name="TAX_FG")
    public String taxFg;
	/** 消費税フラグ（名称） */
	@Column(name="TAX_FG_NM")
	public String taxFgNm;
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
    /** 削除フラグ*/
	@Column(name="DLT_FG")
	public String dltFg;
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
	/** 契約件名 */
	@Column(name="CNTRCT_NM")
	public String cntrctNm;
	/** 契約主体部署コード */
	@Column(name="CNTRCTR_DPT_CD")
	public String cntrctrDptCd;
	/** 契約主体部署名称 */
	@Column(name="CNTRCTR_DPT_NM")
	public String cntrctrDptNm;
	/** 部門コード */
	@Column(name="BUMON_CD")
	public String bumonCd;
	/** 部門名称 */
	@Column(name="BUMON_NM")
	public String bumonNm;
	/** 申請日 */
	@Column(name="SBMT_DPT_DT")
	public Date sbmtDptDt;

	/** 支払金額*/
	public BigDecimal getRtnPayAmt() {
		//金額計算
		if ("1".equals(this.mnyTp) || this.mnyTp == null) {
			return rtnPayAmtJpy;
		} else {
			return rtnPayAmtFc;
		}
	}
}
