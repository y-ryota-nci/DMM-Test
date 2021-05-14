package jp.co.nci.iwf.endpoint.ti.ti0051;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 汎用テーブル検索条件設定の初期化リクエスト
 */
public class Ti0051InitRequest extends BaseRequest {

	public Long tableId;
	public Long tableSearchId;
	public Long version;
}
