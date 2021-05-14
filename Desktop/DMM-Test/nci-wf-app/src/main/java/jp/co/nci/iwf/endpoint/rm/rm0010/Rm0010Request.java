package jp.co.nci.iwf.endpoint.rm.rm0010;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 利用者ロール登録リクエスト
 */
public class Rm0010Request extends BaseRequest {
	public String corporationCode;
	public String menuRoleCode;
}
