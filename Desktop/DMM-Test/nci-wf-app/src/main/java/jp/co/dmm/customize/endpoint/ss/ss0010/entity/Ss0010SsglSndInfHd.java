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
 * SS-GL_送信情報(ヘッダー)
 */
@Entity
@Access(AccessType.FIELD)
public class Ss0010SsglSndInfHd extends BaseJpaEntity {

	/** 疑似ID(=ROWNUM) */
	@Id
	@Column(name="ID")
	public long id;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** SSGL送信No */
	@Column(name="SSGL_SND_NO")
	public String ssglSndNo;
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
	/** 伝票摘要 */
	@Column(name="SLP_SMRY")
	public String slpSmry;
	/** 決算伝票区分 */
	@Column(name="STT_SLP_TP")
	public String sttSlpTp;
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
	/** 洗替伝票区分 */
	@Column(name="EXCCLT_SLP_TP")
	public String exccltSlpTp;
	/** 配賦伝票区分 */
	@Column(name="ALLCT_SLP_TP")
	public String allctSlpTp;
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
