package jp.co.nci.iwf.endpoint.ti.ti0040;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 汎用テーブル定義画面の初期化リクエスト
 */
public class Ti0040InitRequest extends BaseRequest {
	public Long tableId;
	public Long version;
}
