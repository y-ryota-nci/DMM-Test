package jp.co.nci.iwf.endpoint.up.up0030;

import java.util.List;

import jp.co.nci.integrated_workflow.model.custom.WfmMenuRole;
import jp.co.nci.iwf.endpoint.up.BaseUploadResponse;

/**
 * メニューロール定義アップロード画面レスポンス
 */
public class Up0030Response extends BaseUploadResponse {

	/** アップロードファイルのプロセス定義名 */
	public List<WfmMenuRole> menuRoles;
	/** アップロードファイルID */
	public long uploadFileId;
}
