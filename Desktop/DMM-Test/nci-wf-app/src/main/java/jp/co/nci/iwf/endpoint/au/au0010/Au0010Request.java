package jp.co.nci.iwf.endpoint.au.au0010;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ユーザパスワード一覧のリクエスト
 */
public class Au0010Request extends BasePagingRequest {
	public String corporationCode;
	public String userAddedInfo;
	public String userName;
	public boolean lockFlag;
	public boolean changeRequestFlag;
}
