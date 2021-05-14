package jp.co.nci.iwf.endpoint.vd.vd0310.include;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * ブロック：ワークフロー文書ファイルのリクエスト
 */
public class Bl0015Request extends BaseRequest {

	/**  */
	private static final long serialVersionUID = 1L;

	public Long docFileWfId;
	public String comments;

}
