package jp.co.nci.iwf.endpoint.wm.wm0300;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 参加者変更ロール一覧のレスポンス
 */
public class Wm0300Response extends BasePagingResponse {

	public List<OptionItem> corporations;
	public List<OptionItem> deleteFlags;
	public WfmChangeRole changeRole;
}
