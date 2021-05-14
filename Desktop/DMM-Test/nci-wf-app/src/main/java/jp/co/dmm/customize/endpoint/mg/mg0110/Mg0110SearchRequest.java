package jp.co.dmm.customize.endpoint.mg.mg0110;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0110SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 銀行コード */
	public String bnkCd;
	/** 銀行名称 */
	public String bnkNm;
	/** 銀行支店コード */
	public String bnkbrcCd;
	/** 銀行支店名称 */
	public String bnkbrcNm;
	/** 銀行支店名（カナ）*/
	public String bnkbrcNmKn;
	/** 有効期間（開始） */
	public String vdDtS;
	/** 有効期間（終了） */
	public String vdDtE;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
