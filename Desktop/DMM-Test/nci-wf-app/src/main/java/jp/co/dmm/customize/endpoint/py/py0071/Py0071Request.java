package jp.co.dmm.customize.endpoint.py.py0071;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 買掛残高のリクエスト
 */
public class Py0071Request extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 会社名 */
	public String companyNm;
	/** 取引先コード */
	public String splrCd;
	/** 取引先名称 */
	public String splrNmKj;
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
}
