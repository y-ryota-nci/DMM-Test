package jp.co.dmm.customize.endpoint.mg.mg0200;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

public class Mg0200Request extends BaseRequest {
	public static final int SEARCH_TYPE_NORMAL = 0;
	public static final int SEARCH_TYPE_PREV = 1;
	public static final int SEARCH_TYPE_NEXT = 2;

	/** 会社コード */
	public String companyCd;
	/** 年月 */
	public String clndYm;
	/** 検索種別 */
	public int searchType;
	/** 会計カレンダリスト */
	public List<Mg0200Entity> entitys;

}
