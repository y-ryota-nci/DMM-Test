package jp.co.nci.iwf.endpoint.vd.vd0030;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 画面一覧の検索リクエスト
 */
public class Vd0030SearchRequest extends BasePagingRequest {
	public String containerCode;
	public String containerName;
	public String corporationCode;
	public String corporationName;
	public String screenCode;
	public String screenName;
	public String scratchFlag;
}
