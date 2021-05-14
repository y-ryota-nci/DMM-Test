package jp.co.nci.iwf.endpoint.mm.mm0100;

import jp.co.nci.iwf.jersey.base.BasePagingRequest;

/**
 * 情報共有設定の検索リクエスト
 */
public class Mm0100SearchRequest extends BasePagingRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String corporationCode;
	/** プロセス定義コード */
	public String processDefCode;
	/** プロセス定義コード枝番 */
	public String processDefDetailCode;

}
