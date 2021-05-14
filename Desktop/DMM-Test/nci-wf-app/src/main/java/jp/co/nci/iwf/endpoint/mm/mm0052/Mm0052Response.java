package jp.co.nci.iwf.endpoint.mm.mm0052;

import java.util.List;

import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmPartsSequenceSpecEx;

/**
 * 通し番号一覧のレスポンス
 */
public class Mm0052Response extends BasePagingResponse {

	public List<OptionItem> corporations;
	public List<OptionItem> deleteFlags;
	public MwmPartsSequenceSpecEx sequence;
}
