package jp.co.dmm.customize.endpoint.dc.dc1000;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * OCR状況一覧の検索結果レスポンス
 */
public class Dc1000Response extends BasePagingResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public List<OptionItem> ocrFlagItems;

}
