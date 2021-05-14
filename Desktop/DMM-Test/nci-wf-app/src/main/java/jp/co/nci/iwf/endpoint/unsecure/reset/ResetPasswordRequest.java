package jp.co.nci.iwf.endpoint.unsecure.reset;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * パスワードリセット画面のリセットリクエスト
 */
public class ResetPasswordRequest extends BaseRequest {

	public String hash;
}
