package jp.co.dmm.customize.endpoint.ct.ct0010;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * カタログ検索画面の検索リクエスト
 */
public class Ct0010SearchRequest extends BasePagingRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String corporationCode;
	public String catalogCode;
	public String catalogName;
	public Long catalogCategoryId;

}
