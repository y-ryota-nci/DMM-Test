package jp.co.dmm.customize.endpoint.mg.mg0131;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0131GetRequest extends BasePagingRequest {

	/** 会社CD */
	public String companyCd;
	/** 勘定科目コード */
	public String accCd;
	/** 連番 */
	public String sqno;
}
