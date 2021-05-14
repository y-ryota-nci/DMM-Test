package jp.co.nci.iwf.endpoint.cm.cm0010;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 企業選択画面の検索リクエスト
 */
public class Cm0010Request extends BasePagingRequest {
	public String corporationCode;
	public String corporationGroupCode;
}
