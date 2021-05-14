package jp.co.nci.iwf.endpoint.dc.dc0070;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 拡張項目(メタ項目)一覧リクエスト.
 */
public class Dc0070Request extends BasePagingRequest {

	public String corporationCode;
	public String metaTemplateCode;
	public String metaTemplateName;
	public String deleteFlag;
	public String localeCode;
}
