package jp.co.nci.iwf.endpoint.rm.rm0903;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ASP管理者ロール構成登録リクエスト
 */
public class Rm0903Request extends BaseRequest {
	public String corporationCode;
	public String menuRoleCode;
	public Long seqNoMenuRoleDetail;
}
