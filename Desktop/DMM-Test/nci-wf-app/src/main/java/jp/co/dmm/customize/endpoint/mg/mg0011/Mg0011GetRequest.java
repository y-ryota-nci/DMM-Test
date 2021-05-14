package jp.co.dmm.customize.endpoint.mg.mg0011;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0011GetRequest extends BasePagingRequest {

	/** 会社CD */
	public String companyCd;
	/** 組織コード */
	public String orgnzCd;
	/** 品目コード */
	public String itmCd;
	/** 連番 */
	public String sqno;
}
