package jp.co.nci.iwf.endpoint.wm.wm0230;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 参加者ロール構成登録リクエスト
 */
public class Wm0230Request extends BaseRequest {
	public String corporationCode;
	public String assignRoleCode;
	public Long seqNoAssignRoleDetail;
}
