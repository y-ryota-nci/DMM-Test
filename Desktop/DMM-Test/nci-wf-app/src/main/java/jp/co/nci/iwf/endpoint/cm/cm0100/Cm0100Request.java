package jp.co.nci.iwf.endpoint.cm.cm0100;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 参加者変更ユーザ検索のリクエスト
 */
public class Cm0100Request extends BasePagingRequest {
	public String corporationCode;
	public String userCode;
	public String userName;
	public String userAddedInfo;
	public String userNameAbbr;
	public Date validStartDate;
	public Date validEndDate;

	public String processDefCode;
	public String processDefDetailCode;
	public String activityDefCode;
	public String changeRoleCode;

	// 絞込み条件
	/** 組織コード */
	public String organizationCodeStart;
}
