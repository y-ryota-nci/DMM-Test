package jp.co.dmm.customize.endpoint.mg.mg0000;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * アップロード画面のレスポンス
 */
public class MgUploadResponse extends BaseResponse {

	/** エンコード */
	public String encoded;
	/** アップロードファイルのファイル名 */
	public String fileName;

	/** マスタ総件数 */
	public long count;
}
