package jp.co.dmm.customize.endpoint.sp.sp0010;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 住所取得リクエスト
 */
public class Sp0010GetAddressRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 郵便番号 **/
	public String zipCd;
}
