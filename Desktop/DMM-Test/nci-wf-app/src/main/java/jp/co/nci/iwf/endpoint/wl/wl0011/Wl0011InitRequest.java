package jp.co.nci.iwf.endpoint.wl.wl0011;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * トレイ編集の初期化リクエスト
 */
public class Wl0011InitRequest extends BaseRequest {
	public Long trayConfigId;
	public Long version;
	public String from;
}
