package jp.co.nci.iwf.endpoint.mm.mm0000;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * プロファイル管理画面のユーザの追加リクエスト
 */
public class Mm0000AddUserRequest extends BaseRequest {
	public String corporationCode;
	public String organizationCode;

	/** 基準日 */
	public java.sql.Date baseDate;
}
