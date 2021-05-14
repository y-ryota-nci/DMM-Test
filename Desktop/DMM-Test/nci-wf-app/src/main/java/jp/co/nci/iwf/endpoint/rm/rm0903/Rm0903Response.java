package jp.co.nci.iwf.endpoint.rm.rm0903;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * ASP管理者ロール構成設定レスポンス
 */
public class Rm0903Response extends BaseResponse {
	public WfmMenuRoleDetail menuRoleDetail;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;

	/** メニューロール区分の選択肢 */
	public List<OptionItem> menuRoleAssignmentTypeList;

}
