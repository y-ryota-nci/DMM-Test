package jp.co.nci.iwf.endpoint.dc.dc0050;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 文書フォルダ設定画面の移動レスポンス
 */
public class Dc0050MoveResponse extends BaseResponse {

	/** ツリーデータ */
	public List<Dc0050TreeItem> treeItems;
}
