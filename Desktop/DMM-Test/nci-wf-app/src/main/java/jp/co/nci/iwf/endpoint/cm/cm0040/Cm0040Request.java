package jp.co.nci.iwf.endpoint.cm.cm0040;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ユーザ選択のリクエスト
 */
public class Cm0040Request extends BasePagingRequest {
	public String corporationCode;
	public String userCode;
	public String userName;
	public String userAddedInfo;
	public String userNameAbbr;
	public Date validStartDate;
	public Date validEndDate;

	// 絞込み条件
	/** 組織コード */
	public String organizationCode;
}
