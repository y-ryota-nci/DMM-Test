package jp.co.nci.iwf.endpoint.up.up0020;

import java.util.List;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * プロセス定義アップロード画面の登録リクエスト
 */
public class Up0020Request extends BaseRequest {
	/** アップロードファイルID */
	public Long uploadFileId;
	/** アップロード設定リスト */
	public List<Up0020Config> configs;

	/** 参加者ロール構成をアップロードするか */
	public boolean isAssignRoleDetail;
	/** 参加者変更ロール構成をアップロードするか */
	public boolean isChangeRoleDetail;
}
