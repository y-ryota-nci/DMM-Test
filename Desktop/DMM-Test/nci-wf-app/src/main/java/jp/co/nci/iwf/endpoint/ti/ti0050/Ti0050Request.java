package jp.co.nci.iwf.endpoint.ti.ti0050;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 汎用テーブル検索条件一覧のリクエスト
 */
public class Ti0050Request extends BasePagingRequest {
	public Long tableId;
	public String tableSearchCode;
	public String tableSearchName;
	public String displayName;
	public String defaultSearchFlag;
	public String deleteFlag;
}
