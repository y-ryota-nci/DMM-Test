package jp.co.nci.iwf.endpoint.mm.mm0070;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * アクション一覧のリクエスト
 */
public class Mm0070Request extends BasePagingRequest {

	public String corporationCode;
	public String actionCode;
	public String actionName;
	public String deleteFlag;
}
