package jp.co.nci.iwf.endpoint.dc.dc0111;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 画面文書定義設定の初期化リクエスト
 */
public class Dc0111InitRequest extends BaseRequest {

	/** */
	private static final long serialVersionUID = 1L;

	public Long screenDocId;
	public Long version;

}
