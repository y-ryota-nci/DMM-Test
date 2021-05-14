package jp.co.nci.iwf.endpoint.wm.wm0230;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmAssignRoleDetail;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 参加者ロール構成設定レスポンス
 */
public class Wm0230Response extends BaseResponse {
	public WfmAssignRoleDetail assignRoleDetail;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;

	/** 所属区分の選択肢 */
	public List<OptionItem> belongTypeList;

}
