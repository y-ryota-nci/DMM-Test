package jp.co.dmm.customize.endpoint.mg.mg0300;

import jp.co.nci.iwf.jersey.base.BaseResponse;

/**
 * 住所マスタ情報アップロード画面のレスポンス
 */
public class Mg0300UploadResponse extends BaseResponse {

	/** エンコード */
	public String encoded;
	/** アップロードファイルのファイル名 */
	public String fileName;

	/** 住所マスタ総件数 */
	public long zipCount;
}
