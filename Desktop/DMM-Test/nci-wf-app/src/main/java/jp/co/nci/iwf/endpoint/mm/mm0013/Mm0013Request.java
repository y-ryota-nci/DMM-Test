package jp.co.nci.iwf.endpoint.mm.mm0013;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ルックアップ登録リクエスト
 */
public class Mm0013Request extends BaseRequest {
	public String corporationCode;
	public String lookupGroupId;
	public String lookupId;
	public Long timestampUpdated;

}
