package jp.co.dmm.customize.endpoint.mg.mg0141;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0141GetRequest extends BasePagingRequest {

	/** 会社CD */
	public String companyCd;
	/** 勘定科目コード */
	public String accCd;
	/** 勘定科目補助コード */
	public String accBrkdwnCd;
	/** 連番 */
	public String sqno;
}
