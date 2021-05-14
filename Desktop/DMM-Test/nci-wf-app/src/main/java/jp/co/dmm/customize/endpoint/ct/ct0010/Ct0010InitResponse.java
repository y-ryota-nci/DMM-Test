package jp.co.dmm.customize.endpoint.ct.ct0010;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * カタログ検索画面の初期化レスポンス
 */
public class Ct0010InitResponse extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	/** カタログカテゴリーの選択肢 */
	public List<OptionItem> catalogCategories;

}
