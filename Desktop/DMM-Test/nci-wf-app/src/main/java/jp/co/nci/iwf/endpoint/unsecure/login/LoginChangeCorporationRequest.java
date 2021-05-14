package jp.co.nci.iwf.endpoint.unsecure.login;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ログイン企業の変更リクエスト
 */
public class LoginChangeCorporationRequest extends BaseRequest {
	public String newCorporationCode;
}
