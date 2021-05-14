package jp.co.dmm.customize.endpoint.ri.ri0010;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 検収対象選択の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Ri0010Entity extends BaseJpaEntity {

	/** 発注No（表示用）(P_PURORD_NO) */
	@Id
	@Column(name="P_PURORD_NO")
	public String pPurordNo;
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 分納(SPRT_TP_NM) */
	@Column(name="SPRT_TP_NM")
	public String sprtTpNm;
	/** 発注件名(PURORD_NM) */
	@Column(name="PURORD_NM")
	public String purordNm;
	/** 取引先(SPLR_NM_KJ) */
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	/** 品目(ITEM_NM) */
	@Column(name="ITM_NM")
	public String itmNm;
	/** 部門(BUMON_CD) */
	@Column(name="BUMON_CD")
	public String bumonCd;
	/** 通貨(MNY_NM) */
	@Column(name="MNY_NM")
	public String mnyNm;

	/** 発注金額（邦貨）(PURORD_AMT) */
	@Column(name="PURORD_AMT")
	public Double purordAmt;
	/** 発注残金額（邦貨）(PURORD_AMT_REMAIN) */
	@Column(name="PURORD_AMT_REMAIN")
	public Double purordAmtRemain;
	/** 発注No(PURORD_NO) */
	@Column(name="PURORD_NO")
	public String purordNo;
	/** 発注明細No(PURORD_DTL_NO) */
	@Column(name="PURORD_DTL_NO")
	public String purordDtlNo;
	/** 取引先コード(SPLR_CD) */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 取引先名称（カタカナ）(SPLR_NM_KN) */
	@Column(name="SPLR_NM_KN")
	public String splrNmKn;
	/** 通貨区分(MNY_TP) */
	@Column(name="MNY_TP")
	public String mnyTp;
	/** 組織コード(ORGNZ_CD) */
	@Column(name="ORGNZ_CD")
	public String orgnzCd;
	/** 費目コード1(ITMEXPS_CD1) */
	@Column(name="ITMEXPS_CD1")
	public String itmexpsCd1;
	/** 費目名称1(ITMEXPS_NM1) */
	@Column(name="ITMEXPS_NM1")
	public String itmexpsNm1;
	/** 費目コード2(ITMEXPS_CD2) */
	@Column(name="ITMEXPS_CD2")
	public String itmexpsCd2;
	/** 費目名称2(ITMEXPS_NM2) */
	@Column(name="ITMEXPS_NM2")
	public String itmexpsNm2;
	/** 品目コード(ITM_CD) */
	@Column(name="ITM_CD")
	public String itmCd;
	/** 通貨コード(MNY_CD) */
	@Column(name="MNY_CD")
	public String mnyCd;
	/** 分析コード(ANLYS_CD) */
	@Column(name="ANLYS_CD")
	public String anlysCd;
	/** 検収金額(RCVINSP_AMT) */
	@Column(name="RCVINSP_AMT")
	public Double rcvinspAmt;
	/** 発注金額（外貨）(PURORD_AMT_FC) */
	@Column(name="PURORD_AMT_FC")
	public Double purordAmtFc;
	/** 消費税(TAX_CD) */
	@Column(name="TAX_CD")
	public String taxCd;
	/** 支払方法(PAY_MTH) */
	@Column(name="PAY_MTH")
	public String payMth;
	/** 納入場所(DLV_LC) */
	@Column(name="DLV_LC")
	public String dlvLc;
	/** 支払サイトコード(PAY_SITE_CD) */
	@Column(name="PAY_SITE_CD")
	public String paySiteCd;
	/** 支払サイト名称(PAY_SITE_NM) */
	@Column(name="PAY_SITE_NM")
	public String paySiteNm;
	/** 支払条件コード(PAY_COND_CD) */
	@Column(name="PAY_COND_CD")
	public String payCondCd;
	/** 支払条件(PAY_COND_NM) */
	@Column(name="PAY_COND_NM")
	public String payCondNm;
	/** 名称入力可能フラグ(IPT_NM_FG) */
	@Column(name="IPT_NM_FG")
	public String iptNmFg;
	/** 行番号(ROWNUM) */
	@Column(name="ROWNUM")
	public String rownum;

	/** 伝票グループ(GL) */
	@Column(name="SLP_GRP_GL")
	public String slpGrpGl;
	/** 経費区分 */
	@Column(name="CST_TP")
	public String cstTp;
	/** 資産区分 */
	@Column(name="ASST_TP")
	public String asstTp;

	/** 納品日 */
	@Column(name="DLV_PLN_DT")
	public Date dlvPlnDt;
	/** 検収予定日 */
	@Column(name="INSP_COMP_DT")
	public Date inspCompDt;
	/** 支払予定日 */
	@Column(name="PAY_PLN_DT")
	public Date payPlnDt;
	/** サービス期間（開始） */
	@Column(name="RCVINSP_YM_S")
	public String rcvinspYmS;
	/** サービス期間（終了） */
	@Column(name="RCVINSP_YM_E")
	public String rcvinspYmE;
	/** 印鑑種別 */
	@Column(name="STMP_TP")
	public String stmpTp;
	/** 分割可否 */
	@Column(name="SPRT_TP")
	public String sprtTp;
	/** 分割回数 */
	@Column(name="SPRT_CNT")
	public Integer sprtCnt;
	/** 検収内容 */
	@Column(name="PURORD_RQST_CONT")
	public String purordRqstCont;
	/** 目的 */
	@Column(name="PURPS")
	public String purps;
	/** 計上レート */
	@Column(name="ADD_RTO")
	public BigDecimal addRto;
	/** 小数点桁数 */
	@Column(name="RDXPNT_GDT")
	public Integer rdxpntGdt;
	/** 輸入消費税 */
	@Column(name="IMP_TAX_TP")
	public String impTaxTp;
	/** 消費税処理単位 */
	@Column(name="TAX_UNT")
	public String taxUnt;
	/** 消費税率 */
	@Column(name="TAX_RTO")
	public BigDecimal taxRto;
	/** 税処理区分 */
	@Column(name="TAX_TP")
	public String taxTp;
	/** 消費税フラグ */
	@Column(name="TAX_FG")
	public String taxFg;
	/** 源泉税区分 */
	@Column(name="HLDTAX_TP")
	public String hldtaxTp;
	/** 源泉税フラグ */
	@Column(name="HLDTAX_FG")
	public String hldtaxFg;
	/** 源泉税対象額 */
	@Column(name="HLDTAX_SBJ_AMT")
	public BigDecimal hldtaxSbjAmt;
	/** 源泉徴収税額 */
	@Column(name="HLDTAX_AMT")
	public BigDecimal hldtaxAmt;

	/** 源泉税率(1) */
	@Column(name="HLDTAX_RTO")
	public BigDecimal hldtaxRto;
	/** 源泉税率(2) */
	@Column(name="HLDTAX_RTO2")
	public BigDecimal hldtaxRto2;
	/** 検収摘要 */
	@Column(name="RCVINSP_SMRY")
	public String rcvinspSmry;

	@Column(name="INV_COMPANY_CD")
	public String invCompanyCd;


	/** 明細.消費税(TAX_CD) */
	@Column(name="TAX_CD_DTL")
	public String taxCdDtl;
	/** 明細.消費税率 */
	@Column(name="TAX_RTO_DTL")
	public BigDecimal taxRtoDtl;
	/** 明細.税処理区分 */
	@Column(name="TAX_TP_DTL")
	public String taxTpDtl;
	/** 明細.消費税フラグ */
	@Column(name="TAX_FG_DTL")
	public String taxFgDtl;
	/** 前払区分 */
	@Column(name="ADVPAY_TP")
	public String advpayTp;
	/** 製本担当 */
	@Column(name="BKBND_CHG_TP")
	public String bkbndChgTp;
	/** 郵送曜日 */
	@Column(name="MAIL_TP")
	public String mailTp;
	/** 郵送コメント */
	@Column(name="MAIL_RMK")
	public String mailRmk;
	/** 送付先_郵便番号 */
	@Column(name="SHT_TO_ZIP_CD")
	public String shtToZipCd;
	/** 送付先_都道府県CD */
	@Column(name="SHT_ADR_PRF_CD")
	public String shtAdrPrfCd;
	/** 送付先_都道府県 */
	@Column(name="SHT_ADR_PRF")
	public String shtAdrPrf;
	/** 送付先_市区町村 */
	@Column(name="SHT_ADR1")
	public String shtAdr1;
	/** 送付先_町名 */
	@Column(name="SHT_ADR2")
	public String shtAdr2;
	/** 送付先_建物名 */
	@Column(name="SHT_ADR3")
	public String shtAdr3;
	/** 送付先_担当者氏名 */
	@Column(name="SHT_PIC_NM")
	public String shtPicNm;
	/** 送付先_担当者役職 */
	@Column(name="SHT_DPT_NM")
	public String shtDptNm;
	/** 送付先_メールアドレス */
	@Column(name="SHT_MLADR")
	public String shtMladr;
	/** 送付先_電話番号 */
	@Column(name="SHT_TEL_NO")
	public String shtTelNo;

	/** 法人・個人区分 */
	@Column(name="CRP_PRS_TP")
	public String crpPrsTp;
	/** 国内・海外区分 */
	@Column(name="DMS_ABR_TP")
	public String dmsAbrTp;

	/** 広告費（マーケ専用） */
	@Column(name="ADVCST_MRK")
	public String advcstMrk;
	/** デバイスコード */
	@Column(name="DVC_CD")
	public String dvcCd;
	/** 結合フロアコード */
	@Column(name="BND_FLR_CD")
	public String bndFlrCd;
	/** 結合フロア名称 */
	@Column(name="BND_FLR_NM")
	public String bndFlrNm;
	/** 成果地点コード */
	@Column(name="FRT_PNT_CD")
	public String frtPntCd;
	/** 計測ツールコード */
	@Column(name="MSR_TOOL_CD")
	public String msrToolCd;
	/** メディアID */
	@Column(name="MDA_ID")
	public String mdaId;
	@Column(name="TAX_SBJ_TP")
	public String taxSbjTp;
	@Column(name="TAX_KND_CD")
	public String taxKndCd;
	@Column(name="TAX_FG_CHG")
	public String taxFgChg;
	@Column(name="TAX_SBJ_TP_DTL")
	public String taxSbjTpDtl;
	@Column(name="TAX_KND_CD_DTL")
	public String taxKndCdDtl;
	@Column(name="TAX_FG_CHG_DTL")
	public String taxFgChgDtl;
	/** 部門名 */
	@Column(name="BUMON_NM")
	public String bumonNm;

}
