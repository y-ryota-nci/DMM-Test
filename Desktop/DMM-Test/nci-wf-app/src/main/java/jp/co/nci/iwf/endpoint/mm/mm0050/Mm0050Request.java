package jp.co.nci.iwf.endpoint.mm.mm0050;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 採番形式一覧のリクエスト
 */
public class Mm0050Request extends BasePagingRequest {

	public String corporationCode;
	public String partsNumberingFormatCode;
	public String partsNumberingFormatName;
	public String deleteFlag;
}
