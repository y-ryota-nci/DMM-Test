package jp.co.dmm.customize.endpoint.mg.mg0091;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0091GetRequest extends BasePagingRequest {

	/** 会社CD */
	public String companyCd;
	/** 銀行口座コード */
	public String bnkaccCd;
	/** 連番 */
	public String sqno;
}
