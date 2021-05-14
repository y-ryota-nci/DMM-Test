package jp.co.nci.iwf.endpoint.up.up0040;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * トレイ表示設定アップロード画面の保存リクエスト
 */
public class Up0040Request extends BaseRequest {
	/** トレイ表示項目設定.共有設定＝ユーザをアップロード対象にするか */
	public boolean inlcudeUser;
	/** アップロードファイルID */
	public Long uploadFileId;
}
