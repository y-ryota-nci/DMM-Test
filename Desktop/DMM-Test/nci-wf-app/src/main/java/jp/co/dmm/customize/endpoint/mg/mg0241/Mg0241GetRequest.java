package jp.co.dmm.customize.endpoint.mg.mg0241;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 支払サイトマスタ取得リクエスト
 *
 */
public class Mg0241GetRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 支払サイトコード */
	public String paySiteCd;
}
