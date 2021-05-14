package jp.co.dmm.customize.endpoint.mg.mg0171;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 通貨マスタ取得リクエスト
 *
 */
public class Mg0171GetRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 通貨コード */
	public String mnyCd;
}
