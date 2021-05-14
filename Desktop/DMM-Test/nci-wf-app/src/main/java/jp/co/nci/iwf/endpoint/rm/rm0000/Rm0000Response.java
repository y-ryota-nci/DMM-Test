package jp.co.nci.iwf.endpoint.rm.rm0000;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 利用者ロール一覧のレスポンス
 */
public class Rm0000Response extends BasePagingResponse {

	public List<OptionItem> corporations;
	public List<OptionItem> deleteFlags;
	public WfmMenuRole menuRole;
}
