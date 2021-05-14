package jp.co.nci.iwf.endpoint.wm.wm0330;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmChangeRoleDetail;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 参加者ロール構成設定レスポンス
 */
public class Wm0330Response extends BaseResponse {
	public WfmChangeRoleDetail changeRoleDetail;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;

	/** 所属区分の選択肢 */
	public List<OptionItem> changeRoleAssignmentTypeList;

}
