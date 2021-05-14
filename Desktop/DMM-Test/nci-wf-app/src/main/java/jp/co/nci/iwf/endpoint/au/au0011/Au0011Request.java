package jp.co.nci.iwf.endpoint.au.au0011;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * パスワード変更リクエスト
 */
public class Au0011Request extends BaseRequest {
	/** 対象者企業コード */
	public String corporationCode;
	/** 対象者ユーザコード */
	public String userCode;
	/** 新パスワード1 */
	public String newPassword1;
	/** 新パスワード2 */
	public String newPassword2;
}
