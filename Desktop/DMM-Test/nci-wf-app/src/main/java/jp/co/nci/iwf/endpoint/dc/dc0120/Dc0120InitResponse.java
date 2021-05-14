package jp.co.nci.iwf.endpoint.dc.dc0120;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 業務文書メニュー編集画面の初期化レスポンス
 */
public class Dc0120InitResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	/** ツリーデータ */
	public List<Dc0120TreeItem> treeItems;

}
