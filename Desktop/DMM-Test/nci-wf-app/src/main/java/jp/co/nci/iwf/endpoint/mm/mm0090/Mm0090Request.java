package jp.co.nci.iwf.endpoint.mm.mm0090;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ルックアップグループ一覧のリクエスト
 */
public class Mm0090Request extends BasePagingRequest {

	public String corporationCode;
	public String lookupTypeCode;
	public String lookupTypeName;
	public String deleteFlag;
	public String updateFlag;
}
