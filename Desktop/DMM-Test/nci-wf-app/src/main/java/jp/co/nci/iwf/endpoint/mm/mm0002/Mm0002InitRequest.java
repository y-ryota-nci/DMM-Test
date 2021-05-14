package jp.co.nci.iwf.endpoint.mm.mm0002;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 組織編集画面の初期化リクエスト
 */
public class Mm0002InitRequest extends BaseRequest {
	public String corporationCode;
	public String organizationCode;
	public Long timestampUpdated;
	public java.sql.Date baseDate;
}
