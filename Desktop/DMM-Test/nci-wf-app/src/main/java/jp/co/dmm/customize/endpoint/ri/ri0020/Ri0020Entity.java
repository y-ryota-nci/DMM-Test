package jp.co.dmm.customize.endpoint.ri.ri0020;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 検収明細分割の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class Ri0020Entity extends BaseJpaEntity {

	/** 発注No（表示用）(P_PURORD_NO) */
	@Id
	@Column(name="P_PURORD_NO")
	public String pPurordNo;

	/** 会社コード(COMPANY_CD) */
	@Column(name="COMPANY_CD")
	public String companyCd;

	/** 発注No(PURORD_NO) */
	@Column(name="PURORD_NO")
	public String purordNo;

	/** 発注明細No(PURORD_DTL_NO) */
	@Column(name="PURORD_DTL_NO")
	public String purordDtlNo;

	/** 分納(SPRT_TP_NM) */
	@Column(name="SPRT_TP_NM")
	public String sprtTpNm;

	/** 発注件名(PURORD_NM) */
	@Column(name="PURORD_NM")
	public String purordNm;

	/** 取引先コード(SPLR_CD) */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 取引先名称（カタカナ）(SPLR_NM_KN) */
	@Column(name="SPLR_NM_KN")
	public String splrNmKn;
	/** 取引先(SPLR_NM_KJ) */
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;

	/** 通貨コード(MNY_CD) */
	@Column(name="MNY_CD")
	public String mnyCd;

	/** 通貨(MNY_NM) */
	@Column(name="MNY_NM")
	public String mnyNm;

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

	/** 組織コード(ORGNZ_CD) */
	@Column(name="ORGNZ_CD")
	public String orgnzCd;

	/** 品目コード(ITM_CD) */
	@Column(name="ITM_CD")
	public String itmCd;

	/** 品目(ITEM_NM) */
	@Column(name="ITM_NM")
	public String itmNm;

	/** 名称入力可能フラグ(IPT_NM_FG) */
	@Column(name="IPT_NM_FG")
	public String iptNmFg;

	/** 部門(BUMON_CD) */
	@Column(name="BUMON_CD")
	public String bumonCd;

	/** 分析コード(ANLYS_CD) */
	@Column(name="ANLYS_CD")
	public String anlysCd;

	/** 発注金額（邦貨）(PURORD_AMT) */
	@Column(name="PURORD_AMT")
	public BigDecimal purordAmt;

	/** 検収金額（邦貨）(RCVINSP_AMT) */
	@Column(name="RCVINSP_AMT")
	public BigDecimal rcvinspAmt;

	/** 発注残金額（邦貨）(PURORD_AMT_REMAIN) */
	@Column(name="PURORD_AMT_REMAIN")
	public BigDecimal purordAmtRemain;

	/** 発注金額（外貨）(PURORD_AMT_FC) */
	@Column(name="PURORD_AMT_FC")
	public BigDecimal purordAmtFc;

	/** 消費税(TAX_CD) */
	@Column(name="TAX_CD")
	public String taxCd;

	/** 通貨区分(MNY_TP) */
	@Column(name="MNY_TP")
	public String mnyTp;

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

	/** 発注摘要 */
	@Column(name="RCVINSP_SMRY")
	public BigDecimal rcvinspSmry;

	/** 支払条件コード */
	@Column(name="PAY_COND_CD")
	public String payCondCd;

	/** 支払条件名称 */
	@Column(name="PAY_COND_NM")
	public String payCondNm;

	/** 行番号(ROWNUM) */
	@Column(name="ROWNUM")
	public String rownum;
}
