package jp.co.nci.iwf.endpoint.vd.vd0031;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面設定の初期化リクエスト
 */
public class Vd0031InitRequest extends BaseRequest {
	public Long screenId;
	public Long version;
}
