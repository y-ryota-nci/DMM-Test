package jp.co.dmm.customize.endpoint.mg.mg0170;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 通貨マスタ検索リクエスト
 *
 */
public class Mg0170SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 通貨コード */
	public String mnyCd;
	/** 通貨名称 */
	public String mnyNm;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
