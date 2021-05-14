package jp.co.nci.iwf.endpoint.mm.mm0093;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ルックアップ登録リクエスト
 */
public class Mm0093Request extends BaseRequest {
	public String corporationCode;
	public String lookupTypeCode;
	public String lookupCode;
	public Long timestampUpdated;

}
