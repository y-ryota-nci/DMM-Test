package jp.co.dmm.customize.endpoint.mg.mg0221;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 支払業務マスタ取得リクエスト
 *
 */
public class Mg0221GetRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 支払業務コード */
	public String payApplCd;
}
