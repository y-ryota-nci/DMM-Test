package jp.co.nci.iwf.endpoint.rm.rm0710;

import java.util.List;

import jp.co.nci.integrated_workflow.model.base.WfmMenuRole;
import jp.co.nci.iwf.component.OptionItem;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 利用者ロール登録レスポンス
 */
public class Rm0710Response extends BaseResponse {
	public WfmMenuRole menuRole;

	public List<OptionItem> corporations;
}
