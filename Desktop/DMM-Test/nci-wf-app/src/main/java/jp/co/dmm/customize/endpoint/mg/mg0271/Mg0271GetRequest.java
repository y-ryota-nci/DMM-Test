package jp.co.dmm.customize.endpoint.mg.mg0271;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 予算科目マスタ取得リクエスト
 *
 */
public class Mg0271GetRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 予算科目コード */
	public String bgtItmCd;
}
