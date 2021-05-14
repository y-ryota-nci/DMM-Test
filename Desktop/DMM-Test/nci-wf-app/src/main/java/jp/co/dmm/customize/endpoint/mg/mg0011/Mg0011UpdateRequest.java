package jp.co.dmm.customize.endpoint.mg.mg0011;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Mg0011UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 組織コード */
	public String orgnzCd;
	/** 品目コード */
	public String itmCd;
	/** 品目名称 */
	public String itmNm;
	/** 連番" */
	public Long sqno;
	/** カテゴリコード */
	public String ctgryCd;
	/** 在庫区分 */
	public String stckTp;
	/** 取引先コード */
	public String splrCd;
	/** 取引先名称（漢字） */
	public String splrNmKj;
	/** 取引先名称（カタカナ） */
	public String splrNmKn;
	/** 単位 */
	public String untCd;
	/** 金額 */
	public Long amt;
	/** 消費税コード */
	public String taxCd;
	/** メーカー名称 */
	public String makerNm;
	/** メーカー型式 */
	public String makerMdlNo;
	/** 品目備考 */
	public String itmRmk;
	/** 品目バージョン */
	public Long itmVrsn;
	/** 調達部門区分フラグ */
	public String prcFldTp;

	/** 有効開始日付 */
	public Date vdDtS;
	/** 有効終了日付 */
	public Date vdDtE;
	/** 削除フラグ */
	public String dltFg;
	/** 品目画像ID */
	public String itmImgId;
}
