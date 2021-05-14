package jp.co.nci.iwf.endpoint.mm.mm0430;

import java.util.List;

import jp.co.nci.integrated_workflow.model.base.WfmPost;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 役職一覧のレスポンス
 */
public class Mm0430Response extends BasePagingResponse {

	public List<OptionItem> corporations;
	public List<OptionItem> deleteFlags;
	public WfmPost newPost;
}
