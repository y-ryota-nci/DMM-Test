package jp.co.dmm.customize.endpoint.mg.mg0331;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 国マスタ取得リクエスト
 *
 */
public class Mg0331Request extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String companyCd;
	/** 国コード */
	public String lndCd;

}
