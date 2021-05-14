package jp.co.nci.iwf.endpoint.dc.dc0060;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 拡張項目(メタ項目)一覧リクエスト.
 */
public class Dc0060Request extends BasePagingRequest {

	public String corporationCode;
	public String metaCode;
	public String metaName;
	public String inputType;
	public boolean requiredFlag;
	public String deleteFlag;
}
