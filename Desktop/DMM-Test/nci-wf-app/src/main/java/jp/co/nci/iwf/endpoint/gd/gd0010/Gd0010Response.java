package jp.co.nci.iwf.endpoint.gd.gd0010;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ガジェット(件数)画面の検索結果レスポンス
 */
public class Gd0010Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public boolean worklist;
	public boolean ownlist;
	public boolean newApplication;

	public int applicationPendingCount;
	public int approvalPendingCount;
	public int approvedCount;

}
