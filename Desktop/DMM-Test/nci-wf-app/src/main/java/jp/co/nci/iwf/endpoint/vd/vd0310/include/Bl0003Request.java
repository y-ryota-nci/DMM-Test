package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ブロック：添付ファイルのリクエスト
 */
public class Bl0003Request extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public Long attachFileWfId;
	public String comments;

}
