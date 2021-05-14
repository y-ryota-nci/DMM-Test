package jp.co.dmm.customize.endpoint.mg.mg0331;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 国マスタ更新リクエスト
 */
public class Mg0331SaveRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public String lndCd;
	public String lndNm;
	public String lndCdDjii;
	public String sortOrder;
	public String dltFg;

}
