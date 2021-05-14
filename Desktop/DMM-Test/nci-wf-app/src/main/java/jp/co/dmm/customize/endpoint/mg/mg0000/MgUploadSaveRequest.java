package jp.co.dmm.customize.endpoint.mg.mg0000;

import jp.co.nci.iwf.jersey.base.BaseRequest;

/**
 * アップロード画面の保存リクエスト
 */
public class MgUploadSaveRequest extends BaseRequest {
	/** アップロードファイルのバイトデータをBASE64エンコードしたもの */
	public String encoded;
	/** 未使用ならレコード削除するか */
	public boolean deleteIfNotUse;
	/** アップロードファイル名 */
	public String fileName;
}
