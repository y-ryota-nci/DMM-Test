package jp.co.nci.iwf.endpoint.mm.mm0090;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmLookupType;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * ルクアップグループ一覧のレスポンス
 */
public class Mm0090Response extends BasePagingResponse {

	public List<OptionItem> corporations;
	public List<OptionItem> deleteFlags;
	public List<OptionItem> updateFlags;
	public WfmLookupType lookupType;
}
