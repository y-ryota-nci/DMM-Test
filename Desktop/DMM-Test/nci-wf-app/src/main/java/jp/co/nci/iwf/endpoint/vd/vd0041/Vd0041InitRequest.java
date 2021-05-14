package jp.co.nci.iwf.endpoint.vd.vd0041;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面プロセス定義設定の初期化リクエスト
 */
public class Vd0041InitRequest extends BaseRequest {

	public Long screenProcessId;
	public Long version;
}
