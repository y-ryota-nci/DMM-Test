package jp.co.dmm.customize.endpoint.pr.pr0010;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 管理_購入依頼の検索リクエスト
 */
public class Pr0010SearchRequest extends BasePagingRequest {

	/** 購入依頼No */
	public String purrqstNo;
	/** 調達部門区分 */
	public String prcFldTp;

}
