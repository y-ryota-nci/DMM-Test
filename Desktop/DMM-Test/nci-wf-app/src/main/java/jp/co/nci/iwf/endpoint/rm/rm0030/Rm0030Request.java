package jp.co.nci.iwf.endpoint.rm.rm0030;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 利用者ロール構成登録リクエスト
 */
public class Rm0030Request extends BaseRequest {
	public String corporationCode;
	public String menuRoleCode;
	public Long seqNoMenuRoleDetail;
}
