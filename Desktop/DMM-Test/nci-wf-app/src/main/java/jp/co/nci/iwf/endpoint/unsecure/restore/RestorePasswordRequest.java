package jp.co.nci.iwf.endpoint.unsecure.restore;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * パスワード復旧リクエスト
 */
public class RestorePasswordRequest extends BaseRequest {
	/** 企業コード */
	public String corporationCode;
	/** ログインIDまたはメールアドレス */
	public String loginIdOrMailAddress;
}
