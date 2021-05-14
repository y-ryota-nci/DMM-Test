package jp.co.nci.iwf.endpoint.mm.mm0010;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmLookupGroupEx;

/**
 * ルクアップグループ一覧のレスポンス
 */
public class Mm0010Response extends BasePagingResponse {

	public List<OptionItem> corporations;
	public List<OptionItem> deleteFlags;
	public MwmLookupGroupEx lookupGroup;
}
