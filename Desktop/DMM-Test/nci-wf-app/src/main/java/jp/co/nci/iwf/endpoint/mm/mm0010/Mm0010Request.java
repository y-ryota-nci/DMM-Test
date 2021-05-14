package jp.co.nci.iwf.endpoint.mm.mm0010;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ルックアップグループ一覧のリクエスト
 */
public class Mm0010Request extends BasePagingRequest {

	public String corporationCode;
	public String lookupGroupId;
	public String lookupGroupName;
	public String deleteFlag;
}
