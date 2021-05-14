package jp.co.nci.iwf.endpoint.mm.mm0431;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 役職設定リクエスト
 */
public class Mm0431Request extends BaseRequest {
	public String corporationCode;
	public String postCode;
	public Long timestampUpdated;
}
