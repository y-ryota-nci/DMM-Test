package jp.co.dmm.customize.endpoint.ri.ri0010;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 検収対象選択の検索結果レスポンス
 */
public class Ri0010SearchResponse extends BasePagingResponse {
	/** 通貨の選択肢 */
	public List<OptionItem> mnyCds;
}
