package jp.co.nci.iwf.endpoint.dc.dc0050;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;
import jp.co.nci.iwf.jpa.entity.ex.MwvDocFolder;
import jp.co.nci.iwf.jpa.entity.mw.MwmDocFolderAccessibleInfo;

/**
 * 文書フォルダ設定画面の保存（追加、編集、削除）リクエスト
 */
public class Dc0050SaveRequest extends BaseRequest {

//	public Long docFolderId;
//	public String folderName;
//	public Date validStartDate;
//	public Date validEndDate;
//	public Integer sortOrder;

	public MwvDocFolder folder;
	public List<MwmDocFolderAccessibleInfo> accessibles;
//	public String deleteFlag;
}
