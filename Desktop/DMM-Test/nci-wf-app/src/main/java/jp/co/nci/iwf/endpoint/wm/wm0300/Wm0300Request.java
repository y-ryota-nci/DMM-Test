package jp.co.nci.iwf.endpoint.wm.wm0300;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 参加者変更ロール一覧のリクエスト
 */
public class Wm0300Request extends BasePagingRequest {

	public String corporationCode;
	public String changeRoleCode;
	public String changeRoleName;
	public Date validStartDate;
	public Date validEndDate;
	public String deleteFlag;
}
