package jp.co.nci.iwf.endpoint.mm.mm0420;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 組織一覧画面の検索リクエスト
 */
public class Mm0420Request extends BasePagingRequest {

	public String corporationCode;
	public String organizationAddedInfo;
	public String organizationName;
	public String organizationNameAbbr;
	public String organizationCodeUp;
	public String organizationNameUp;
	public Integer organizationLevel;
	public String telNum;
	public String faxNum;
	public String deleteFlag;
	public Date validEndDate;
	public Date validStartDate;
}
