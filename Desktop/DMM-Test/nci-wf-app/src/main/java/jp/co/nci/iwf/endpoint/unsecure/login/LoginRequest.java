package jp.co.nci.iwf.endpoint.unsecure.login;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ログイン認証リクエスト
 */
public class LoginRequest extends BaseRequest {
	/** 企業コード */
	private String corporationCode;
	/** ログインID */
	private String userAddedInfo;
	/** 平文パスワード */
	private String password;


	/** 企業コード */
	public String getCorporationCode() {
		return corporationCode;
	}
	/** 企業コード */
	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode;
	}

	/** ログインID */
	public String getUserAddedInfo() {
		return userAddedInfo;
	}
	/** ログインID */
	public void setUserAddedInfo(String userAddedInfo) {
		this.userAddedInfo = userAddedInfo;
	}

	/** 平文パスワード */
	public String getPassword() {
		return password;
	}
	/** 平文パスワード */
	public void setPassword(String plainPassword) {
		this.password = plainPassword;
	}
}
