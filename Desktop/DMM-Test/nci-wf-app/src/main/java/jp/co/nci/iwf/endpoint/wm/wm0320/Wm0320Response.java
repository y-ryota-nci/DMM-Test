package jp.co.nci.iwf.endpoint.wm.wm0320;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmChangeRole;
import jp.co.nci.integrated_workflow.model.custom.WfmChangeRoleDetail;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 参加者変更ロール設定レスポンス
 */
public class Wm0320Response extends BasePagingResponse {
	public WfmChangeRole changeRole;

	public List<WfmChangeRoleDetail> changeRoleDetailList;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;

}
