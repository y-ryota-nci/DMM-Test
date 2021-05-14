package jp.co.dmm.customize.endpoint.mg.mg0200;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

public class Mg0200Response extends BaseResponse {

	/** 会社コード */
	public String companyCd;
	/** 年月 */
	public String clndYm;
	/** カレンダ件数 */
	public int count;
	/** カレンダリスト */
	public List<Mg0200Entity> entitys;
}
