package jp.co.nci.iwf.endpoint.mm.mm0080;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * アクション機能一覧のリクエスト
 */
public class Mm0080Request extends BasePagingRequest {

	public String corporationCode;
	public String functionCode;
	public String functionName;
	public String deleteFlag;
}
