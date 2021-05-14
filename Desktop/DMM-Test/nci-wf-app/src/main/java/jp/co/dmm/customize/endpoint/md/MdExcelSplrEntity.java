package jp.co.dmm.customize.endpoint.md;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.dmm.customize.jpa.entity.mw.SplrMstPK;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 取引先一覧の検索結果エンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class MdExcelSplrEntity extends BaseJpaEntity {

	@EmbeddedId
	private SplrMstPK id;

	/** 処理区分 */
	@Column(name="PROCESS_TYPE")
	public String processType;

	/** 会社コード */
	@Column(name="COMPANY_CD")
	public String companyCd;
	/** 会社コード(会社付加コード) */
	@Column(name="COMPANY_ADDED_INFO")
	public String companyAddedInfo;
	/** 会社名 */
	@Column(name="COMPANY_NM")
	public String companyNm;
	/** 取引先コード */
	@Column(name="SPLR_CD")
	public String splrCd;
	/** 適格請求書発行事業者 */
	@Column(name="CMPT_ETR_NO")
	public String cmptEtrNo;
	/** 連番 */
	@Column(name="SQNO")
	public long sqno;
	/** 法人・個人区分 */
	@Column(name="CRP_PRS_TP")
	public String crpPrsTp;
	/** 国内・海外区分 */
	@Column(name="DMS_ABR_TP")
	public String dmsAbrTp;
	/** 国コード */
	@Column(name="LND_CD")
	public String lndCd;
	/** 国名 */
	@Column(name="LND_NM")
	public String lndNm;
	/** 法人番号 */
	@Column(name="CRP_NO")
	public String crpNo;
	/** 取引先名称（漢字） */
	@Column(name="SPLR_NM_KJ")
	public String splrNmKj;
	/** 取引先名称（カタカナ） */
	@Column(name="SPLR_NM_KN")
	public String splrNmKn;
	/** 取引先名称（略称） */
	@Column(name="SPLR_NM_S")
	public String splrNmS;
	/** 取引先名称（英名） */
	@Column(name="SPLR_NM_E")
	public String splrNmE;
	/** 郵便番号 */
	@Column(name="ZIP_CD")
	public String zipCd;
	/** 住所（都道府県コード） */
	@Column(name="ADR_PRF_CD")
	public String adrPrfCd;
	/** 住所（都道府県） */
	@Column(name="ADR_PRF")
	public String adrPrf;
	/** 住所（市区町村） */
	@Column(name="ADR1")
	public String adr1;
	/** 住所（町名番地） */
	@Column(name="ADR2")
	public String adr2;
	/** 住所（建物名） */
	@Column(name="ADR3")
	public String adr3;
	/** 電話番号 */
	@Column(name="TEL_NO")
	public String telNo;
	/** FAX番号 */
	@Column(name="FAX_NO")
	public String faxNo;
	/** 関係会社区分 */
	@Column(name="AFFCMP_TP")
	public String affcmpTp;
	/** 生年月日 */
	@Temporal(TemporalType.DATE)
	@Column(name="BRTH_DT")
	public Date brthDt;
	/** 取引状況区分 */
	@Column(name="TRD_STS_TP")
	public String trdStsTp;
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
	/** 最終判定区分 */
	@Column(name="LAST_JDG_TP")
	public String lastJdgTp;
	/** 最終判定備考 */
	@Column(name="LAST_JDG_RMK")
	public String lastJdgRmk;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;

	/** 部門コード */
	@Column(name="BUMON_CD")
	public String bumonCd;

	/** エラー */
	public String errorText;

	/** 有効開始日付 文字列 */
	public String strVdDtS;
	/** 有効終了日付 文字列 */
	public String strVdDtE;
	/** 連番 文字列*/
	public String strSqno;
	/** 生年月日 文字列 */
	public String strBrthDt;
}
