package jp.co.dmm.customize.endpoint.mg.mg0021;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Mg0021UpdateRequest extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 費目コード */
	public String itmexpsCd;
	/** 費目階層 */
	public String itmexpsLevel;
	/** 費目名称 */
	public String itmexpsNm;
	/** 費目略称 */
	public String itmexpsNmS;
	/** 削除フラグ */
	public String dltFg;
}
