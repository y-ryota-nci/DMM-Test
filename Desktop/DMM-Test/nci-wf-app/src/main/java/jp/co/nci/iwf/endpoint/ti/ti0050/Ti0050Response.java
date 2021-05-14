package jp.co.nci.iwf.endpoint.ti.ti0050;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmTableEx;

/**
 * 汎用テーブル検索条件一覧のレスポンス
 */
public class Ti0050Response extends BasePagingResponse {
	public MwmTableEx table;
	public List<OptionItem> deleteFlags;
}
