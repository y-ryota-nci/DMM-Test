package jp.co.nci.iwf.endpoint.rm.rm0030;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 利用者ロール構成設定レスポンス
 */
public class Rm0030Response extends BaseResponse {
	public WfmMenuRoleDetail menuRoleDetail;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;

	/** メニューロール区分の選択肢 */
	public List<OptionItem> menuRoleAssignmentTypeList;

}
