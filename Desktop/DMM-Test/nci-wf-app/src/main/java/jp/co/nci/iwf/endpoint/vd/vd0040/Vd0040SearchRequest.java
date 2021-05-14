package jp.co.nci.iwf.endpoint.vd.vd0040;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 画面プロセス定義一覧の検索リクエスト
 */
public class Vd0040SearchRequest extends BasePagingRequest {

	public String corporationCode;
	public String processDefCode;
	public String screenProcessCode;
	public String screenProcessName;
	public String screenCode;
	public String screenName;
	public Date validEndDate;
	public Date validStartDate;
}
