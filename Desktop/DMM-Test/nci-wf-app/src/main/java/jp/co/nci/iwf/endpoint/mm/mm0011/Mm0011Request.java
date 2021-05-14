package jp.co.nci.iwf.endpoint.mm.mm0011;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ルックアップグループ登録リクエスト
 */
public class Mm0011Request extends BaseRequest {
	public String corporationCode;
	public String lookupGroupId;
}
