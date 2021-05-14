package jp.co.dmm.customize.endpoint.sp;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import jp.co.dmm.customize.jpa.entity.mw.SplrMstPK;
import jp.co.nci.iwf.jpa.entity.BaseJpaEntity;

/**
 * 取引先マスタエンティティ
 */
@Entity
@Access(AccessType.FIELD)
public class SplrMstEntity extends BaseJpaEntity {

	@EmbeddedId
	private SplrMstPK id;

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
	/** 連番 */
	@Column(name="SQNO")
	public Long sqno;
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
	/** 法人・個人区分 */
	@Column(name="CRP_PRS_TP")
	public String crpPrsTp;
	/** 法人・個人区分（名称） */
	@Column(name="CRP_PRS_NM")
	public String crpPrsNm;
	/** 国内・海外区分 */
	@Column(name="DMS_ABR_TP")
	public String dmsAbrTp;
	/** 国内・海外区分（名称）  */
	@Column(name="DMS_ABR_NM")
	public String dmsAbrNm;
	/** 国名 */
	@Column(name="LND_NM")
	public String lndNm;
	/** 法人番号 */
	@Column(name="CRP_NO")
	public String crpNo;
	/** 郵便番号 */
	@Column(name="ZIP_CD")
	public String zipCd;
	/** 住所（都道府県） */
	@Column(name="ADR_PRF_CD")
	public String adrPrfCd;
	/** 住所（都道府県）名称 */
	@Column(name="ADR_PRF")
	public String adrPrfNm;
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
	/** 関係会社区分（名称） */
	@Column(name="AFFCMP_NM")
	public String affcmpNm;
	/** 取引状況区分 */
	@Column(name="TRD_STS_TP")
	public String trdStsTp;
	/** 取引状況区分（名称） */
	@Column(name="TRD_STS_NM")
	public String trdStsNm;
	/** 有効開始日付 */
	@Column(name="VD_DT_S")
	public Date vdDtS;
	/** 有効終了日付 */
	@Column(name="VD_DT_E")
	public Date vdDtE;
	/** 支払業務コード */
	@Column(name="PAY_BSN_CD")
	public String payBsnCd;
	/** 支払業務（名称） */
	@Column(name="PAY_BSN_NM")
	public String payBsnNm;
	/** 備考 */
	@Column(name="RMK")
	public String rmk;
	/** 削除フラグ */
	@Column(name="DLT_FG")
	public String dltFg;
	/** 有効か */
	@Column(name="ENABLED")
	public String enabled;
	/** 部門コード */
	@Column(name="BUMON_CD")
	public String bumonCd;
	/** 部門名称 */
	@Column(name="BUMON_NM")
	public String bumonNm;
	/** 適格請求書発行事業者登録番号 */
	@Column(name="CMPT_ETR_NO")
	public String cmptEtrNo;
	/** 生年月日 */
	@Column(name="BRTH_DT")
	public Date brthDt;
	/** 国コード */
	@Column(name="LND_CD")
	public String lndCd;

}
