package jp.co.nci.iwf.endpoint.vd.vd0130;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * コンテナ設定の初期化リクエスト
 */
public class Vd0130InitRequest extends BaseRequest {
	/** コンテナID */
	public long containerId;
}
