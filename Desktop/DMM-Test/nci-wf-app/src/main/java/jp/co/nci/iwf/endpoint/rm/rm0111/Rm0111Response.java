package jp.co.nci.iwf.endpoint.rm.rm0111;

import java.util.List;

import jp.co.nci.integrated_workflow.model.base.WfmMenuRole;
import jp.co.nci.iwf.jersey.base.BaseResponse;
import jp.co.nci.iwf.jpa.entity.ex.MwmMenuEx;

/**
 * 業務管理項目名称設定のレスポンス
 */
public class Rm0111Response extends BaseResponse {

	public WfmMenuRole menuRole;
	public List<MwmMenuEx> accessibleMenus;
}
