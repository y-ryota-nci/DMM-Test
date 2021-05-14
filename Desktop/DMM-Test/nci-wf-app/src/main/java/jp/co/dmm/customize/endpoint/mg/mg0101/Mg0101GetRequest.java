package jp.co.dmm.customize.endpoint.mg.mg0101;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0101GetRequest extends BasePagingRequest {

	/** 会社CD */
	public String companyCd;
	/** 銀行コード */
	public String bnkCd;
}
