package jp.co.dmm.customize.endpoint.sp.sp0011;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 取引先登録情報取得リクエスト
 */
public class Sp0011GetRequest extends BasePagingRequest {

	/** 会社CD */
	public String companyCd;
	/** 取引先コード */
	public String splrCd;
}
