package jp.co.nci.iwf.endpoint.rm.rm0510;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 業務管理項目名称設定のリクエスト
 */
public class Rm0510Request extends BaseRequest {
	public String corporationCode;
	public String menuRoleCode;
}
