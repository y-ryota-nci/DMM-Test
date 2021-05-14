package jp.co.dmm.customize.endpoint.mg.mg0320;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 消費税関連マスタ検索リクエスト
 *
 */
public class Mg0320SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 消費税種類コード */
	public String taxKndCd;
	/** 消費税種別 */
	public String taxSpc;
	/** 消費税コード */
	public String taxCd;
	/** 消費税名称 */
	public String taxNm;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
