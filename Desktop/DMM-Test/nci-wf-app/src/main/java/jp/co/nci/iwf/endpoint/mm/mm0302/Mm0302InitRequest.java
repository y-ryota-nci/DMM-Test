package jp.co.nci.iwf.endpoint.mm.mm0302;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ルート編集リクエスト
 */
public class Mm0302InitRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 会社コード */
	public String corporationCode;
	/** プロセス定義コード */
	public String processDefCode;
	/** プロセス定義コード枝番 */
	public String processDefDetailCode;
	public Long timestampUpdated;

}
