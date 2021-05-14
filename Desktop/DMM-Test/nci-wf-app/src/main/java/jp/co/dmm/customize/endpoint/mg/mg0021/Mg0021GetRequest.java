package jp.co.dmm.customize.endpoint.mg.mg0021;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0021GetRequest extends BasePagingRequest {

	/** 会社CD */
	public String companyCd;
	/** 費目コード */
	public String itmexpsCd;
}
