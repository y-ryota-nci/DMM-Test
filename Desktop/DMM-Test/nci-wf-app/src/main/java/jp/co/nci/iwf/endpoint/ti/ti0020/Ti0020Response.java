package jp.co.nci.iwf.endpoint.ti.ti0020;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * メニューロール一覧（マスタ権限設定）のレスポンス
 */
public class Ti0020Response extends BasePagingResponse {

	public List<OptionItem> deleteFlags;
}
