package jp.co.nci.iwf.endpoint.vd.vd0050;

import java.util.Date;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 外部Javascript一覧の検索リクエスト
 */
public class Vd0050SearchRequest extends BasePagingRequest {

	public String corporationCode;
	public String fileName;
	public String remarks;
	public Date from;
	public Date to;
	public boolean includeOldVersions;

}
