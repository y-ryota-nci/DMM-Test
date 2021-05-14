package jp.co.nci.iwf.endpoint.up;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アップロード後レスポンスの基底クラス
 */
public abstract class BaseUploadResponse extends BaseResponse {

	/** アップロードファイルの作成日時 */
	public String timestampCreated;
	/** アップロードファイルのファイル名 */
	public String fileName;
	/** アップロードファイル作成時のDB接続先 */
	public String dbDestination;
	/** アップロードファイル作成時のAPPバージョン */
	public String appVersion;
	/** アップロードファイル作成時のホストIPアドレス */
	public String hostIpAddr;
	/** アップロードファイル作成時のサーバ名 */
	public String hostName;
	/** 企業コード */
	public String corporationCode;
	/** 企業名 */
	public String corporationName;
}
