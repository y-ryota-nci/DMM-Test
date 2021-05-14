package jp.co.dmm.customize.endpoint.mg.mg0190;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0190SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 部門コード */
	public String bumonCd;
	/** 部門名称 */
	public String bumonNm;
	/** 事業分類コード */
	public String entrpTpCd;
	/** 事業コード */
	public String entrpCd;
	/** タブコード */
	public String tabCd;
	/** サイトコード */
	public String siteCd;
	/** 分類コード */
	public String tpCd;
	/** 消費税種類コード */
	public String taxKndCd;

	/** 事業名称 */
	public String entrpNm;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
