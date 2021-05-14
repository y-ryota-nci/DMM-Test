package jp.co.nci.iwf.endpoint.mm.mm0000;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 組織追加リクエスト
 */
public class Mm0000AddOrgRequest extends BaseRequest {
	public String corporationCode;
	public String organizationCodeUp;
	public java.sql.Date baseDate;
	public Boolean displayValidOnly;
}
