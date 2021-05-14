package jp.co.dmm.customize.endpoint.batch.purord;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 定期発注バッチ結果レスポンス
 */
public class PurOrdResponse extends BaseResponse {
	/**  */
	private static final long serialVersionUID = 1L;

	/** データ */
	public PurOrdData data;
	/** 結果 */
	public String result;
	/** エラーメッセージ */
	public String errorMessage;
}
