package jp.co.nci.iwf.endpoint.rm.rm0720;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.integrated_workflow.model.custom.WfmMenuRoleDetail;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BasePagingResponse;

/**
 * 利用者ロール設定レスポンス
 */
public class Rm0720Response extends BasePagingResponse {
	public WfmMenuRole menuRole;

	public List<WfmMenuRoleDetail> menuRoleDetailList;

	/** 削除区分の選択肢 */
	public List<OptionItem> deleteFlagList;

}
