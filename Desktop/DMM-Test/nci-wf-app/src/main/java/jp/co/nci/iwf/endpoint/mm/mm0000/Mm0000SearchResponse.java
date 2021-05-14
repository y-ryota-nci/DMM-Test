package jp.co.nci.iwf.endpoint.mm.mm0000;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * プロファイル管理の検索結果レスポンス
 */
public class Mm0000SearchResponse extends BaseResponse {
	/** ツリーデータ */
	public List<Mm0000TreeItem> treeItems;
}
