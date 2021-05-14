package jp.co.nci.iwf.endpoint.wm.wm0220;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmAssignRole;
import jp.co.nci.integrated_workflow.model.custom.WfmAssignRoleDetail;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 参加者ロール設定レスポンス
 */
public class Wm0220Response extends BasePagingResponse {
	public WfmAssignRole assignRole;

	public List<WfmAssignRoleDetail> assignRoleDetailList;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;

}
