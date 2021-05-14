package jp.co.nci.iwf.endpoint.wm.wm0330;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 参加者変更ロール構成登録リクエスト
 */
public class Wm0330Request extends BaseRequest {
	public String corporationCode;
	public String changeRoleCode;
	public Long seqNoChangeRoleDetail;
}
