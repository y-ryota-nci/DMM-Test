package jp.co.nci.iwf.endpoint.mm.mm0070;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmAction;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * アクション一覧のレスポンス
 */
public class Mm0070Response extends BasePagingResponse {

	public List<OptionItem> corporations;
	public List<OptionItem> deleteFlags;
	public WfmAction action;
}
