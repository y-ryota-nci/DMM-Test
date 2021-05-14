package jp.co.dmm.customize.endpoint.mg.mg0211;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Mg0211Request extends BaseRequest {

	/** 会社コード */
	public String companyCd;
	/** 源泉税区分 */
	public String hldtaxTp;

	/** 入力内容 */
	public Mg0211Entity inputs;

}
