package jp.co.dmm.customize.endpoint.py.py0070;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 買掛残高のリクエスト
 */
public class Py0070Request extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 勘定科目コード */
	public String accCd;
	/** 勘定科目名称 */
	public String accNm;
	/** 計上月 */
	//public String addMm;
	/** 期間(From) */
	public String addYmS;
	/** 期間(To) */
	public String addYmE;
	/** 金額が０の取引先を除外 */
	public boolean excZeroAmt;
}
