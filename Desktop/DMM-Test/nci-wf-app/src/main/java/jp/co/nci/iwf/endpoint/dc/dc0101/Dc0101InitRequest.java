package jp.co.nci.iwf.endpoint.dc.dc0101;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * 文書ファイルの更新画面用リクエスト
 */
public class Dc0101InitRequest extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 文書ID */
	public Long docId;
	/** 文書ファイルID */
	public Long docFileId;
	/** 企業コード */
	public String corporationCode;
}
