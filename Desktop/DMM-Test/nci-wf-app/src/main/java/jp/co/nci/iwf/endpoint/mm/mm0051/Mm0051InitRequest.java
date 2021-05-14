package jp.co.nci.iwf.endpoint.mm.mm0051;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 採番形式登録リクエスト
 */
public class Mm0051InitRequest extends BaseRequest {
	public String corporationCode;
	public String partsNumberingFormatCode;
	public Long version;
}
