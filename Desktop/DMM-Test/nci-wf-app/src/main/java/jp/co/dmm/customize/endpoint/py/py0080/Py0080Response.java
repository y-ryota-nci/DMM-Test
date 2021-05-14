package jp.co.dmm.customize.endpoint.py.py0080;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 前払残高の検索結果レスポンス
 */
public class Py0080Response extends BasePagingResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 会社選択肢 */
	public List<OptionItem> companyCds;
}
