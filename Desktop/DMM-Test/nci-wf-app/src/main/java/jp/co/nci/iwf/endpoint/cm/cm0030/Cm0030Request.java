package jp.co.nci.iwf.endpoint.cm.cm0030;

import java.sql.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 役職選択のリクエスト
 */
public class Cm0030Request extends BasePagingRequest {
	public String corporationCode;
	public String postAddedInfo;
	public String postCode;
	public String postName;
	public String postNameAbbr;
	public Date validStartDate;
	public Date validEndDate;
}
