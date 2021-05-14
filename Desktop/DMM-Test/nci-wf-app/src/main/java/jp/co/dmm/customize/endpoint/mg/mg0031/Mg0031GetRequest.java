package jp.co.dmm.customize.endpoint.mg.mg0031;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0031GetRequest extends BasePagingRequest {

	/** 会社CD */
	public String companyCd;
	/** 組織コード */
	public String orgnzCd;
	/** 費目コード（１） */
	public String itmExpsCd1;
	/** 費目コード（２） */
	public String itmExpsCd2;
}
