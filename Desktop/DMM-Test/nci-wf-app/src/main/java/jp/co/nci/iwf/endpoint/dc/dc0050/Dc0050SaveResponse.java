package jp.co.nci.iwf.endpoint.dc.dc0050;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 文書フォルダ設定画面の保存（新規、編集、削除）レスポンス
 */
public class Dc0050SaveResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	/** ツリーデータ */
	public Dc0050TreeItem treeItem;
}
