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
 * SS-AP_送信情報(明細)
 */
@Entity
@Access(AccessType.FIELD)
public class Ss0010SsapSndInfDt extends BaseJpaEntity {

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
	/** 明細行番号 */
	@Column(name="DTL_LNNO")
	public BigDecimal dtlLnno;
	/** AP科目区分 */
	@Column(name="AP_ACC_TP")
	public String apAccTp;
	/** 科目コード */
	@Column(name="ACC_CD")
	public String accCd;
	/** 補助科目コード */
	@Column(name="ACC_BRKDWN_CD")
	public String accBrkdwnCd;
	/** 部門コード */
	@Column(name="DPT_CD")
	public String dptCd;
	/** 機能コード1 */
	@Column(name="FNC_CD1")
	public String fncCd1;
	/** 機能コード2 */
	@Column(name="FNC_CD2")
	public String fncCd2;
	/** 機能コード3 */
	@Column(name="FNC_CD3")
	public String fncCd3;
	/** 機能コード4 */
	@Column(name="FNC_CD4")
	public String fncCd4;
	/** プロジェクトコード */
	@Column(name="PRJ_CD")
	public String prjCd;
	/** 取引先・社員区分 */
	@Column(name="SPLR_PRS_TP")
	public String splrPrsTp;
	/** 取引先・社員コード */
	@Column(name="SPLR_PRS_CD")
	public String splrPrsCd;
	/** 仕訳金額 */
	@Column(name="JRN_AMT")
	public BigDecimal jrnAmt;
	/** 税抜金額 */
	@Column(name="AMT_EXCTAX")
	public BigDecimal amtExctax;
	/** 税額 */
	@Column(name="TAX_AMT")
	public BigDecimal taxAmt;
	/** 税処理コード */
	@Column(name="TAX_CD")
	public String taxCd;
	/** 税入力区分 */
	@Column(name="TAX_IPT_TP")
	public String taxIptTp;
	/** 摘要1 */
	@Column(name="SMRY1")
	public String smry1;
	/** 摘要2 */
	@Column(name="SMRY2")
	public String smry2;
	/** 取引通貨金額 */
	@Column(name="TRD_MNY_AMT")
	public BigDecimal trdMnyAmt;
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
