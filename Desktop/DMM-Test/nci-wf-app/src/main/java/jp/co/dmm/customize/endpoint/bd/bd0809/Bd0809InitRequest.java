package jp.co.dmm.customize.endpoint.bd.bd0809;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 予算分析明細確認画面の初期化リクエスト
 */
public class Bd0809InitRequest extends BaseRequest {
	public String companyCd;
	public String yrCd;
	public String mm;
	public String rcvCostPayTp;
	public String bgtItmCd;
	public String organizationCodeLv2;
	public String organizationCodeLv3;
}
