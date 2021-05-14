package jp.co.nci.iwf.endpoint.mm.mm0052;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 通し番号一覧のリクエスト
 */
public class Mm0052Request extends BasePagingRequest {

	public String corporationCode;
	public String partsSequenceSpecCode;
	public String partsSequenceSpecName;
	public String deleteFlag;
	public String partsNumberingFormatCode;
	public Long version;
}
