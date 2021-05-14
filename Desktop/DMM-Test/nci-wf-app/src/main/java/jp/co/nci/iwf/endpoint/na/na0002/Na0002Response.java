package jp.co.nci.iwf.endpoint.na.na0002;

import java.util.List;

import jp.co.nci.integrated_workflow.model.base.WfmMenuRole;
import jp.co.nci.iwf.endpoint.na.na0002.entity.AccessibleScreenEntity;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アクセス可能画面設定レスポンス
 */
public class Na0002Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmMenuRole menuRole;
	public List<AccessibleScreenEntity> accessibleScreens;
	public List<Na0002TreeItem> treeItems;

}
