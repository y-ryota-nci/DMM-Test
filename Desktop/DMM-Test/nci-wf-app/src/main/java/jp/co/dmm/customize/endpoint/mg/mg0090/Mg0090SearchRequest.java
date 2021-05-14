package jp.co.dmm.customize.endpoint.mg.mg0090;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0090SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 銀行口座コード */
	public String bnkaccCd;
	/** 連番 */
	public String sqno;
	/** 銀行コード */
	public String bnkCd;
	/** 銀行名称 */
	public String bnkNm;
	/** 銀行支店コード */
	public String bnkbrcCd;
	/** 銀行支店名称 */
	public String bnkbrcNm;
	/** 銀行口座種別 */
	public String bnkaccTp;
	/** 銀行口座番号 */
	public String bnkaccNo;
	/** 銀行口座名称 */
	public String bnkaccNm;
	/** 銀行口座名称(カタカナ) */
	public String bnkaccNmKn;
	/** 有効期間（開始） */
	public String vdDtS;
	/** 有効期間（終了） */
	public String vdDtE;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
