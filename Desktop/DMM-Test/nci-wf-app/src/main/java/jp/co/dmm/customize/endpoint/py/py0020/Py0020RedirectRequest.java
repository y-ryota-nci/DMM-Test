package jp.co.dmm.customize.endpoint.py.py0020;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面遷移用検索リクエスト
 */
public class Py0020RedirectRequest extends BaseRequest {

	/** */
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String companyCd;
	/** 支払No */
	public String payNo;

}
