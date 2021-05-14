package jp.co.dmm.customize.endpoint.mg.mg0111;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0111GetRequest extends BasePagingRequest {

	/** 会社CD */
	public String companyCd;
	/** 銀行コード */
	public String bnkCd;
	/** 銀行支店コード */
	public String bnkbrcCd;
}
