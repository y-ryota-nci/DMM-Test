package jp.co.dmm.customize.endpoint.co.co0020;

import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 経常支払マスタ一覧の検索結果レスポンス
 */
public class Co0020SearchResponse extends BasePagingResponse {

	/** 会社コード */
	public String companyCd;

	/** 編集可否フラグ */
	public String editableFlg;
}
