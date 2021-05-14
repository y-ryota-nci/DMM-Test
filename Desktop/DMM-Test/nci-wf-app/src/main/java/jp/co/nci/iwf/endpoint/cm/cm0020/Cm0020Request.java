package jp.co.nci.iwf.endpoint.cm.cm0020;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 組織選択のリクエスト
 */
public class Cm0020Request extends BasePagingRequest {
	public String corporationCode;
	public String organizationAddedInfo;
	public String organizationCode;
	public String organizationName;
	public String organizationNameAbbr;
	public String organizationTreeName;
	public Integer organizationLevel;
	public Date validStartDate;
	public Date validEndDate;
}
