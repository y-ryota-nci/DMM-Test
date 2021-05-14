package jp.co.nci.iwf.endpoint.vd.vd0091;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 新規申請メニュー割当設定の初期化リクエスト
 */
public class Vd0091InitRequest extends BaseRequest {

	public String corporationCode;
	public Long menuId;
	public Long screenProcessMenuId;
	public Long version;

}
