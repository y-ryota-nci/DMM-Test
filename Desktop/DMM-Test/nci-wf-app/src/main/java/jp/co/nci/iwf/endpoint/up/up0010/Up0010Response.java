package jp.co.nci.iwf.endpoint.up.up0010;

import java.util.List;

import jp.co.nci.iwf.endpoint.up.BaseUploadResponse;

/**
 * 画面定義アップロード画面の保存レスポンス
 */
public class Up0010Response extends BaseUploadResponse {
	/** 確認メッセージ */
	public String confirmMessage;
	/** アップロードファイルの内容リスト */
	public List<Up0010Entity> results;
	/** アップロードファイルID */
	public long uploadFileId;
	/** アップロードファイルの総画面数 */
	public int allCount;
	/** アップロードファイルの選択済み画面数 */
	public int count;
}
