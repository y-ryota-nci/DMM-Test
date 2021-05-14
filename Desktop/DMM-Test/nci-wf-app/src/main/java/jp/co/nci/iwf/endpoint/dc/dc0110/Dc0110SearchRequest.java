package jp.co.nci.iwf.endpoint.dc.dc0110;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 画面文書定義一覧の検索リクエスト
 */
public class Dc0110SearchRequest extends BasePagingRequest {

	/** */
	private static final long serialVersionUID = 1L;

	public String corporationCode;
	public String screenDocCode;
	public String screenDocName;
	public String screenCode;
	public String screenName;
	public Date validEndDate;
	public Date validStartDate;

}
