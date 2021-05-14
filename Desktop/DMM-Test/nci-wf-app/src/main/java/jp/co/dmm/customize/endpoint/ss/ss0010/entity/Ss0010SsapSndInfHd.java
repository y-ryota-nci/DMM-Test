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
 * SS-AP_送信情報(ヘッダー)
 */
@Entity
@Access(AccessType.FIELD)
public class Ss0010SsapSndInfHd extends BaseJpaEntity {

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
	/** 支払先区分 */
	@Column(name="PAYER_TP")
	public String payerTp;
	/** 仕入先コード */
	@Column(name="BUYER_CD")
	public String buyerCd;
	/** 請求書No. */
	@Column(name="INVSHT_NO")
	public String invshtNo;
	/** 請求書日付 */
	@Column(name="INVSHT_DT")
	public Date invshtDt;
	/** 支払業務コード */
	@Column(name="PAY_APPL_CD")
	public String payApplCd;
	/** AP伝票種類 */
	@Column(name="AP_SLT_TP")
	public String apSltTp;
	/** 貸借反転区分 */
	@Column(name="DC_RVR_TP")
	public String dcRvrTp;
	/** 赤伝区分 */
	@Column(name="R_SLP_TP")
	public String rSlpTp;
	/** 元伝票グループ */
	@Column(name="ORG_SLP_GRP")
	public String orgSlpGrp;
	/** 元伝票番号 */
	@Column(name="ORG_SLP_NO")
	public String orgSlpNo;
	/** 元伝票日付 */
	@Column(name="ORG_SLP_DT")
	public Date orgSlpDt;
	/** 他システム伝票番号 */
	@Column(name="ELS_SYS_SLP_NO")
	public String elsSysSlpNo;
	/** 通貨コード */
	@Column(name="MNY_CD")
	public String mnyCd;
	/** レートタイプ */
	@Column(name="RTO_TP")
	public String rtoTp;
	/** 換算レート */
	@Column(name="CHG_RTO")
	public BigDecimal chgRto;
	/** 伝票摘要 */
	@Column(name="SLP_SMRY")
	public String slpSmry;
	/** スポット支払区分 */
	@Column(name="SPT_PAY_TP")
	public String sptPayTp;
	/** 起票者ID */
	@Column(name="ISSVCHER_ID")
	public String issvcherId;
	/** 起票日付 */
	@Column(name="ISSVCHER_DT")
	public Date issvcherDt;
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
