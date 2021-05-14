package jp.co.dmm.customize.endpoint.suggestion.bumon;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 部門サジェスチョン用リクエスト
 */
public class DmmSuggestionBumonRequest extends BasePagingRequest {
	public String corporationCode;
	public String bumonCd;
	public String bumonNm;
}
