package jp.co.dmm.customize.endpoint.mg.mg0020;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

public class Mg0020SearchRequest extends BasePagingRequest {

	/** 会社コード */
	public String companyCd;
	/** 費目コード */
	public String itmexpsCd;
	/** 費目名称 */
	public String itmexpsNm;
	/** 費目略称 */
	public String itmexpsNmS;
	/** 費目階層 */
	public String itmexpsLevel;

	/** 削除フラグ */
	public boolean dltFgOff;
	public boolean dltFgOn;
}
