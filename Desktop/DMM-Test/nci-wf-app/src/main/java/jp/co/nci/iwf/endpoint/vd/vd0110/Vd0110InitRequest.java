package jp.co.nci.iwf.endpoint.vd.vd0110;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面コンテナ設定の初期化リクエスト
 */
public class Vd0110InitRequest extends BaseRequest {
	/** コンテナID */
	public Long containerId;
	/** バージョン */
	public Long version;
}
