package jp.co.nci.iwf.endpoint.mm.mm0053;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 通し番号登録リクエスト
 */
public class Mm0053Request extends BaseRequest {
	public String corporationCode;
	public String partsSequenceSpecCode;
	public Long version;
}
