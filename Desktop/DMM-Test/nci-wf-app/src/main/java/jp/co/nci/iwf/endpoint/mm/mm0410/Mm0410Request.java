package jp.co.nci.iwf.endpoint.mm.mm0410;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 企業一覧のリクエスト
 */
public class Mm0410Request extends BasePagingRequest {

	public String corporationCode;
	public String corporationName;
	public String deleteFlag;
	public String corporationGroupCode;
}
