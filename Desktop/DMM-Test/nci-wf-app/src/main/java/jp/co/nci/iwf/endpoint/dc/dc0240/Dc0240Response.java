package jp.co.nci.iwf.endpoint.dc.dc0240;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 文書トレイ一覧(管理者用)の検索レスポンス
 */
public class Dc0240Response extends BasePagingResponse {

	public List<OptionItem> systemFlags;

}
