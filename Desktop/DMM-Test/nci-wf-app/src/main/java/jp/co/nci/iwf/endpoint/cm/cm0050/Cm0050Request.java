package jp.co.nci.iwf.endpoint.cm.cm0050;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * ユーザ所属選択のリクエスト
 */
public class Cm0050Request extends BasePagingRequest {

	public String corporationCode;
	public String userAddedInfo;
	public String userName;
	public String organizationCode;
	public String organizationName;
	public String postName;
	public String organizationTreeName;
	public Date validStartDate;
	public Date validEndDate;
}
