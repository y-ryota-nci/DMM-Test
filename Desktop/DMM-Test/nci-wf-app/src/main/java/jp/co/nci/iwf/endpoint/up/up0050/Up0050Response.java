package jp.co.nci.iwf.endpoint.up.up0050;

import java.util.List;

import jp.co.nci.iwf.endpoint.up.BaseUploadResponse;
import jp.co.nci.iwf.jpa.entity.mw.MwmMailTemplateFile;

/**
 * メールテンプレート定義アップロード画面のレスポンス
 */
public class Up0050Response extends BaseUploadResponse {

	public List<MwmMailTemplateFile> fileList;
	/** アップロードファイルID */
	public long uploadFileId;

}
