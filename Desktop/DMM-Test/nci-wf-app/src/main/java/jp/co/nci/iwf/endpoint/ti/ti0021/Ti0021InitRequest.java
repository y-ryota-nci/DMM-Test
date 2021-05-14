package jp.co.nci.iwf.endpoint.ti.ti0021;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * マスタ権限設定の初期化リクエスト
 */
public class Ti0021InitRequest extends BaseRequest {
	public String corporationCode;
	public String menuRoleCode;
}
