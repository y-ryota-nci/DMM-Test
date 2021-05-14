package jp.co.nci.iwf.endpoint.up.up0200;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * プロファイル情報アップロード画面のレスポンス
 */
public class Up0200Response extends BaseResponse {
	/** アップロードファイルのバイトデータをBASE64エンコードしたもの */
	public String encoded;
	/** アップロードファイルのファイル名 */
	public String fileName;

	/** 組織マスタ総件数 */
	public long orgCount;
	/** 役職マスタ総件数 */
	public long postCount;
	/** ユーザマスタ総件数 */
	public long userCount;
	/** ユーザ所属マスタ総件数 */
	public long ubCount;
}
