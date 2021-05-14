package jp.co.nci.iwf.endpoint.wm.wm0200;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 参加者ロール一覧のリクエスト
 */
public class Wm0200Request extends BasePagingRequest {

	public String corporationCode;
	public String assignRoleCode;
	public String assignRoleName;
	public Date validStartDate;
	public Date validEndDate;
	public String deleteFlag;
}
