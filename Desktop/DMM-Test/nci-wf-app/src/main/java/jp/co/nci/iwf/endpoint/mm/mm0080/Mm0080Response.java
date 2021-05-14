package jp.co.nci.iwf.endpoint.mm.mm0080;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmFunction;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * アクション一覧のレスポンス
 */
public class Mm0080Response extends BasePagingResponse {

	public List<OptionItem> corporations;
	public List<OptionItem> deleteFlags;
	public WfmFunction function;
}
