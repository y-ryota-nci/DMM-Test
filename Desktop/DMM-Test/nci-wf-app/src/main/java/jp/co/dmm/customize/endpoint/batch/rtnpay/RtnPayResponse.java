package jp.co.dmm.customize.endpoint.batch.rtnpay;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 経常支払バッチ結果レスポンス
 */
public class RtnPayResponse extends BaseResponse {
	/**  */
	private static final long serialVersionUID = 1L;

	/** データ */
	public RtnPayData data;
	/** 結果 */
	public String result;
	/** エラーメッセージ */
	public String errorMessage;
}
