package jp.co.nci.iwf.endpoint.rm.rm0500;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 利用者ロール一覧のリクエスト
 */
public class Rm0500Request extends BasePagingRequest {

	public String corporationCode;
	public String menuRoleCode;
	public String menuRoleName;
	public String deleteFlag;
}
