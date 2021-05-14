package jp.co.nci.iwf.endpoint.mm.mm0130;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * メニュー編集の初期化レスポンス
 */
public class Mm0130InitResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	/** ツリーデータ */
	public List<Mm0130TreeItem> treeItems;

}