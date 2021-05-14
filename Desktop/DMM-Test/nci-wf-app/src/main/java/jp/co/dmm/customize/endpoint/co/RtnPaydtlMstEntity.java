package jp.co.dmm.customize.endpoint.co;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import jp.co.dmm.customize.jpa.entity.mw.RtnPaydtlMstPK;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 管理_契約登録（経常支払明細）画面エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class RtnPaydtlMstEntity extends BaseJpaEntity {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RtnPaydtlMstPK id;

	/** No */
	@Column(name="RTN_PAY_DTL_DISP_NO")
	public Long rtnPayDtlDispNo;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 経常支払No */
	@Column(name="RTN_PAY_NO")
	public Long rtnPayNo;
	/** 経常支払明細No */
	@Column(name="RTN_PAY_DTL_NO")
	public long rtnPayDtlNo;
	/** 仕訳伝票No */
	@Column(name="JRNSLP_NO")
	public String jrnslpNo;
	/** 仕訳伝票明細No */
	@Column(name="JRNSLP_DTL_NO")
	public BigDecimal jrnslpDtlNo;
	/** 組織コード */
	@Column(name="ORGNZ_CD")
	public String orgnzCd;
	/** COM変換組織コード */
	@Column(name="COM_CHG_ORGNZ_CD")
	public String comChgOrgnzCd;
	/** 費目コード（１） */
	@Column(name="ITMEXPS_CD1")
	public String itmexpsCd1;
	/** 費目名称（１） */
	@Column(name="ITMEXPS_NM1")
	public String itmexpsNm1;
	/** 費目コード（２） */
	@Column(name="ITMEXPS_CD2")
	public String itmexpsCd2;
	/** 費目名称（２） */
	@Column(name="ITMEXPS_NM2")
	public String itmexpsNm2;
	/** 品目コード */
	@Column(name="ITM_CD")
	public String itmCd;
	/** 品目名称 */
	@Column(name="ITM_NM")
	public String itmNm;
	/** 部門コード */
	@Column(name="BUMON_CD")
	public String bumonCd;
	/** 部門名 */
	@Column(name="BUMON_NM")
	public String bumonNm;
	/** 分析コード */
	@Column(name="ANLYS_CD")
	public String anlysCd;
	/** 資産区分 */
	@Column(name="ASST_TP")
	public String asstTp;
	/** 資産区分（名称） */
	@Column(name="ASST_TP_NM")
	public String asstTpNm;
	/** 支払金額（邦貨） */
	@Column(name="PAY_AMT_JPY")
	public BigDecimal payAmtJpy;
	/** 通貨コード */
	@Column(name="MNY_CD")
	public String mnyCd;
	/** 通貨名称 */
	@Column(name="MNY_NM")
	public String mnyNm;
	/** 計上レート */
	@Column(name="ADD_RTO")
	public BigDecimal addRto;
	/** 支払金額（外貨） */
	@Column(name="PAY_AMT_FC")
	public BigDecimal payAmtFc;
	/** 消費税フラグ */
	@Column(name="TAX_FG")
	public String taxFg;
	/** 消費税フラグ（名称） */
	@Column(name="TAX_FG_NM")
	public String taxFgNm;
	/** 消費税コード */
	@Column(name="TAX_CD")
	public String taxCd;
	/** 消費税名称 */
	@Column(name="TAX_NM")
	public String taxNm;
	/** 支払概要 */
	@Column(name="PAY_SMRY")
	public String paySmry;
	/** 業務内容 */
	@Column(name="APPL_CONT")
	public String applCont;
	/** 契約形態 */
	@Column(name="CTRCT_TP")
	public String ctrctTp;
	/** 契約形態（名称） */
	@Column(name="CTRCT_TP_NM")
	public String ctrctTpNm;
	/** 内訳区分 */
	@Column(name="BRKDWN_TP")
	public String brkdwnTp;
	/** 内訳区分（名称 */
	@Column(name="BRKDWN_TP_NM")
	public String brkdwnTpNm;
	/** 業務期間（開始） */
	@Column(name="APPL_PRD_S_DT")
	public Date applPrdSDt;
	/** 業務期間（終了） */
	@Column(name="APPL_PRD_E_DT")
	public Date applPrdEDt;
	/** クレカ区分 */
	@Column(name="CRDCRD_TP")
	public String crdcrdTp;
	/** クレカ取込No */
	@Column(name="CRDCRD_IN_NO")
	public String crdcrdInNo;
	/** 前払金No */
	@Column(name="ADVPAY_NO")
	public String advpayNo;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;

	/** 支払金額（邦貨）(税込) */
	@Column(name="PAY_AMT_JPY_INCTAX")
	public BigDecimal payAmtJpyInctax;
	/** 前払充当金額（外貨） */
	@Column(name="ADVPAY_APLY_AMT_FC")
	public BigDecimal advpayAplyAmtFc;
	/** 請求先会社コード */
	@Column(name="INV_COMPANY_CD")
	public String invCompanyCd;

	/** 支払金額 */
	public BigDecimal getPayAmt() {
		if ("JPY".equals(this.mnyCd) || this.mnyCd == null) {
			return payAmtJpy;
		} else {
			return payAmtFc;
		}
	}
}
