package jp.co.nci.iwf.endpoint.up.up0020;

import java.util.List;

import jp.co.nci.iwf.endpoint.up.BaseUploadResponse;

/**
 * プロセス定義アップロード画面のレスポンス
 */
public class Up0020Response extends BaseUploadResponse {

	/** 確認メッセージ */
	public String confirmMessage;
	/** アップロードファイルID */
	public long uploadFileId;
	/** アップロードエントリーリスト */
	public List<Up0020Config> results;
	/** アップロード総件数 */
	public int allCount;
	/** アップロード件数 */
	public int count;
}
