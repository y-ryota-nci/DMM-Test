package jp.co.nci.iwf.endpoint.cm.cm0010;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 企業選択画面の検索結果レスポンス
 */
public class Cm0010Response extends BasePagingResponse {

	public List<OptionItem> corporationGroups;
	public List<OptionItem> corporations;

}
