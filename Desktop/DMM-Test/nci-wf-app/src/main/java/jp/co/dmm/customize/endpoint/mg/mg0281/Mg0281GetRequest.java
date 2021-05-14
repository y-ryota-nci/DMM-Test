package jp.co.dmm.customize.endpoint.mg.mg0281;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * メディアマスタ取得リクエスト
 *
 */
public class Mg0281GetRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** メディアID */
	public String mdaId;
}
