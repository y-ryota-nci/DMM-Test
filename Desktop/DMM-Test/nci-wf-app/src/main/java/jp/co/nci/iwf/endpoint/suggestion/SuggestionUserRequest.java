package jp.co.nci.iwf.endpoint.suggestion;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * Sandboxのサジェスチョン用リクエスト
 */
public class SuggestionUserRequest extends BasePagingRequest {
	public String corporationCode;
	public String userAddedInfo;
	public String userName;
	public Date baseDate;
	public String mailAddress;
}
