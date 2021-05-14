package jp.co.nci.iwf.endpoint.al.al0010;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * アクセスログ検索の検索リクエスト
 */
public class Al0010SearchRequest extends BasePagingRequest {
	public Long accessLogId;
	public String accessLogResultType;
	public String accessDate;
	public String accessTmFrom;
	public String accessTmTo;
	public String opeCorporationCode;
	public String opeUserAddedInfo;
	public String opeUserName;
	public String opeIpAddress;
	public String screenId;
	public String screenName;
	public String actionName;
	public String sessionId;
	public String userAgent;
	public String appVersion;
	public String dbConnectString;
	public String hostIpAddress;
	public Integer hostPort;
	public String threadName;
	public String spoofingUserAddedInfo;
	public String spoofingUserName;
	public String accessLogDetail;
}
