package jp.co.nci.iwf.endpoint.dc.dc0022;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 詳細検索リクエスト.
 */
public class Dc0022SearchRequest extends BasePagingRequest {

	public String title;
	public String contentsType;
	public String ownerCorporationCode;
	public String ownerUserCode;
	public String ownerUserName;
	public String publishFlag;
	public Date publishStartDate;
//	public Date publishStartDateTo;
	public Date publishEndDate;
//	public Date publishEndDateTo;
	public String lockFlag;
	public String retentionTermType;
	public String docBusinessInfo001;
	public String docBusinessInfo002;
	public String docBusinessInfo003;
	public String docBusinessInfo004;
	public String docBusinessInfo005;
}
