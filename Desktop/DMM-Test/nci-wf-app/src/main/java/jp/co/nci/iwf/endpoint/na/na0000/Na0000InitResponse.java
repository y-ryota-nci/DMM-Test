package jp.co.nci.iwf.endpoint.na.na0000;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 新規申請フォルダ設定画面の初期化レスポンス
 */
public class Na0000InitResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	/** ツリーデータ */
	public List<Na0000TreeItem> treeItems;

}
