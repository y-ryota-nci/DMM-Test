package jp.co.nci.iwf.endpoint.wm.wm0200;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 参加者ロール一覧のレスポンス
 */
public class Wm0200Response extends BasePagingResponse {

	public List<OptionItem> corporations;
	public List<OptionItem> deleteFlags;
	public WfmAssignRole assignRole;
}
