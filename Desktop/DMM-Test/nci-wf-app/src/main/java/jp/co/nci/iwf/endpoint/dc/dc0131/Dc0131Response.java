package jp.co.nci.iwf.endpoint.dc.dc0131;

import java.util.List;

import jp.co.nci.integrated_workflow.model.base.WfmMenuRole;
import jp.co.nci.iwf.endpoint.dc.dc0131.entity.AccessibleDocEntity;
import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アクセス可能画面設定レスポンス
 */
public class Dc0131Response extends BaseResponse {

	/**  */
	private static final long serialVersionUID = 1L;

	public WfmMenuRole menuRole;
	public List<AccessibleDocEntity> accessibleDocs;
	public List<Dc0131TreeItem> treeItems;

}
