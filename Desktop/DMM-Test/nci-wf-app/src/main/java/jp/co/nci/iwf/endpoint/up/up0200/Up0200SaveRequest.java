package jp.co.nci.iwf.endpoint.up.up0200;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * プロファイル情報アップロード画面の保存リクエスト
 */
public class Up0200SaveRequest extends BaseRequest {
	/** アップロードファイルのバイトデータをBASE64エンコードしたもの */
	public String encoded;
	/** 未使用ならレコード削除するか */
	public boolean deleteIfNotUse;
	/** アップロードファイル名 */
	public String fileName;
}
