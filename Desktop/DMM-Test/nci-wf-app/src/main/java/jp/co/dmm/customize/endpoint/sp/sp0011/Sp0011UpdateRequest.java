package jp.co.dmm.customize.endpoint.sp.sp0011;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 取引先登録情報更新リクエスト
 */
public class Sp0011UpdateRequest extends BaseRequest {

	/** 会社CD */
	public String companyCd;
	/** 取引先コード */
	public String splrCd;
	/** 連番 */
	public long sqno;
	/** 法人・個人区分 */
	public String crpPrsTp;
	/** 国内・海外区分 */
	public String dmsAbrTp;
	/** 国名 */
	public String lndNm;
	/** 法人番号 */
	public String crpNo;
	/** 取引先名称（漢字） */
	public String splrNmKj;
	/** 取引先名称（カタカナ） */
	public String splrNmKn;
	/** 取引先名称（略称） */
	public String splrNmS;
	/** 取引先名称（英名） */
	public String splrNmE;
	/** 郵便番号 */
	public String zipCd;
	/** 住所（都道府県） */
	public String adrPrfCd;
	/** 住所（都道府県）名称 */
	public String adrPrfNm;
	/** 住所（市区町村） */
	public String adr1;
	/** 住所（町名番地） */
	public String adr2;
	/** 住所（建物名） */
	public String adr3;
	/** 電話番号 */
	public String telNo;
	/** FAX番号 */
	public String faxNo;
	/** 関係会社区分 */
	public String affcmpTp;
	/** 資本金 */
	public BigDecimal cptl;
	/** メールアドレス１ */
	public String mladr1;
	/** メールアドレス２ */
	public String mladr2;
	/** 取引状況区分 */
	public String trdStsTp;
	/** 取引状況区分（名称） */
	public String trdStsNm;
	/** 有効開始日付 */
	@Temporal(TemporalType.DATE)
	public Date vdDtS;
	/** 有効終了日付 */
	@Temporal(TemporalType.DATE)
	public Date vdDtE;
	/** 振込手数料負担区分 */
	public String payCmmOblTp;
	/** 休日処理区分 */
	public String hldTrtTp;
	/** 支払業務コード */
	public String payBsnCd;
	/** 支払条件コード */
	public String payCondCd;
	/** 支払条件名称 */
	public String payCondNm;

	/** 下請法区分 */
	public String subCntrctTp;
	/** 取引先担当者部署 */
	public String splrDptNm;
	/** 取引先担当者役職 */
	public String splrPstNm;
	/** 取引先担当者名称 */
	public String splrPicNm;

	/** 備考 */
	public String rmk;
	/** 摘要（社内向け） */
	public String abstIn;

	/** 部門コード */
	public String bumonCd;
}
