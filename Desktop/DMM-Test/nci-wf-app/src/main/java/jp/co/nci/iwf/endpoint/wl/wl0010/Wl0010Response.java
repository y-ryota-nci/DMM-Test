package jp.co.nci.iwf.endpoint.wl.wl0010;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * トレイ設定一覧（管理者）の検索レスポンス
 */
public class Wl0010Response extends BasePagingResponse {
	public List<OptionItem> systemFlags;
}
